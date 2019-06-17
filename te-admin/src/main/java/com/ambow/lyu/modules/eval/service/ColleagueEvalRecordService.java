package com.ambow.lyu.modules.eval.service;

import com.ambow.lyu.common.dto.EvalTaskItemScoreDto;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
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

    /**
     * 添加一条记录
     * @param taskId 评价任务ID
     * @param subTaskId  同行评价任务ID
     * @param name user姓名
     * @param scoreDtoList 分数
     * @return 是否成功
     */
    boolean add(Long taskId, Long subTaskId, String name, List<EvalTaskItemScoreDto> scoreDtoList);
}

