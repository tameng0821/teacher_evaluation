package com.ambow.lyu.modules.eval.service;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalBaseItemEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 同行评价基础项目
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-10 16:32:24
 */
public interface ColleagueEvalBaseItemService extends IService<ColleagueEvalBaseItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

