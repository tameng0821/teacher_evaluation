package com.ambow.lyu.modules.eval.service;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.InspectorEvalTaskItemEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
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

    List<InspectorEvalTaskItemEntity> selectByTaskId(Long taskId);
}

