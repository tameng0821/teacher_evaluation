package com.ambow.lyu.modules.eval.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.StudentEvalTaskEntity;

import java.util.Map;

/**
 * 学生评价子任务
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-13 16:25:38
 */
public interface StudentEvalTaskService extends IService<StudentEvalTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

