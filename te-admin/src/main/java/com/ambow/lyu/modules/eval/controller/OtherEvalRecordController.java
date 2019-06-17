package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.exception.TeException;
import com.ambow.lyu.common.utils.Constant;
import com.ambow.lyu.common.utils.ExcelUtils;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.OtherEvalRecordEntity;
import com.ambow.lyu.modules.eval.service.OtherEvalRecordService;
import com.ambow.lyu.modules.sys.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.regex.Pattern;


/**
 * 其他评价记录
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-06-05 09:17:10
 */
@RestController
@RequestMapping("eval/otherevalrecord")
public class OtherEvalRecordController {
    @Autowired
    private OtherEvalRecordService otherEvalRecordService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 列表
     */
    @RequestMapping("/list/{subTaskId}")
    @RequiresPermissions("eval:otherevaltask:eval")
    public Response list(@PathVariable("subTaskId") Long subTaskId,@RequestParam Map<String, Object> params){

        params.put(Constant.SUB_TASK_ID, subTaskId);

        PageUtils page = otherEvalRecordService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:otherevaltask:eval")
    public Response info(@PathVariable("id") Long id){
        OtherEvalRecordEntity otherEvalRecord = otherEvalRecordService.getById(id);

        //姓名
        String name = sysUserService.getById(otherEvalRecord.getUserId()).getName();
        otherEvalRecord.setUserName(name);

        return Response.ok().put("otherEvalRecord", otherEvalRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:otherevaltask:eval")
    public Response save(@RequestBody OtherEvalRecordEntity otherEvalRecord){

        //数据校验
        ValidatorUtils.validateEntity(otherEvalRecord);

        //数据是否存在校验
        int sum = otherEvalRecordService.count( new QueryWrapper<OtherEvalRecordEntity>()
                .eq("sub_task_id",otherEvalRecord.getSubTaskId()).eq("user_id",otherEvalRecord.getUserId()));
        if(sum != 0){
            throw new TeException("该教师已添加其他评价记录！！！");
        }


        otherEvalRecord.setUpdateTime(new Date());

        otherEvalRecordService.save(otherEvalRecord);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:otherevaltask:eval")
    public Response update(@RequestBody OtherEvalRecordEntity otherEvalRecord){
        ValidatorUtils.validateEntity(otherEvalRecord);
        otherEvalRecord.setUpdateTime(new Date());
        otherEvalRecordService.updateById(otherEvalRecord);
        
        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:otherevaltask:eval")
    public Response delete(@RequestBody Long[] ids){
        otherEvalRecordService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

    /**
     * 文件上传导入
     */
    @RequestMapping(value = "/import/{taskId}/{subTaskId}",method=RequestMethod.POST
            ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("eval:studentevaltask:eval")
    public Response fileImport(@PathVariable("taskId") Long taskId,@PathVariable("subTaskId") Long subTaskId,@RequestParam("xlsRecordFile") MultipartFile xlsRecordFile) {
        try {
            Map<String,String> score;
            score = ExcelUtils.readOtherEvalScore(xlsRecordFile.getInputStream(),xlsRecordFile.getOriginalFilename());
            Response response = Response.ok();
            List<Map<String,String>> successList = new ArrayList<>();
            List<Map<String,String>> errorList = new ArrayList<>();
            for(String name : score.keySet()){
                Map<String,String> itemResult = new HashMap<>(3);
                itemResult.put("name",name);
                itemResult.put("score",score.get(name));
                try{
                    if(!Pattern.matches("^(100|([1-9]?\\d))([.]\\d*)?$",score.get(name))){
                        throw new TeException("成绩只能为浮点数或者正整数");
                    }
                    Double s = Double.valueOf(score.get(name));
                    boolean result = otherEvalRecordService.add(taskId,subTaskId,name,s);
                    if(result){
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
        } catch (Exception e) {
            //捕获所有的异常，使用bootstrap-fileinput需要返回error字段
            String error = e.getMessage();
            return Response.error().put("error",error);
        }

    }

}
