package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.dto.ColOrInspectorEvalScoreDto;
import com.ambow.lyu.common.dto.EvalTaskItemScoreDto;
import com.ambow.lyu.common.exception.TeException;
import com.ambow.lyu.common.utils.Constant;
import com.ambow.lyu.common.utils.ExcelUtils;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalRecordEntity;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalTaskItemEntity;
import com.ambow.lyu.modules.eval.service.ColleagueEvalRecordService;
import com.ambow.lyu.modules.eval.service.ColleagueEvalTaskItemService;
import com.ambow.lyu.modules.sys.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


/**
 * 同行评价记录
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-06-05 09:17:11
 */
@RestController
@RequestMapping("eval/colleagueevalrecord")
public class ColleagueEvalRecordController {
    @Autowired
    private ColleagueEvalRecordService colleagueEvalRecordService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ColleagueEvalTaskItemService colleagueEvalTaskItemService;

    /**
     * 列表
     */
    @RequestMapping("/list/{subTaskId}")
    @RequiresPermissions("eval:colleagueevaltask:eval")
    public Response list(@PathVariable("subTaskId") Long subTaskId, @RequestParam Map<String, Object> params) {

        params.put(Constant.SUB_TASK_ID, subTaskId);

        PageUtils page = colleagueEvalRecordService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:colleagueevaltask:eval")
    public Response info(@PathVariable("id") Long id) {
        ColleagueEvalRecordEntity colleagueEvalRecord = colleagueEvalRecordService.getById(id);

        //姓名
        String name = sysUserService.getById(colleagueEvalRecord.getUserId()).getName();
        colleagueEvalRecord.setUserName(name);

        //分数细节
        List<EvalTaskItemScoreDto> list = EvalTaskItemScoreDto.string2list(colleagueEvalRecord.getDetail());
        colleagueEvalRecord.setEvalItemResults(list);

        return Response.ok().put("colleagueEvalRecord", colleagueEvalRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:colleagueevaltask:eval")
    public Response save(@RequestBody ColleagueEvalRecordEntity colleagueEvalRecord) {

        ValidatorUtils.validateEntity(colleagueEvalRecord);

        //数据是否存在校验
        int sum = colleagueEvalRecordService.count( new QueryWrapper<ColleagueEvalRecordEntity>()
                .eq("sub_task_id",colleagueEvalRecord.getSubTaskId()).eq("user_id",colleagueEvalRecord.getUserId()));
        if(sum != 0){
            throw new TeException("该教师已添加同行评价记录！！！");
        }

        colleagueEvalRecord.setDetail(EvalTaskItemScoreDto.list2string(colleagueEvalRecord.getEvalItemResults()));
        colleagueEvalRecord.setScore(EvalTaskItemScoreDto.calculateScore(colleagueEvalRecord.getEvalItemResults()));
        colleagueEvalRecord.setUpdateTime(new Date());

        colleagueEvalRecordService.save(colleagueEvalRecord);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:colleagueevaltask:eval")
    public Response update(@RequestBody ColleagueEvalRecordEntity colleagueEvalRecord) {
        ValidatorUtils.validateEntity(colleagueEvalRecord);

        ColleagueEvalRecordEntity dbData = colleagueEvalRecordService.getById(colleagueEvalRecord.getId());
        dbData.setDetail(EvalTaskItemScoreDto.list2string(colleagueEvalRecord.getEvalItemResults()));
        dbData.setScore(EvalTaskItemScoreDto.calculateScore(colleagueEvalRecord.getEvalItemResults()));
        dbData.setUpdateTime(new Date());

        colleagueEvalRecordService.updateById(dbData);

        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:colleagueevaltask:eval")
    public Response delete(@RequestBody Long[] ids) {
        colleagueEvalRecordService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

    /**
     * 文件上传导入
     */
    @RequestMapping(value = "/import/{taskId}/{subTaskId}", method = RequestMethod.POST
            , consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("eval:colleagueevaltask:eval")
    public Response fileImport(@PathVariable("taskId") Long taskId,
                               @PathVariable("subTaskId") Long subTaskId, @RequestParam("xlsRecordFile") MultipartFile xlsRecordFile) {
        try {
            List<ColleagueEvalTaskItemEntity> itemEntities = colleagueEvalTaskItemService.selectByTaskId(taskId);

            List<ColOrInspectorEvalScoreDto> xlsResult = ExcelUtils.readColleagueEvalScore(
                    xlsRecordFile.getInputStream(),xlsRecordFile.getOriginalFilename(),itemEntities);

            Response response = Response.ok();
            List<ColOrInspectorEvalScoreDto> successList = new ArrayList<>();
            List<ColOrInspectorEvalScoreDto> errorList = new ArrayList<>();

            for(ColOrInspectorEvalScoreDto item : xlsResult){
                try{
                    boolean result = colleagueEvalRecordService.add(taskId,subTaskId,item.getUsername(),item.getName(),item.getDetails());
                    if(result){
                        item.setScore(""+EvalTaskItemScoreDto.calculateScore(item.getDetails()));
                        successList.add(item);
                    }else {
                        throw new TeException("数据库异常，添加失败");
                    }
                }catch (Exception ex){
                    item.setReason(ex.getMessage());
                    errorList.add(item);
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