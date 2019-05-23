package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.InspectorEvalTaskEntity;
import com.ambow.lyu.modules.eval.service.InspectorEvalTaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 督导评价子任务
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-13 16:25:38
 */
@RestController
@RequestMapping("eval/inspectorevaltask")
public class InspectorEvalTaskController {
    @Autowired
    private InspectorEvalTaskService inspectorEvalTaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("eval:inspectorevaltask:list")
    public Response list(@RequestParam Map<String, Object> params) {
        PageUtils page = inspectorEvalTaskService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:inspectorevaltask:info")
    public Response info(@PathVariable("id") Long id) {
        InspectorEvalTaskEntity inspectorEvalTask = inspectorEvalTaskService.getById(id);

        return Response.ok().put("inspectorEvalTask", inspectorEvalTask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:inspectorevaltask:save")
    public Response save(@RequestBody InspectorEvalTaskEntity inspectorEvalTask) {
        inspectorEvalTaskService.save(inspectorEvalTask);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:inspectorevaltask:update")
    public Response update(@RequestBody InspectorEvalTaskEntity inspectorEvalTask) {
        ValidatorUtils.validateEntity(inspectorEvalTask);
        inspectorEvalTaskService.updateById(inspectorEvalTask);

        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:inspectorevaltask:delete")
    public Response delete(@RequestBody Long[] ids) {
        inspectorEvalTaskService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

}
