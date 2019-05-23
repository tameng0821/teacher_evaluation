package com.ambow.lyu.modules.eval.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 同行评价子任务评价项目
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-13 16:25:38
 */
@Data
@TableName("tb_colleague_eval_task_item")
public class ColleagueEvalTaskItemEntity implements Serializable {
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
     * 评价项目名称
     */
    @NotBlank(message = "同行评价项目名称不能为空")
    private String name;
    /**
     * 评价占比
     */
    @NotNull(message = "同行评价项目占比不能为空")
    private Integer percentage;
    /**
     * 项目备注
     */
    private String remark;

}
