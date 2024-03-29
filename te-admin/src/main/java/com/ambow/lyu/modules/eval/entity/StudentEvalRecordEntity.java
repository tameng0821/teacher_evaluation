package com.ambow.lyu.modules.eval.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 学生评价记录
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-24 15:43:24
 */
@Data
@TableName("tb_student_eval_record")
public class StudentEvalRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 学生评级ID
     */
    @NotNull(message = "学生评级ID不能为空")
    private Long subTaskId;
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    /**
     * 分数
     */
    @NotNull(message = "成绩不能为空")
    @Max(value = 100,message = "成绩最大不能大于100")
    @Min(value = 0,message = "成绩最小不能小于0")
    private Double score;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 姓名
     */
    @TableField(exist = false)
    private String userName;

}
