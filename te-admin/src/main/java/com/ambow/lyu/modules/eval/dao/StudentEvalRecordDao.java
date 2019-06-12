package com.ambow.lyu.modules.eval.dao;

import com.ambow.lyu.modules.eval.entity.StudentEvalRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 学生评价记录
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-24 15:43:24
 */
@Mapper
public interface StudentEvalRecordDao extends BaseMapper<StudentEvalRecordEntity> {

    /**
     * 分页条件查询
     * @param page 分页条件
     * @param subTaskId 学生评价子任务ID
     * @param name 用户名
     * @param sql_filter 数据隔离
     * @return
     */
    List<StudentEvalRecordEntity> pageGetList(IPage<StudentEvalRecordEntity> page,Long subTaskId, String name, String sql_filter);

}
