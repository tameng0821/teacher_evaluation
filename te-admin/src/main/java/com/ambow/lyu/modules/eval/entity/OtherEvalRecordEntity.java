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
 * 其他评价记录
 * 
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-06-05 09:17:10
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
	 * 其他评价ID
	 */
	@NotNull(message = "学生评级ID不能为空")
	private Long subTaskId;
	/**
	 * 用户ID
	 */
	@NotNull(message = "用户ID不能为空")
	private Long userId;
	/**
	 * 汇总分数
	 */
	@NotNull(message = "成绩不能为空")
	@Max(value = 100,message = "成绩最大不能大于100")
	@Min(value = 0,message = "成绩最小不能小于0")
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

}
