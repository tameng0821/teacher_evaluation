package com.ambow.lyu.modules.eval.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author woondeewyy
 * @date 2019/5/23
 */
@Data
public class StudentEvalTaskVo implements Serializable {

    /**
     * 学生评价任务ID
     */
    private Long id;
    /**
     * 评价任务名称
     */
    private String name;
    /**
     * 评价任务创建时间
     */
    private Date createTime;
    /**
     * 评价占比
     */
    private Integer percentage;
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 评价任务部门名称
     */
    private String deptName;

}
