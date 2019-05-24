package com.ambow.lyu.modules.eval.controller;

import java.util.Arrays;
import java.util.Map;

import com.ambow.lyu.common.utils.Constant;
import com.ambow.lyu.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ambow.lyu.modules.eval.entity.StudentEvalRecordEntity;
import com.ambow.lyu.modules.eval.service.StudentEvalRecordService;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.vo.Response;



/**
 * 学生评价记录
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-24 15:43:24
 */
@RestController
@RequestMapping("eval/studentevalrecord")
public class StudentEvalRecordController {
    @Autowired
    private StudentEvalRecordService studentEvalRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list/{subTaskId}")
    @RequiresPermissions("eval:studentevaltask:eval")
    public Response list(@PathVariable("subTaskId") Long subTaskId,@RequestParam Map<String, Object> params){

        params.put(Constant.SUB_TASK_ID,subTaskId);

        PageUtils page = studentEvalRecordService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:studentevaltask:eval")
    public Response info(@PathVariable("id") Long id){
        StudentEvalRecordEntity studentEvalRecord = studentEvalRecordService.getById(id);

        return Response.ok().put("studentEvalRecord", studentEvalRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:studentevaltask:eval")
    public Response save(@RequestBody StudentEvalRecordEntity studentEvalRecord){
        studentEvalRecordService.save(studentEvalRecord);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:studentevaltask:eval")
    public Response update(@RequestBody StudentEvalRecordEntity studentEvalRecord){
        ValidatorUtils.validateEntity(studentEvalRecord);
        studentEvalRecordService.updateById(studentEvalRecord);
        
        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:studentevaltask:eval")
    public Response delete(@RequestBody Long[] ids){
        studentEvalRecordService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

}
