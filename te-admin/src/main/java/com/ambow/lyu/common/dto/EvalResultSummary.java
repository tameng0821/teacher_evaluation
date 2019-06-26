package com.ambow.lyu.common.dto;

import lombok.Data;

import java.util.List;

/**
 * 评价汇总统计
 * @author woondeewyy
 * @date 2019/6/26
 */
@Data
public class EvalResultSummary {

    /**
     * 总分平均分
     */
    private double averageTotal;
    /**
     * 学生评价平均分
     */
    private double averageStudent;
    /**
     * 同行评价平均分
     */
    private double averageColleague;
    /**
     * 督导评价平均分
     */
    private double averageInspector;
    /**
     * 其他评价平均分
     */
    private double averageOther;

    /**
     * 优秀的数量
     */
    private long goodCount;
    /**
     * 良好的数量
     */
    private long fineCount;
    /**
     * 合格的数量
     */
    private long passCount;
    /**
     * 不合格的数量
     */
    private long failCount;

    /**
     * 每个部门平均分详情
     */
    private List<String> deptList;
    private List<Double> deptAverageStudentList;
    private List<Double> deptAverageColleagueList;
    private List<Double> deptAverageInspectorList;
    private List<Double> deptAverageOtherList;
    private List<Double> deptAverageTotalList;

    /**
     * 每个人平均分详情,数据太多，图表显示太杂乱
     */
//    private List<String> personList;
//    private List<Double> personStudentList;
//    private List<Double> personColleagueList;
//    private List<Double> personInspectorList;
//    private List<Double> personOtherList;
//    private List<Double> personTotalList;
}
