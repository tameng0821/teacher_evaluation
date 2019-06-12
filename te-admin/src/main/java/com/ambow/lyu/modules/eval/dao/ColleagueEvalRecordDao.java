package com.ambow.lyu.modules.eval.dao;

import com.ambow.lyu.modules.eval.entity.ColleagueEvalRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 同行评价记录
 * 
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-06-05 09:17:11
 */
@Mapper
public interface ColleagueEvalRecordDao extends BaseMapper<ColleagueEvalRecordEntity> {

    /**
     * 分页条件查询
     * @param page 分页条件
     * @param subTaskId 同行评价子任务ID
     * @param name 用户名
     * @param sql_filter 数据隔离
     * @return
     */
    List<ColleagueEvalRecordEntity> pageGetList(IPage<ColleagueEvalRecordEntity> page, Long subTaskId, String name, String sql_filter);
}
