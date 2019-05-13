package com.ambow.lyu.modules.eval.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
	private Long deptId;
	/**
	 * 系主任ID
	 */
	private Long userId;
	/**
	 * 评价占比
	 */
	private Integer percentage;

}
