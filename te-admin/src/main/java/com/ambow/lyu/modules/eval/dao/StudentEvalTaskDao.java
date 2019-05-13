package com.ambow.lyu.modules.eval.dao;

import com.ambow.lyu.modules.eval.entity.StudentEvalTaskEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 学生评价子任务
 * 
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-13 16:25:38
 */
@Mapper
public interface StudentEvalTaskDao extends BaseMapper<StudentEvalTaskEntity> {
	
}
