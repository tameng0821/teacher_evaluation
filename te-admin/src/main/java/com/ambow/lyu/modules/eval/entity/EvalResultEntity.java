package com.ambow.lyu.modules.eval.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评价结果
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-07 16:49:54
 */
@Data
@TableName("tb_eval_result")
public class EvalResultEntity implements Serializable {
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
     * 用户ID
     */
    private Long userId;
    /**
     * 学生评价分数
     */
    private Double studentEvalScore;
    /**
     * 同行评价分数
     */
    private Double colleagueEvalScore;
    /**
     * 督导评价分数
     */
    private Double inspectorEvalScore;
    /**
     * 其他评价分数
     */
    private Double otherEvalScore;
    /**
     * 合计分数
     */
    private Double accountScore;
    /**
     * 排名
     */
    private Long ranking;
    /**
     * 修改时间
     */
    private Date updateTime;

}
