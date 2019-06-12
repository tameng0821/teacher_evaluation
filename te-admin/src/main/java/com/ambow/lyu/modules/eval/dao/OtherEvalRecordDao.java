package com.ambow.lyu.modules.eval.dao;

import com.ambow.lyu.modules.eval.entity.OtherEvalRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 其他评价记录
 * 
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-06-05 09:17:10
 */
@Mapper
public interface OtherEvalRecordDao extends BaseMapper<OtherEvalRecordEntity> {

    /**
     * 分页条件查询
     * @param page 分页条件
     * @param subTaskId 其他评价子任务ID
     * @param name 用户名
     * @param sql_filter 数据隔离
     * @return
     */
    List<OtherEvalRecordEntity> pageGetList(IPage<OtherEvalRecordEntity> page, Long subTaskId, String name, String sql_filter);
	
}
