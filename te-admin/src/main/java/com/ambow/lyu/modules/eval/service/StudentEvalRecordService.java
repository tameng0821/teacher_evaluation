package com.ambow.lyu.modules.eval.service;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.StudentEvalRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Map;

/**
 * 学生评价记录
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-24 15:43:24
 */
public interface StudentEvalRecordService extends IService<StudentEvalRecordEntity> {

    /**
     * 分页查询
     * @param params 查询参数
     * @return 封装结果
     */
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

    /**
     * 根据ID查找学生评价记录
     * @param id 数据库ID
     * @return 学生评价记录包含数据库不存在的字段
     */
    StudentEvalRecordEntity findById(Serializable id);
}

