package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalTaskItemEntity;
import com.ambow.lyu.modules.eval.service.ColleagueEvalTaskItemService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 同行评价子任务评价项目
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-13 16:25:38
 */
@RestController
@RequestMapping("eval/colleagueevaltaskitem")
public class ColleagueEvalTaskItemController {
    @Autowired
    private ColleagueEvalTaskItemService colleagueEvalTaskItemService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("eval:colleagueevaltaskitem:list")
    public Response list(@RequestParam Map<String, Object> params) {
        PageUtils page = colleagueEvalTaskItemService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:colleagueevaltaskitem:info")
    public Response info(@PathVariable("id") Long id) {
        ColleagueEvalTaskItemEntity colleagueEvalTaskItem = colleagueEvalTaskItemService.getById(id);

        return Response.ok().put("colleagueEvalTaskItem", colleagueEvalTaskItem);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:colleagueevaltaskitem:save")
    public Response save(@RequestBody ColleagueEvalTaskItemEntity colleagueEvalTaskItem) {
        colleagueEvalTaskItemService.save(colleagueEvalTaskItem);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:colleagueevaltaskitem:update")
    public Response update(@RequestBody ColleagueEvalTaskItemEntity colleagueEvalTaskItem) {
        ValidatorUtils.validateEntity(colleagueEvalTaskItem);
        colleagueEvalTaskItemService.updateById(colleagueEvalTaskItem);

        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:colleagueevaltaskitem:delete")
    public Response delete(@RequestBody Long[] ids) {
        colleagueEvalTaskItemService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

}
