package com.ambow.lyu.modules.eval.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.EvalResultEntity;

import java.util.Map;

/**
 * 评价结果
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-06-24 14:54:15
 */
public interface EvalResultService extends IService<EvalResultEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 重新计算总分，计算排名
     * @param taskId 任务ID
     */
    void sortEvalResult(Long taskId);
}

