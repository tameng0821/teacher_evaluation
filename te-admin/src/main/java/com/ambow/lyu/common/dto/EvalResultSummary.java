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
     * 总分最高分
     */
    private double maxTotal;
    /**
     * 学生评价最高分
     */
    private double maxStudent;
    /**
     * 同行评价最高分
     */
    private double maxColleague;
    /**
     * 督导评价最高分
     */
    private double maxInspector;
    /**
     * 其他评价最高分
     */
    private double maxOther;

    /**
     * 总分最低分
     */
    private double minTotal;
    /**
     * 学生评价最低分
     */
    private double minStudent;
    /**
     * 同行评价最低分
     */
    private double minColleague;
    /**
     * 督导评价最低分
     */
    private double minInspector;
    /**
     * 其他评价最低分
     */
    private double minOther;

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

    private List<String> personList;
    private List<Double> personStudentList;
    private List<Double> personColleagueList;
    private List<Double> personInspectorList;
    private List<Double> personOtherList;
    private List<Double> personTotalList;

    /**
     * 每个部门详情
     */
    private List<EvalResultSummaryDeptDetail> deptDetails;
}
