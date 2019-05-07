package com.ambow.lyu.modules.eval.service;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.StudentEvalTaskEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 学生任务
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-07 10:47:28
 */
public interface StudentEvalTaskService extends IService<StudentEvalTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

