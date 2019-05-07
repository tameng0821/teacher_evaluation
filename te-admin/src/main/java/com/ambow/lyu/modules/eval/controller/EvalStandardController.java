package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.EvalStandardEntity;
import com.ambow.lyu.modules.eval.service.EvalStandardService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 评分标准预置数据，只做比例的修改
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-07 10:47:29
 */
@RestController
@RequestMapping("eval/evalstandard")
public class EvalStandardController {
    @Autowired
    private EvalStandardService evalStandardService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("eval:evalstandard:list")
    public Response list(@RequestParam Map<String, Object> params) {
        PageUtils page = evalStandardService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:evalstandard:info")
    public Response info(@PathVariable("id") Long id) {
        EvalStandardEntity evalStandard = evalStandardService.getById(id);

        return Response.ok().put("evalStandard", evalStandard);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:evalstandard:save")
    public Response save(@RequestBody EvalStandardEntity evalStandard) {
        evalStandardService.save(evalStandard);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:evalstandard:update")
    public Response update(@RequestBody EvalStandardEntity evalStandard) {
        ValidatorUtils.validateEntity(evalStandard);
        evalStandardService.updateById(evalStandard);

        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:evalstandard:delete")
    public Response delete(@RequestBody Long[] ids) {
        evalStandardService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

}
