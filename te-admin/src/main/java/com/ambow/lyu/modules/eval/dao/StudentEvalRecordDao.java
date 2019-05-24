package com.ambow.lyu.modules.eval.dao;

import com.ambow.lyu.modules.eval.entity.StudentEvalRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 学生评价记录
 * 
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-24 15:43:24
 */
@Mapper
public interface StudentEvalRecordDao extends BaseMapper<StudentEvalRecordEntity> {
	
}
