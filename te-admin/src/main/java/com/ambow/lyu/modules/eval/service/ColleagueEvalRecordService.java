package com.ambow.lyu.modules.eval.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalRecordEntity;

import java.util.Map;

/**
 * 同行评价记录
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-06-05 09:17:11
 */
public interface ColleagueEvalRecordService extends IService<ColleagueEvalRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

