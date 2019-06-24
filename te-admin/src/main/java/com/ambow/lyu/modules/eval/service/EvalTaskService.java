package com.ambow.lyu.modules.eval.service;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.EvalTaskEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;
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

    /**
     * 生成评价结果
     * @param taskId 任务ID
     */
    void generateResult(Long taskId);

    /**
     * 获取评价任务状态
     *
     * @param taskId 评价任务ID
     * @return 评价任务状态
     */
    EvalTaskEntity.Status getTaskStatus(Long taskId);

    /**
     * 修改评价任务状态
     *
     * @param taskId 任务ID
     * @param status 任务状态
     * @return 执行结果
     */
    boolean updateTaskStatus(Long taskId, EvalTaskEntity.Status status);

    /**
     * 删除评价任务
     * 级联删除学生、同行、督导、其他
     *
     * @param idList 评价任务IDs
     */
    void deleteById(Collection<? extends Serializable> idList);

    /**
     * 根据ID查询评价任务，包含评价子任务详情
     * @param id 评价任务ID
     * @return 评价任务
     */
    EvalTaskEntity findById(Serializable id);
    /**
     * 添加评价任务，包含评价子任务详情
     * @param entity 评价任务ID
     * @return 是否成功
     */
    boolean add(EvalTaskEntity entity);
    /**
     * 修改评价任务，包含评价子任务详情
     * @param viewTask 评价任务ID
     * @return 是否成功
     */
    boolean modifyById(EvalTaskEntity viewTask);

}

