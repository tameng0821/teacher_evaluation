package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.dto.EvalResultSummary;
import com.ambow.lyu.common.utils.ExcelUtils;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.EvalResultEntity;
import com.ambow.lyu.modules.eval.entity.EvalTaskEntity;
import com.ambow.lyu.modules.eval.service.EvalResultService;
import com.ambow.lyu.modules.eval.service.EvalTaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
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
     * 明细
     */
    @RequestMapping("/detail/list/{taskId}")
    @RequiresPermissions("eval:evalresult:list")
    public Response detailList(@PathVariable("taskId") Long taskId,@RequestParam Map<String, Object> params){
        params.put("task_id",taskId);
        PageUtils page = evalResultService.queryPage(params);

        return Response.ok().put("page", page);
    }

    /**
     * 导出明细
     */
    @RequestMapping("/detail/export/{taskId}")
    @RequiresPermissions("eval:evalresult:list")
    public ResponseEntity<byte[]> exportDetailList(@PathVariable("taskId") Long taskId, @RequestParam Map<String, Object> params) throws IOException {
        params.put("task_id",taskId);
        List<EvalResultEntity> list = evalResultService.queryList(params);

        EvalTaskEntity evalTaskEntity = evalTaskService.getById(taskId);

        return ExcelUtils.createEvalResultExcel(evalTaskEntity.getName()+"评价结果明细筛选导出.xls",list);
    }

    /**
     * 汇总
     */
    @RequestMapping("/detail/summary/{taskId}")
    @RequiresPermissions("eval:evalresult:info")
    public Response detailSummary(@PathVariable("taskId") Long taskId){
        EvalResultSummary summary = evalResultService.querySummary(taskId);

        return Response.ok().put("summary", summary);
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
