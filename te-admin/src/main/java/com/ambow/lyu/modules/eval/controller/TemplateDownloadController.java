package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.exception.TeException;
import com.ambow.lyu.common.utils.ExcelUtils;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalTaskItemEntity;
import com.ambow.lyu.modules.eval.entity.InspectorEvalTaskItemEntity;
import com.ambow.lyu.modules.eval.service.ColleagueEvalTaskItemService;
import com.ambow.lyu.modules.eval.service.InspectorEvalTaskItemService;
import com.ambow.lyu.modules.sys.entity.SysUserEntity;
import com.ambow.lyu.modules.sys.service.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author woondeewyy
 * @date 2019/6/11
 */
@RestController
@RequestMapping("template/")
public class TemplateDownloadController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ColleagueEvalTaskItemService colleagueEvalTaskItemService;
    @Autowired
    private InspectorEvalTaskItemService inspectorEvalTaskItemService;

    @RequestMapping("/student/{deptId}")
    @RequiresPermissions("eval:studentevaltask:eval")
    public ResponseEntity<byte[]> downloadStudentImportTemplate(@PathVariable("deptId") Long deptId) throws IOException {

        String fileName = "学生评价分数导入模板.xls";

        String sheetName = "学生评价";

        List<String> titles = new ArrayList<>();
        titles.add(ExcelUtils.EXCEL_TEM_TITLE_0);
        titles.add(ExcelUtils.EXCEL_TEM_TITLE_1);
        titles.add("学生评价得分[0-100分]");

        List<SysUserEntity> userList = sysUserService.queryByDept(deptId);

        return ExcelUtils.createTemplate(fileName,sheetName,titles,userList);
    }

    @RequestMapping("/colleague/{taskId}/{deptId}")
    @RequiresPermissions("eval:colleagueevaltask:eval")
    public ResponseEntity<byte[]> downloadColleagueImportTemplate(@PathVariable("taskId") Long taskId,@PathVariable("deptId") Long deptId) throws IOException {

        List<ColleagueEvalTaskItemEntity> itemEntities = colleagueEvalTaskItemService.selectByTaskId(taskId);
        if(itemEntities.size() == 0){
            throw new TeException("未能找到同行评价项目，请检查数据是否完整！");
        }

        String fileName = "同行评价分数导入模板.xls";

        String sheetName = "同行评价";

        List<String> titles = new ArrayList<>();
        titles.add(ExcelUtils.EXCEL_TEM_TITLE_0);
        titles.add(ExcelUtils.EXCEL_TEM_TITLE_1);
        for (ColleagueEvalTaskItemEntity itemEntity : itemEntities) {
            String value = itemEntity.getName() + "(占比:" + itemEntity.getPercentage() + "%)[0-100分]";
            titles.add(value);
        }

        List<SysUserEntity> userList = sysUserService.queryByDept(deptId);

        return ExcelUtils.createTemplate(fileName,sheetName,titles,userList);
    }

    @RequestMapping("/inspector/{taskId}/{deptId}")
    @RequiresPermissions("eval:inspectorevaltask:eval")
    public ResponseEntity<byte[]> downloadInspectorImportTemplate(@PathVariable("taskId") Long taskId,@PathVariable("deptId") Long deptId) throws IOException {

        List<InspectorEvalTaskItemEntity> itemEntities = inspectorEvalTaskItemService.selectByTaskId(taskId);
        if(itemEntities.size() == 0){
            throw new TeException("未能找到督导评价项目，请检查数据是否完整！");
        }

        String fileName = "督导评价分数导入模板.xls";

        String sheetName = "督导评价";

        List<String> titles = new ArrayList<>();
        titles.add(ExcelUtils.EXCEL_TEM_TITLE_0);
        titles.add(ExcelUtils.EXCEL_TEM_TITLE_1);
        for (InspectorEvalTaskItemEntity itemEntity : itemEntities) {
            String value = itemEntity.getName() + "(占比:" + itemEntity.getPercentage() + "%)[0-100分]";
            titles.add(value);
        }

        List<SysUserEntity> userList = sysUserService.queryByDept(deptId);


        return ExcelUtils.createTemplate(fileName,sheetName,titles,userList);
    }


    @RequestMapping("/other/{deptId}")
    @RequiresPermissions("eval:otherevaltask:eval")
    public ResponseEntity<byte[]> downloadOtherImportTemplate(@PathVariable("deptId") Long deptId) throws IOException {
        String fileName = "其他评价分数导入模板.xls";

        String sheetName = "其他评价";

        List<String> titles = new ArrayList<>();
        titles.add(ExcelUtils.EXCEL_TEM_TITLE_0);
        titles.add(ExcelUtils.EXCEL_TEM_TITLE_1);
        titles.add("其他评价得分[0-100分]");

        List<SysUserEntity> userList = sysUserService.queryByDept(deptId);

        return ExcelUtils.createTemplate(fileName,sheetName,titles,userList);
    }
}
