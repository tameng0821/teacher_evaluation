package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.EvalResultEntity;
import com.ambow.lyu.modules.eval.service.EvalResultService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 评价结果
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-07 16:49:54
 */
@RestController
@RequestMapping("eval/evalresult")
public class EvalResultController {
    @Autowired
    private EvalResultService evalResultService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("eval:evalresult:list")
    public Response list(@RequestParam Map<String, Object> params) {
        PageUtils page = evalResultService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:evalresult:info")
    public Response info(@PathVariable("id") Long id) {
        EvalResultEntity evalResult = evalResultService.getById(id);

        return Response.ok().put("evalResult", evalResult);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:evalresult:save")
    public Response save(@RequestBody EvalResultEntity evalResult) {
        evalResultService.save(evalResult);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:evalresult:update")
    public Response update(@RequestBody EvalResultEntity evalResult) {
        ValidatorUtils.validateEntity(evalResult);
        evalResultService.updateById(evalResult);

        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:evalresult:delete")
    public Response delete(@RequestBody Long[] ids) {
        evalResultService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

}
