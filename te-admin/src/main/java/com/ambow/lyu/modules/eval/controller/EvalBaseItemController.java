package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.validator.group.AddGroup;
import com.ambow.lyu.common.validator.group.UpdateGroup;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.EvalBaseItemEntity;
import com.ambow.lyu.modules.eval.service.EvalBaseItemService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 评分标准基础项目
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-10 16:32:25
 */
@RestController
@RequestMapping("eval/evalbaseitem")
public class EvalBaseItemController {
    @Autowired
    private EvalBaseItemService evalBaseItemService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("eval:evalbaseitem:list")
    public Response list(@RequestParam Map<String, Object> params) {
        PageUtils page = evalBaseItemService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:evalbaseitem:info")
    public Response info(@PathVariable("id") Long id) {
        EvalBaseItemEntity evalBaseItem = evalBaseItemService.getById(id);

        return Response.ok().put("evalBaseItem", evalBaseItem);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:evalbaseitem:save")
    public Response save(@RequestBody EvalBaseItemEntity evalBaseItem) {

        ValidatorUtils.validateEntity(evalBaseItem, AddGroup.class);

        evalBaseItemService.save(evalBaseItem);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:evalbaseitem:update")
    public Response update(@RequestBody EvalBaseItemEntity evalBaseItem) {

        ValidatorUtils.validateEntity(evalBaseItem, UpdateGroup.class);

        evalBaseItemService.updateById(evalBaseItem);

        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:evalbaseitem:delete")
    public Response delete(@RequestBody Long[] ids) {
        evalBaseItemService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

}
