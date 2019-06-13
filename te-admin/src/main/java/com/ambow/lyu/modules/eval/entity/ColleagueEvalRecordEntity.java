package com.ambow.lyu.modules.eval.entity;

import com.ambow.lyu.modules.eval.dto.EvalTaskItemScoreDto;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 同行评价记录
 * 
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-06-05 09:17:11
 */
@Data
@TableName("tb_colleague_eval_record")
public class ColleagueEvalRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 同行评价ID
	 */
	@NotNull(message = "数据错误")
	private Long subTaskId;
	/**
	 * 用户ID
	 */
	@NotNull(message = "用户不能为空")
	private Long userId;
	/**
	 * 记录每个评价选项以及分数，格式：taskItemID:name:percentage:taskItemScore,taskItemID1:name:percentage:taskItemScore1
	 */
	private String detail;
	/**
	 * 汇总分数
	 */
	private Double score;
	/**
	 * 更新时间
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

	/**
	 * 得分详情
	 */
	@TableField(exist = false)
	@Valid
	private List<EvalTaskItemScoreDto> evalItemResults;

}
