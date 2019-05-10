package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.validator.group.AddGroup;
import com.ambow.lyu.common.validator.group.UpdateGroup;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.InspectorEvalBaseItemEntity;
import com.ambow.lyu.modules.eval.service.InspectorEvalBaseItemService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 督导评价基础项目
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-10 16:32:25
 */
@RestController
@RequestMapping("eval/inspectorevalbaseitem")
public class InspectorEvalBaseItemController {
    @Autowired
    private InspectorEvalBaseItemService inspectorEvalBaseItemService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("eval:inspectorevalbaseitem:list")
    public Response list(@RequestParam Map<String, Object> params) {
        PageUtils page = inspectorEvalBaseItemService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:inspectorevalbaseitem:info")
    public Response info(@PathVariable("id") Long id) {
        InspectorEvalBaseItemEntity inspectorEvalBaseItem = inspectorEvalBaseItemService.getById(id);

        return Response.ok().put("inspectorEvalBaseItem", inspectorEvalBaseItem);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:inspectorevalbaseitem:save")
    public Response save(@RequestBody InspectorEvalBaseItemEntity inspectorEvalBaseItem) {

        ValidatorUtils.validateEntity(inspectorEvalBaseItem, AddGroup.class);

        inspectorEvalBaseItemService.save(inspectorEvalBaseItem);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:inspectorevalbaseitem:update")
    public Response update(@RequestBody InspectorEvalBaseItemEntity inspectorEvalBaseItem) {
        ValidatorUtils.validateEntity(inspectorEvalBaseItem, UpdateGroup.class);
        inspectorEvalBaseItemService.updateById(inspectorEvalBaseItem);

        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:inspectorevalbaseitem:delete")
    public Response delete(@RequestBody Long[] ids) {
        inspectorEvalBaseItemService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

}
