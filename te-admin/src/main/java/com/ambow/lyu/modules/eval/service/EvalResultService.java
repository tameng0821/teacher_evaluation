package com.ambow.lyu.modules.eval.service;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.EvalResultEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 评价结果
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-07 16:49:54
 */
public interface EvalResultService extends IService<EvalResultEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

