package com.ambow.lyu.common.dto;

import lombok.Data;

import java.util.List;

/**
 * @author woondeewyy
 * @date 2019/6/27
 */
@Data
public class EvalResultSummaryDeptDetail {

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 每个人平均分详情,数据太多，图表显示太杂乱
     */
    private List<String> personList;
    private List<Double> personStudentList;
    private List<Double> personColleagueList;
    private List<Double> personInspectorList;
    private List<Double> personOtherList;
    private List<Double> personTotalList;

}
