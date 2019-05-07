package com.ambow.lyu.modules.eval.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 其他评价记录
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-07 10:47:28
 */
@Data
@TableName("tb_other_eval_record")
public class OtherEvalRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 子任务ID
     */
    private Long subTaskId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 分数
     */
    private Double score;
    /**
     * 修改时间
     */
    private Date updateTime;

}
