package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalTaskEntity;
import com.ambow.lyu.modules.eval.service.ColleagueEvalTaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 同行评价任务
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-07 10:47:29
 */
@RestController
@RequestMapping("eval/colleagueevaltask")
public class ColleagueEvalTaskController {
    @Autowired
    private ColleagueEvalTaskService colleagueEvalTaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("eval:colleagueevaltask:list")
    public Response list(@RequestParam Map<String, Object> params) {
        PageUtils page = colleagueEvalTaskService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:colleagueevaltask:info")
    public Response info(@PathVariable("id") Long id) {
        ColleagueEvalTaskEntity colleagueEvalTask = colleagueEvalTaskService.getById(id);

        return Response.ok().put("colleagueEvalTask", colleagueEvalTask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:colleagueevaltask:save")
    public Response save(@RequestBody ColleagueEvalTaskEntity colleagueEvalTask) {
        colleagueEvalTaskService.save(colleagueEvalTask);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:colleagueevaltask:update")
    public Response update(@RequestBody ColleagueEvalTaskEntity colleagueEvalTask) {
        ValidatorUtils.validateEntity(colleagueEvalTask);
        colleagueEvalTaskService.updateById(colleagueEvalTask);

        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:colleagueevaltask:delete")
    public Response delete(@RequestBody Long[] ids) {
        colleagueEvalTaskService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

}
