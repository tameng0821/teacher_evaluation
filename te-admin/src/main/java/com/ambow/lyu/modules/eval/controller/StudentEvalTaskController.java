package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.service.StudentEvalTaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * 学生评价子任务
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-13 16:25:38
 */
@RestController
@RequestMapping("eval/studentevaltask")
public class StudentEvalTaskController {
    @Autowired
    private StudentEvalTaskService studentEvalTaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("eval:studentevaltask:eval")
    public Response list(@RequestParam Map<String, Object> params) {
        PageUtils page = studentEvalTaskService.queryPage(params);

        return Response.ok().put("page", page);
    }

}
