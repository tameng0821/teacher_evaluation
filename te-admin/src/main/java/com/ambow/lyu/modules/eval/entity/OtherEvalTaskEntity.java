package com.ambow.lyu.modules.eval.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 其他评价子任务
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-13 16:25:38
 */
@Data
@TableName("tb_other_eval_task")
public class OtherEvalTaskEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 任务ID
     */
    private Long taskId;
    /**
     * 评价占比
     */
    private Integer percentage;

    /**
     * 任务所属部门ID
     */
    @TableField(exist = false)
    private Long deptId;
    /**
     * 部门名称
     */
    @TableField(exist = false)
    private String deptName;
    /**
     * 评价任务名称
     */
    @TableField(exist = false)
    private String taskName;
    /**
     * 评价任务创建时间
     */
    @TableField(exist = false)
    private String taskCreateTime;

    /**
     * 完成进度，完成人数，最后会和部门总人数做比较
     */
    @TableField(exist = false)
    private Integer schedule;

}
