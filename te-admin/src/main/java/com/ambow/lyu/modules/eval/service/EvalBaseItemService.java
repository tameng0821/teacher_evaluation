package com.ambow.lyu.modules.eval.service;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.EvalBaseItemEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 评分标准基础项目
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-10 16:32:25
 */
public interface EvalBaseItemService extends IService<EvalBaseItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

