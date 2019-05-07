package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.InspectorEvalRecordEntity;
import com.ambow.lyu.modules.eval.service.InspectorEvalRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 督导评价记录
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-07 10:47:29
 */
@RestController
@RequestMapping("eval/inspectorevalrecord")
public class InspectorEvalRecordController {
    @Autowired
    private InspectorEvalRecordService inspectorEvalRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("eval:inspectorevalrecord:list")
    public Response list(@RequestParam Map<String, Object> params) {
        PageUtils page = inspectorEvalRecordService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:inspectorevalrecord:info")
    public Response info(@PathVariable("id") Long id) {
        InspectorEvalRecordEntity inspectorEvalRecord = inspectorEvalRecordService.getById(id);

        return Response.ok().put("inspectorEvalRecord", inspectorEvalRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:inspectorevalrecord:save")
    public Response save(@RequestBody InspectorEvalRecordEntity inspectorEvalRecord) {
        inspectorEvalRecordService.save(inspectorEvalRecord);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:inspectorevalrecord:update")
    public Response update(@RequestBody InspectorEvalRecordEntity inspectorEvalRecord) {
        ValidatorUtils.validateEntity(inspectorEvalRecord);
        inspectorEvalRecordService.updateById(inspectorEvalRecord);

        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:inspectorevalrecord:delete")
    public Response delete(@RequestBody Long[] ids) {
        inspectorEvalRecordService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

}
