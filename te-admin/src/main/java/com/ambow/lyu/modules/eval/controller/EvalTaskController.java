package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.EvalTaskEntity;
import com.ambow.lyu.modules.eval.service.EvalTaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 评价任务
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-07 10:47:29
 */
@RestController
@RequestMapping("eval/evaltask")
public class EvalTaskController {
    @Autowired
    private EvalTaskService evalTaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("eval:evaltask:list")
    public Response list(@RequestParam Map<String, Object> params) {
        PageUtils page = evalTaskService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:evaltask:info")
    public Response info(@PathVariable("id") Long id) {
        EvalTaskEntity evalTask = evalTaskService.getById(id);

        return Response.ok().put("evalTask", evalTask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:evaltask:save")
    public Response save(@RequestBody EvalTaskEntity evalTask) {
        evalTaskService.save(evalTask);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:evaltask:update")
    public Response update(@RequestBody EvalTaskEntity evalTask) {
        ValidatorUtils.validateEntity(evalTask);
        evalTaskService.updateById(evalTask);

        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:evaltask:delete")
    public Response delete(@RequestBody Long[] ids) {
        evalTaskService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

}
