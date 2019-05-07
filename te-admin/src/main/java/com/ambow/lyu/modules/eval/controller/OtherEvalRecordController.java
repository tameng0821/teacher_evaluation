package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.OtherEvalRecordEntity;
import com.ambow.lyu.modules.eval.service.OtherEvalRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 其他评价记录
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-07 10:47:28
 */
@RestController
@RequestMapping("eval/otherevalrecord")
public class OtherEvalRecordController {
    @Autowired
    private OtherEvalRecordService otherEvalRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("eval:otherevalrecord:list")
    public Response list(@RequestParam Map<String, Object> params) {
        PageUtils page = otherEvalRecordService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:otherevalrecord:info")
    public Response info(@PathVariable("id") Long id) {
        OtherEvalRecordEntity otherEvalRecord = otherEvalRecordService.getById(id);

        return Response.ok().put("otherEvalRecord", otherEvalRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:otherevalrecord:save")
    public Response save(@RequestBody OtherEvalRecordEntity otherEvalRecord) {
        otherEvalRecordService.save(otherEvalRecord);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:otherevalrecord:update")
    public Response update(@RequestBody OtherEvalRecordEntity otherEvalRecord) {
        ValidatorUtils.validateEntity(otherEvalRecord);
        otherEvalRecordService.updateById(otherEvalRecord);

        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:otherevalrecord:delete")
    public Response delete(@RequestBody Long[] ids) {
        otherEvalRecordService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

}
