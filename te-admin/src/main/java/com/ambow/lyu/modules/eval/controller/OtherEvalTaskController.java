package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.OtherEvalTaskEntity;
import com.ambow.lyu.modules.eval.service.OtherEvalTaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 其他评价子任务
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-13 16:25:38
 */
@RestController
@RequestMapping("eval/otherevaltask")
public class OtherEvalTaskController {
    @Autowired
    private OtherEvalTaskService otherEvalTaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("eval:otherevaltask:list")
    public Response list(@RequestParam Map<String, Object> params) {
        PageUtils page = otherEvalTaskService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:otherevaltask:info")
    public Response info(@PathVariable("id") Long id) {
        OtherEvalTaskEntity otherEvalTask = otherEvalTaskService.getById(id);

        return Response.ok().put("otherEvalTask", otherEvalTask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:otherevaltask:save")
    public Response save(@RequestBody OtherEvalTaskEntity otherEvalTask) {
        otherEvalTaskService.save(otherEvalTask);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:otherevaltask:update")
    public Response update(@RequestBody OtherEvalTaskEntity otherEvalTask) {
        ValidatorUtils.validateEntity(otherEvalTask);
        otherEvalTaskService.updateById(otherEvalTask);

        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:otherevaltask:delete")
    public Response delete(@RequestBody Long[] ids) {
        otherEvalTaskService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

}
