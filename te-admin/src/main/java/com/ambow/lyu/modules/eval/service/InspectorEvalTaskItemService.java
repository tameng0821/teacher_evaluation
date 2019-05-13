package com.ambow.lyu.modules.eval.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.InspectorEvalTaskItemEntity;

import java.util.Map;

/**
 * 督导评价子任务评价项目
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-13 16:25:38
 */
public interface InspectorEvalTaskItemService extends IService<InspectorEvalTaskItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

