package com.ambow.lyu.modules.eval.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 同行/系主任评价子任务
 * 
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-13 16:25:38
 */
@Data
@TableName("tb_colleague_eval_task")
public class ColleagueEvalTaskEntity implements Serializable {
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
	 * 部门ID
	 */
	@NotNull(message = "同行评价所属部门不能为空")
	private Long deptId;
	/**
	 * 系主任ID
	 */
	@NotNull(message = "同行评价指定评价人不能为空")
	private Long userId;
	/**
	 * 评价占比
	 */
	private Integer percentage;

	/**
	 * 部门名称
	 */
	@TableField(exist = false)
	private String deptName;
	/**
	 * 系主任姓名
	 */
	@TableField(exist = false)
	private String userName;


}
