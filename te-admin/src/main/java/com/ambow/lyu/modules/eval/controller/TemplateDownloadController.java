package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.exception.TeException;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalTaskItemEntity;
import com.ambow.lyu.modules.eval.entity.InspectorEvalTaskItemEntity;
import com.ambow.lyu.modules.eval.service.ColleagueEvalTaskItemService;
import com.ambow.lyu.modules.eval.service.InspectorEvalTaskItemService;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.List;

/**
 * @author woondeewyy
 * @date 2019/6/11
 */
@RestController
@RequestMapping("template/")
public class TemplateDownloadController {

    @Autowired
    private ColleagueEvalTaskItemService colleagueEvalTaskItemService;
    @Autowired
    private InspectorEvalTaskItemService inspectorEvalTaskItemService;

    @RequestMapping("/student")
    @RequiresPermissions("eval:studentevaltask:eval")
    public ResponseEntity<byte[]> downloadStudentImportTemplate() throws IOException {
        Resource resource = new ClassPathResource("template/studentTemplate.xls");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "studentTemplate.xls");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<byte[]>(IOUtils.toByteArray(resource.getInputStream()), headers, HttpStatus.OK);
    }

    @RequestMapping("/colleague/{taskId}")
    @RequiresPermissions("eval:colleagueevaltask:eval")
    public ResponseEntity<byte[]> downloadColleagueImportTemplate(@PathVariable("taskId") Long taskId) throws IOException {

        List<ColleagueEvalTaskItemEntity> itemEntities = colleagueEvalTaskItemService.selectByTaskId(taskId);
        if(itemEntities.size() == 0){
            throw new TeException("未能找到同行评价项目，请检查数据是否完整！");
        }


        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("同行评价导入模板");

        //左右、上下居中
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        //标题
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("姓名");
        cell.setCellStyle(cellStyle);
        sheet.setColumnWidth(0,5*3*256);
        for(int i=0; i < itemEntities.size() ; ++i){
            String value = itemEntities.get(i).getName()+"（占比:"+itemEntities.get(i).getPercentage()+"%）";
            HSSFCell itemCell = row.createCell(i+1);
            itemCell.setCellValue(value);
            itemCell.setCellStyle(cellStyle);
            sheet.setColumnWidth(i+1,value.getBytes().length*256);
        }

        //演示数据
        HSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("李某某");
        for(int i=0; i < itemEntities.size() ; ++i){
            row1.createCell(i+1).setCellValue("100");
        }


        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "colleagueTemplate.xls");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        wb.write(outByteStream);
        return new ResponseEntity<byte[]>(outByteStream.toByteArray(), headers, HttpStatus.OK);
    }

    @RequestMapping("/inspector/{taskId}")
    @RequiresPermissions("eval:inspectorevaltask:eval")
    public ResponseEntity<byte[]> downloadInspectorImportTemplate(@PathVariable("taskId") Long taskId) throws IOException {

        List<InspectorEvalTaskItemEntity> itemEntities = inspectorEvalTaskItemService.selectByTaskId(taskId);
        if(itemEntities.size() == 0){
            throw new TeException("未能找到督导评价项目，请检查数据是否完整！");
        }


        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("督导评价导入模板");

        //左右、上下居中
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        //标题
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("姓名");
        cell.setCellStyle(cellStyle);
        sheet.setColumnWidth(0,5*3*256);
        for(int i=0; i < itemEntities.size() ; ++i){
            String value = itemEntities.get(i).getName()+"（占比:"+itemEntities.get(i).getPercentage()+"%）";
            HSSFCell itemCell = row.createCell(i+1);
            itemCell.setCellValue(value);
            itemCell.setCellStyle(cellStyle);
            sheet.setColumnWidth(i+1,value.getBytes().length*256);
        }

        //演示数据
        HSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("刘某某");
        for(int i=0; i < itemEntities.size() ; ++i){
            row1.createCell(i+1).setCellValue("100");
        }


        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "inspectorTemplate.xls");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        wb.write(outByteStream);
        return new ResponseEntity<byte[]>(outByteStream.toByteArray(), headers, HttpStatus.OK);
    }


    @RequestMapping("/other")
    @RequiresPermissions("eval:otherevaltask:eval")
    public ResponseEntity<byte[]> downloadOtherImportTemplate() throws IOException {
        Resource resource = new ClassPathResource("template/otherTemplate.xls");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "otherTemplate.xls");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<byte[]>(IOUtils.toByteArray(resource.getInputStream()), headers, HttpStatus.OK);
    }
}
