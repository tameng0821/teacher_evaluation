package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.InspectorEvalTaskItemEntity;
import com.ambow.lyu.modules.eval.service.InspectorEvalTaskItemService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 督导评价子任务评价项目
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-13 16:25:38
 */
@RestController
@RequestMapping("eval/inspectorevaltaskitem")
public class InspectorEvalTaskItemController {
    @Autowired
    private InspectorEvalTaskItemService inspectorEvalTaskItemService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("eval:inspectorevaltaskitem:list")
    public Response list(@RequestParam Map<String, Object> params) {
        PageUtils page = inspectorEvalTaskItemService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:inspectorevaltaskitem:info")
    public Response info(@PathVariable("id") Long id) {
        InspectorEvalTaskItemEntity inspectorEvalTaskItem = inspectorEvalTaskItemService.getById(id);

        return Response.ok().put("inspectorEvalTaskItem", inspectorEvalTaskItem);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:inspectorevaltaskitem:save")
    public Response save(@RequestBody InspectorEvalTaskItemEntity inspectorEvalTaskItem) {
        inspectorEvalTaskItemService.save(inspectorEvalTaskItem);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:inspectorevaltaskitem:update")
    public Response update(@RequestBody InspectorEvalTaskItemEntity inspectorEvalTaskItem) {
        ValidatorUtils.validateEntity(inspectorEvalTaskItem);
        inspectorEvalTaskItemService.updateById(inspectorEvalTaskItem);

        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:inspectorevaltaskitem:delete")
    public Response delete(@RequestBody Long[] ids) {
        inspectorEvalTaskItemService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

}