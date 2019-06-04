package com.ambow.lyu.modules.eval.dao;

import com.ambow.lyu.modules.eval.entity.InspectorEvalTaskEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 督导评价子任务
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-13 16:25:38
 */
@Mapper
public interface InspectorEvalTaskDao extends BaseMapper<InspectorEvalTaskEntity> {

    /**
     * 分页查询
     *
     * @param page           page 翻页对象，可以作为 xml 参数直接使用，传递参数 Page 即自动分页
     * @param evalTaskStatus 评价任务状态
     * @param sql_filter 拼接SQL
     * @return 查询结果
     */
    List<InspectorEvalTaskEntity> pageGetList(IPage<InspectorEvalTaskEntity> page, Integer evalTaskStatus,String sql_filter);

}
