package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.validator.group.AddGroup;
import com.ambow.lyu.common.validator.group.UpdateGroup;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalBaseItemEntity;
import com.ambow.lyu.modules.eval.service.ColleagueEvalBaseItemService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 同行评价基础项目
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-10 16:32:24
 */
@RestController
@RequestMapping("eval/colleagueevalbaseitem")
public class ColleagueEvalBaseItemController {
    @Autowired
    private ColleagueEvalBaseItemService colleagueEvalBaseItemService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("eval:colleagueevalbaseitem:list")
    public Response list(@RequestParam Map<String, Object> params) {
        PageUtils page = colleagueEvalBaseItemService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:colleagueevalbaseitem:info")
    public Response info(@PathVariable("id") Long id) {
        ColleagueEvalBaseItemEntity colleagueEvalBaseItem = colleagueEvalBaseItemService.getById(id);

        return Response.ok().put("colleagueEvalBaseItem", colleagueEvalBaseItem);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:colleagueevalbaseitem:save")
    public Response save(@RequestBody ColleagueEvalBaseItemEntity colleagueEvalBaseItem) {

        ValidatorUtils.validateEntity(colleagueEvalBaseItem, AddGroup.class);

        colleagueEvalBaseItemService.save(colleagueEvalBaseItem);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:colleagueevalbaseitem:update")
    public Response update(@RequestBody ColleagueEvalBaseItemEntity colleagueEvalBaseItem) {

        ValidatorUtils.validateEntity(colleagueEvalBaseItem, UpdateGroup.class);

        colleagueEvalBaseItemService.updateById(colleagueEvalBaseItem);

        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:colleagueevalbaseitem:delete")
    public Response delete(@RequestBody Long[] ids) {
        colleagueEvalBaseItemService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

}
