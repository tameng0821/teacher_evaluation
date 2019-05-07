package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalRecordEntity;
import com.ambow.lyu.modules.eval.service.ColleagueEvalRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 同行评价记录
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-07 10:47:29
 */
@RestController
@RequestMapping("eval/colleagueevalrecord")
public class ColleagueEvalRecordController {
    @Autowired
    private ColleagueEvalRecordService colleagueEvalRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("eval:colleagueevalrecord:list")
    public Response list(@RequestParam Map<String, Object> params) {
        PageUtils page = colleagueEvalRecordService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:colleagueevalrecord:info")
    public Response info(@PathVariable("id") Long id) {
        ColleagueEvalRecordEntity colleagueEvalRecord = colleagueEvalRecordService.getById(id);

        return Response.ok().put("colleagueEvalRecord", colleagueEvalRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:colleagueevalrecord:save")
    public Response save(@RequestBody ColleagueEvalRecordEntity colleagueEvalRecord) {
        colleagueEvalRecordService.save(colleagueEvalRecord);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:colleagueevalrecord:update")
    public Response update(@RequestBody ColleagueEvalRecordEntity colleagueEvalRecord) {
        ValidatorUtils.validateEntity(colleagueEvalRecord);
        colleagueEvalRecordService.updateById(colleagueEvalRecord);

        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:colleagueevalrecord:delete")
    public Response delete(@RequestBody Long[] ids) {
        colleagueEvalRecordService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

}
