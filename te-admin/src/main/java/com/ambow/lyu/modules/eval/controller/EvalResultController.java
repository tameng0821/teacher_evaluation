package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.EvalResultEntity;
import com.ambow.lyu.modules.eval.entity.EvalTaskEntity;
import com.ambow.lyu.modules.eval.service.EvalResultService;
import com.ambow.lyu.modules.eval.service.EvalTaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;



/**
 * 评价结果
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-06-24 09:18:25
 */
@RestController
@RequestMapping("eval/evalresult")
public class EvalResultController {
    @Autowired
    private EvalTaskService evalTaskService;
    @Autowired
    private EvalResultService evalResultService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("eval:evalresult:list")
    public Response list(@RequestParam Map<String, Object> params){
        params.put("task_status", EvalTaskEntity.Status.COMPLETE.value());
        PageUtils page = evalTaskService.queryPage(params);

        return Response.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/detail/list/{taskId}")
    @RequiresPermissions("eval:evalresult:list")
    public Response detailList(@PathVariable("taskId") Long taskId,@RequestParam Map<String, Object> params){
        params.put("task_id",taskId);
        PageUtils page = evalResultService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:evalresult:info")
    public Response info(@PathVariable("id") Long id){
        EvalResultEntity evalResult = evalResultService.getById(id);

        return Response.ok().put("evalResult", evalResult);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:evalresult:update")
    public Response update(@RequestBody EvalResultEntity evalResult){
        ValidatorUtils.validateEntity(evalResult);
        evalResultService.updateById(evalResult);
        
        return Response.ok();
    }

}
