package com.ambow.lyu.modules.eval.service;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 同行评价记录
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-07 10:47:29
 */
public interface ColleagueEvalRecordService extends IService<ColleagueEvalRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

