package com.ambow.lyu.modules.eval.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.OtherEvalRecordEntity;

import java.util.Map;

/**
 * 其他评价记录
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-06-05 09:17:10
 */
public interface OtherEvalRecordService extends IService<OtherEvalRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 添加一条记录
     * @param taskId 评价任务ID
     * @param subTaskId  学生评价任务ID
     * @param username 用户名/工号
     * @param name 姓名
     * @param score 分数
     * @return 是否成功
     */
    boolean add(Long taskId,Long subTaskId,String username,String name,Double score);
}

