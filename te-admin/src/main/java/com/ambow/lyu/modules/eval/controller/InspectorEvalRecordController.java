package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.dto.EvalTaskItemScoreDto;
import com.ambow.lyu.common.exception.TeException;
import com.ambow.lyu.common.utils.Constant;
import com.ambow.lyu.common.utils.ExcelUtils;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.InspectorEvalRecordEntity;
import com.ambow.lyu.modules.eval.entity.InspectorEvalTaskItemEntity;
import com.ambow.lyu.modules.eval.service.InspectorEvalRecordService;
import com.ambow.lyu.modules.eval.service.InspectorEvalTaskItemService;
import com.ambow.lyu.modules.sys.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


/**
 * 督导评价记录
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-06-05 09:17:11
 */
@RestController
@RequestMapping("eval/inspectorevalrecord")
public class InspectorEvalRecordController {
    @Autowired
    private InspectorEvalRecordService inspectorEvalRecordService;
    @Autowired
    private InspectorEvalTaskItemService inspectorEvalTaskItemService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 列表
     */
    @RequestMapping("/list/{subTaskId}")
    @RequiresPermissions("eval:inspectorevaltask:eval")
    public Response list(@PathVariable("subTaskId") Long subTaskId, @RequestParam Map<String, Object> params){

        params.put(Constant.SUB_TASK_ID, subTaskId);

        PageUtils page = inspectorEvalRecordService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:inspectorevaltask:eval")
    public Response info(@PathVariable("id") Long id){
        InspectorEvalRecordEntity inspectorEvalRecord = inspectorEvalRecordService.getById(id);

        //姓名
        String name = sysUserService.getById(inspectorEvalRecord.getUserId()).getName();
        inspectorEvalRecord.setUserName(name);

        //分数细节
        List<EvalTaskItemScoreDto> list = EvalTaskItemScoreDto.string2list(inspectorEvalRecord.getDetail());
        inspectorEvalRecord.setEvalItemResults(list);

        return Response.ok().put("inspectorEvalRecord", inspectorEvalRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:inspectorevaltask:eval")
    public Response save(@RequestBody InspectorEvalRecordEntity inspectorEvalRecord){

        ValidatorUtils.validateEntity(inspectorEvalRecord);

        inspectorEvalRecord.setDetail(EvalTaskItemScoreDto.list2string(inspectorEvalRecord.getEvalItemResults()));
        inspectorEvalRecord.setScore(EvalTaskItemScoreDto.calculateScore(inspectorEvalRecord.getEvalItemResults()));
        inspectorEvalRecord.setUpdateTime(new Date());

        inspectorEvalRecordService.save(inspectorEvalRecord);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:inspectorevaltask:eval")
    public Response update(@RequestBody InspectorEvalRecordEntity inspectorEvalRecord){
        ValidatorUtils.validateEntity(inspectorEvalRecord);

        //数据是否存在校验
        int sum = inspectorEvalRecordService.count( new QueryWrapper<InspectorEvalRecordEntity>()
                .eq("sub_task_id",inspectorEvalRecord.getSubTaskId()).eq("user_id",inspectorEvalRecord.getUserId()));
        if(sum != 0){
            throw new TeException("该教师已添加督导评价记录！！！");
        }

        InspectorEvalRecordEntity dbData = inspectorEvalRecordService.getById(inspectorEvalRecord.getId());
        dbData.setDetail(EvalTaskItemScoreDto.list2string(inspectorEvalRecord.getEvalItemResults()));
        dbData.setScore(EvalTaskItemScoreDto.calculateScore(inspectorEvalRecord.getEvalItemResults()));
        dbData.setUpdateTime(new Date());

        inspectorEvalRecordService.updateById(inspectorEvalRecord);
        
        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:inspectorevaltask:eval")
    public Response delete(@RequestBody Long[] ids){
        inspectorEvalRecordService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

    /**
     * 文件上传导入
     */
    @RequestMapping(value = "/import/{taskId}/{subTaskId}", method = RequestMethod.POST
            , consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("eval:inspectorevaltask:eval")
    public Response fileImport(@PathVariable("taskId") Long taskId,
                               @PathVariable("subTaskId") Long subTaskId, @RequestParam("xlsRecordFile") MultipartFile xlsRecordFile) {
        try {
            List<InspectorEvalTaskItemEntity> itemEntities = inspectorEvalTaskItemService.selectByTaskId(taskId);

            Map<String, List<EvalTaskItemScoreDto>> score = ExcelUtils.readInspectorEvalScore(
                    xlsRecordFile.getInputStream(),xlsRecordFile.getOriginalFilename(),itemEntities);

            Response response = Response.ok();
            List<Map<String,String>> successList = new ArrayList<>();
            List<Map<String,String>> errorList = new ArrayList<>();

            for(String name : score.keySet()){
                Map<String,String> itemResult = new HashMap<>(3);
                itemResult.put("name",name);
                try{
                    boolean result = inspectorEvalRecordService.add(taskId,subTaskId,name,score.get(name));
                    if(result){
                        itemResult.put("score",""+ EvalTaskItemScoreDto.calculateScore(score.get(name)));
                        successList.add(itemResult);
                    }else {
                        throw new TeException("数据库异常，添加失败");
                    }
                }catch (Exception ex){
                    itemResult.put("reason",ex.getMessage());
                    errorList.add(itemResult);
                }
            }

            response.put("successList",successList);
            response.put("errorList",errorList);
            return response;
        }catch (Exception e) {
            //捕获所有的异常，使用bootstrap-fileinput需要返回error字段
            String error = e.getMessage();
            return Response.error().put("error",error);
        }

    }

}
