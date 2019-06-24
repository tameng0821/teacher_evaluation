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
 * @date 2019-06-24 14:54:15
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
	 * 关联评价任务ID
	 */
	private Long taskId;
	/**
	 * 用户名/工号
	 */
	private String username;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 系部名称
	 */
	private String deptName;
	/**
	 * 学生评价分数
	 */
	private Double studentEvalScore;
	/**
	 * 学生评价细节
	 */
	private String studentEvalDetail;
	/**
	 * 同行评价分数
	 */
	private Double colleagueEvalScore;
	/**
	 * 同行评价细节
	 */
	private String colleagueEvalDetail;
	/**
	 * 督导评价分数
	 */
	private Double inspectorEvalScore;
	/**
	 * 督导评价细节
	 */
	private String inspectorEvalDetail;
	/**
	 * 其他评价分数
	 */
	private Double otherEvalScore;
	/**
	 * 其他评价细节
	 */
	private String otherEvalDetail;
	/**
	 * 总分
	 */
	private Double accountScore;
	/**
	 * 排名
	 */
	private Long ranking;
	/**
	 * 评级：30%为优秀，30~70为良好，70~100为合格，小于60分为不合格
	 */
	private String rating;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 修改时间
	 */
	private Date updateTime;

}
