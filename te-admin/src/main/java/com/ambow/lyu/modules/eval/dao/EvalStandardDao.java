package com.ambow.lyu.modules.eval.dao;

import com.ambow.lyu.modules.eval.entity.EvalStandardEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评分标准预置数据，只做比例的修改
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-07 10:47:29
 */
@Mapper
public interface EvalStandardDao extends BaseMapper<EvalStandardEntity> {

}
