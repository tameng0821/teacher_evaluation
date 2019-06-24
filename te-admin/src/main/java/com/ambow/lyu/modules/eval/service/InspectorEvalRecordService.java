package com.ambow.lyu.modules.eval.service;

import com.ambow.lyu.common.dto.EvalTaskItemScoreDto;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.InspectorEvalRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 督导评价记录
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-06-05 09:17:11
 */
public interface InspectorEvalRecordService extends IService<InspectorEvalRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 添加一条记录
     * @param taskId 评价任务ID
     * @param subTaskId  同行评价任务ID
     * @param username 用户名/工号
     * @param name 姓名
     * @param scoreDtoList 分数
     * @return 是否成功
     */
    boolean add(Long taskId, Long subTaskId,String username, String name, List<EvalTaskItemScoreDto> scoreDtoList);
}

