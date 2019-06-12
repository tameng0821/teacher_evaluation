package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalTaskItemEntity;
import com.ambow.lyu.modules.eval.service.ColleagueEvalTaskItemService;
import com.ambow.lyu.modules.eval.service.ColleagueEvalTaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 同行/系主任评价子任务
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-13 16:25:38
 */
@RestController
@RequestMapping("eval/colleagueevaltask")
public class ColleagueEvalTaskController {
    @Autowired
    private ColleagueEvalTaskService colleagueEvalTaskService;
    @Autowired
    private ColleagueEvalTaskItemService colleagueEvalTaskItemService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("eval:colleagueevaltask:eval")
    public Response list(@RequestParam Map<String, Object> params) {

        PageUtils page = colleagueEvalTaskService.queryPage(params);

        return Response.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/items/{taskId}")
    @RequiresPermissions("eval:colleagueevaltask:eval")
    public Response items(@PathVariable("taskId") Long taskId){
        List<ColleagueEvalTaskItemEntity> items = colleagueEvalTaskItemService.selectByTaskId(taskId);
        return Response.ok().put("items", items);
    }

}
