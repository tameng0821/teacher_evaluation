package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.dto.StuOrOtherEvalScoreDto;
import com.ambow.lyu.common.exception.TeException;
import com.ambow.lyu.common.utils.Constant;
import com.ambow.lyu.common.utils.ExcelUtils;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.entity.StudentEvalRecordEntity;
import com.ambow.lyu.modules.eval.service.StudentEvalRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.regex.Pattern;


/**
 * 学生评价记录
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-24 15:43:24
 */
@RestController
@RequestMapping("eval/studentevalrecord")
public class StudentEvalRecordController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentEvalRecordController.class);

    @Autowired
    private StudentEvalRecordService studentEvalRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list/{subTaskId}")
    @RequiresPermissions("eval:studentevaltask:eval")
    public Response list(@PathVariable("subTaskId") Long subTaskId, @RequestParam Map<String, Object> params) {

        params.put(Constant.SUB_TASK_ID, subTaskId);

        PageUtils page = studentEvalRecordService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:studentevaltask:eval")
    public Response info(@PathVariable("id") Long id) {
        StudentEvalRecordEntity studentEvalRecord = studentEvalRecordService.findById(id);

        return Response.ok().put("studentEvalRecord", studentEvalRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:studentevaltask:eval")
    public Response save(@RequestBody StudentEvalRecordEntity studentEvalRecord) {
        //数据校验
        ValidatorUtils.validateEntity(studentEvalRecord);
        //数据是否存在校验
        int sum = studentEvalRecordService.count( new QueryWrapper<StudentEvalRecordEntity>()
                .eq("sub_task_id",studentEvalRecord.getSubTaskId()).eq("user_id",studentEvalRecord.getUserId()));
        if(sum != 0){
            throw new TeException("该教师已添加学生评价记录！！！");
        }

        studentEvalRecord.setUpdateTime(new Date());

        studentEvalRecordService.save(studentEvalRecord);

        return Response.ok();
    }

    /**
     * 文件上传导入
     */
    @RequestMapping(value = "/import/{taskId}/{subTaskId}",method=RequestMethod.POST
            ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("eval:studentevaltask:eval")
    public Response fileImport(@PathVariable("taskId") Long taskId,@PathVariable("subTaskId") Long subTaskId,@RequestParam("xlsRecordFile") MultipartFile xlsRecordFile) {
        try {
            List<StuOrOtherEvalScoreDto> xlsResult = ExcelUtils.readStudentOrOtherEvalScore(xlsRecordFile.getInputStream(),xlsRecordFile.getOriginalFilename());
            Response response = Response.ok();
            List<StuOrOtherEvalScoreDto> successList = new ArrayList<>();
            List<StuOrOtherEvalScoreDto> errorList = new ArrayList<>();
            for(StuOrOtherEvalScoreDto item : xlsResult){
                if(StringUtils.isBlank(item.getUsername())){
                    item.setReason("教师工号不能为空");
                    errorList.add(item);
                }else if(StringUtils.isBlank(item.getName())){
                    item.setReason("教师姓名不能为空");
                    errorList.add(item);
                }else if(StringUtils.isBlank(item.getScore())){
                    item.setReason("得分不能为空");
                    errorList.add(item);
                }else if(!Pattern.matches("^(100|([1-9]?\\d))([.]\\d*)?$",item.getScore())){
                    item.setReason("学生评价分数只能为浮点数或者正整数");
                    errorList.add(item);
                }else {
                    try{
                        Double score = Double.valueOf(item.getScore());
                        boolean result = studentEvalRecordService.add(taskId,subTaskId,item.getUsername(),item.getName(),score);
                        if(result){
                            successList.add(item);
                        }else {
                            item.setReason("添加失败，系统异常，请联系管理员");
                            errorList.add(item);
                        }
                    }catch (NumberFormatException e){
                        item.setReason("成绩只能为浮点数或者正整数");
                        errorList.add(item);
                    }catch (Exception ex){
                        item.setReason(ex.getMessage());
                        errorList.add(item);
                    }
                }

            }
            response.put("successList",successList);
            response.put("errorList",errorList);
            return response;
        } catch (Exception e) {
            //捕获所有的异常，使用bootstrap-fileinput需要返回error字段
            String error = e.getMessage();
            return Response.error().put("error",error);
        }

    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:studentevaltask:eval")
    public Response update(@RequestBody StudentEvalRecordEntity studentEvalRecord) {
        ValidatorUtils.validateEntity(studentEvalRecord);

        studentEvalRecord.setUpdateTime(new Date());

        studentEvalRecordService.updateById(studentEvalRecord);

        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:studentevaltask:eval")
    public Response delete(@RequestBody Long[] ids) {
        studentEvalRecordService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

}
