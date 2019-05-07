package com.ambow.lyu.modules.eval.service;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.EvalStandardEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 评分标准预置数据，只做比例的修改
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-07 10:47:29
 */
public interface EvalStandardService extends IService<EvalStandardEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

