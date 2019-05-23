package com.ambow.lyu.modules.eval.service;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.InspectorEvalTaskEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 督导评价子任务
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-13 16:25:38
 */
public interface InspectorEvalTaskService extends IService<InspectorEvalTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

