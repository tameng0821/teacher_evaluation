package com.ambow.lyu.modules.eval.service;

import com.ambow.lyu.common.dto.EvalResultSummary;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.EvalResultEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
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

    List<EvalResultEntity> queryList(Map<String, Object> params);

    /**
     * 重新计算总分，计算排名
     * @param taskId 任务ID
     */
    void sortEvalResult(Long taskId);

    /**
     * 查询评价结果汇总
     * @param taskId 任务ID
     * @return 评价结果汇总
     */
    EvalResultSummary querySummary(Long taskId);

    /**
     * 查询部门列表
     * @param taskId 任务ID
     * @return 部门列表
     */
    List<String> queryDeptList(Long taskId);
}

