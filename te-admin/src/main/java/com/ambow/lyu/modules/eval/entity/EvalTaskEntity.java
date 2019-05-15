package com.ambow.lyu.modules.eval.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评价任务
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-13 16:25:38
 */
@Data
@TableName("tb_eval_task")
public class EvalTaskEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 状态，0：开启；1：关闭；
     */
    private Integer status;
    /**
     * 评价部门ID
     */
    private Long deptId;
    /**
     * 备注
     */
    private String remark;

    /**
     * 部门名称
     */
    @TableField(exist = false)
    private String deptName;

    /**
     * 学生评价占比
     */
    @TableField(exist = false)
    private Integer studentPercentage;
    /**
     * 同行评价占比
     */
    @TableField(exist = false)
    private Integer colleaguePercentage;
    /**
     * 督导评价占比
     */
    @TableField(exist = false)
    private Integer inspectorPercentage;
    /**
     * 其他评价占比
     */
    @TableField(exist = false)
    private Integer otherPercentage;

    /**
     * 学生评价子任务
     */
    @TableField(exist = false)
    private StudentEvalTaskEntity studentEvalTask;
    /**
     * 同行评价子任务
     * 有几个子部门就有几个同行评价子任务
     */
    @TableField(exist = false)
    private List<ColleagueEvalTaskEntity> colleagueEvalTasks;
    /**
     * 同行评价子任务项目
     * 定义同行评价子任务具体评价项目
     */
    @TableField(exist = false)
    private List<ColleagueEvalTaskItemEntity> colleagueEvalTaskItems;

    /**
     * 督导评价子任务
     * 有几个督导就有几个督导评价子任务
     */
    @TableField(exist = false)
    private List<InspectorEvalTaskEntity> inspectorEvalTasks;
    /**
     * 督导评价子任务项目
     * 定义督导评价子任务具体评价项目
     */
    @TableField(exist = false)
    private List<InspectorEvalTaskItemEntity> inspectorEvalTaskItems;
    /**
     * 其他评价子任务
     */
    @TableField(exist = false)
    private OtherEvalTaskEntity otherEvalTask;


}
