package com.ambow.lyu.modules.eval.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.EvalTaskEntity;

import java.util.Map;

/**
 * 评价任务
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-13 16:25:38
 */
public interface EvalTaskService extends IService<EvalTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);

}

