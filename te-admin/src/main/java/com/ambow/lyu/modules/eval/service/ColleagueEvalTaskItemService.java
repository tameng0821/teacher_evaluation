package com.ambow.lyu.modules.eval.service;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalTaskItemEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 同行评价子任务评价项目
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-13 16:25:38
 */
public interface ColleagueEvalTaskItemService extends IService<ColleagueEvalTaskItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

