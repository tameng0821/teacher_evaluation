package com.ambow.lyu.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作excel文件工具类
 *
 * @author woondeewyy
 * @date 2019/5/28
 */
public class ExcelUtils {

    /**
     * excel 文件后缀名
     */
    public static final String OFFICE_EXCEL_XLS = "xls";
    public static final String OFFICE_EXCEL_XLSX = "xlsx";

    public static void main(String [] args) throws IOException {
        File file = new File("C:\\Users\\w\\Documents\\项目文档\\教师评价系统_项目文档目录\\01产品需求文档\\学生评价导入样例表（教务处导出）.xls");
        Map<String,Double> resultScore = readStudentEvalScore(file);
        int i = 10;
        int j = 10;
        int n = 0;
        for(String name : resultScore.keySet()){

            String pinyin = HanyuPinyinUtils.toHanyuPinyin(name);
            System.out.println("INSERT INTO `sys_user` (`user_id`, `username`, `password`, `salt`,`name`,`email`, `mobile`, `status`, `dept_id`, `create_time`) VALUES ("+i+"," +
                    " '"+pinyin+"', 'f854a071e5d3747cbfb8495c0666c75636fc53c57428c6e3df7d5ffb3904ea77', 'LBmXDCbL20S0aGwCAuJa'," +
                    " '"+name+"', NULL, NULL, 1, "+(n%5 + 4)+", '2019-05-23 10:21:28');");
            System.out.println("INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`) VALUES ("+j+", "+i+", 1);");
            i++;
            j++;
            n++;
        }
    }

    /**
     * 读取学生评价分数
     *
     * @param file 学生评价导入样例表（教务处导出）.xls
     * @return Map<教师, 分数>
     */
    public static Map<String, Double> readStudentEvalScore(File file) throws IOException {
        List<Map<Integer, String>> result = readExcel(file, 0);

        Map<String, Double> score = new HashMap<>();
        boolean isStart = false;
        int nameIndex = 1;
        int scoreIndex = 6;
        for (Map<Integer, String> row : result) {
            if (isStart) {
                score.put(row.get(nameIndex), Double.valueOf(row.get(scoreIndex)));
            }

            if (row.values().contains("教师姓名") && row.values().contains("平均分")) {
                for (Integer index : row.keySet()) {
                    if ("教师姓名".equals(row.get(index))) {
                        nameIndex = index;
                    } else if ("平均分".equals(row.get(index))) {
                        scoreIndex = index;
                    }
                }
                isStart = true;
            }
        }
        return score;
    }

    /**
     * 读取指定Sheet也的内容
     *
     * @param file    file excel文件
     * @param sheetNo sheet序号,从0开始
     */
    public static List<Map<Integer, String>> readExcel(File file, Integer sheetNo) throws IOException {

        if (file == null || sheetNo == null) {
            throw new IllegalArgumentException("文件不能为空");
        }

        List<Map<Integer, String>> result = new ArrayList<>();
        Workbook workbook = getWorkbook(file);
        if (workbook != null) {
            Sheet sheet = workbook.getSheetAt(sheetNo);
            if (sheet != null) {
                // 得到excel的总记录条数
                int rowNos = sheet.getLastRowNum();
                for (int i = 0; i <= rowNos; i++) {
                    Row row = sheet.getRow(i);
                    if (row != null) {

                        Map<Integer, String> rowResult = new HashMap<>();

                        // 表头总共的列数
                        int columnNos = row.getLastCellNum();
                        for (int j = 0; j < columnNos; j++) {
                            Cell cell = row.getCell(j);
                            if (cell != null) {
                                rowResult.put(j, cell.getStringCellValue());
                            }
                        }

                        result.add(rowResult);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 根据文件获取Workbook对象
     *
     * @param file 文件
     */
    public static Workbook getWorkbook(File file)
            throws EncryptedDocumentException, IOException {
        InputStream is = null;
        Workbook wb = null;
        if (file == null) {
            throw new IllegalArgumentException("文件不能为空");
        }
        String suffix = getSuffix(file.getName());
        if (StringUtils.isBlank(suffix)) {
            throw new IllegalArgumentException("文件后缀不能为空");
        }
        if (OFFICE_EXCEL_XLS.equals(suffix) || OFFICE_EXCEL_XLSX.equals(suffix)) {
            try {
                is = new FileInputStream(file);
                wb = WorkbookFactory.create(is);
            } finally {
                if (is != null) {
                    is.close();
                }
                if (wb != null) {
                    wb.close();
                }
            }
        } else {
            throw new IllegalArgumentException("该文件非Excel文件");
        }
        return wb;
    }

    /**
     * 获取后缀
     *
     * @param filepath filepath 文件全路径
     */
    private static String getSuffix(String filepath) {
        if (StringUtils.isBlank(filepath)) {
            return "";
        }
        int index = filepath.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return filepath.substring(index + 1);
    }
}
