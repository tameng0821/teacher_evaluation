package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.StudentEvalTaskEntity;
import com.ambow.lyu.modules.eval.service.StudentEvalTaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 学生评价子任务
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-13 16:25:38
 */
@RestController
@RequestMapping("eval/studentevaltask")
public class StudentEvalTaskController {
    @Autowired
    private StudentEvalTaskService studentEvalTaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("eval:studentevaltask:list")
    public Response list(@RequestParam Map<String, Object> params) {
        PageUtils page = studentEvalTaskService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:studentevaltask:info")
    public Response info(@PathVariable("id") Long id) {
        StudentEvalTaskEntity studentEvalTask = studentEvalTaskService.getById(id);

        return Response.ok().put("studentEvalTask", studentEvalTask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:studentevaltask:save")
    public Response save(@RequestBody StudentEvalTaskEntity studentEvalTask) {
        studentEvalTaskService.save(studentEvalTask);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:studentevaltask:update")
    public Response update(@RequestBody StudentEvalTaskEntity studentEvalTask) {
        ValidatorUtils.validateEntity(studentEvalTask);
        studentEvalTaskService.updateById(studentEvalTask);

        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:studentevaltask:delete")
    public Response delete(@RequestBody Long[] ids) {
        studentEvalTaskService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

}
