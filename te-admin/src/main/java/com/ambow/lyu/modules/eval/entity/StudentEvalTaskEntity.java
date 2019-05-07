package com.ambow.lyu.modules.eval.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生任务
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-07 10:47:28
 */
@Data
@TableName("tb_student_eval_task")
public class StudentEvalTaskEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 评价标准ID
     */
    private Long standardId;
    /**
     * 任务ID
     */
    private Long taskId;
    /**
     * 部门ID
     */
    private Long deptId;

}
