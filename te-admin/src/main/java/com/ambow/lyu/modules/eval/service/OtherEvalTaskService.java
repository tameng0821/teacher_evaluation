package com.ambow.lyu.modules.eval.service;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.OtherEvalTaskEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 其他评价子任务
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-13 16:25:38
 */
public interface OtherEvalTaskService extends IService<OtherEvalTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

