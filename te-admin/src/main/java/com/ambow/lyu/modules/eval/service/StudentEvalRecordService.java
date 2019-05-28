package com.ambow.lyu.modules.eval.service;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.eval.entity.StudentEvalRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 学生评价记录
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-24 15:43:24
 */
public interface StudentEvalRecordService extends IService<StudentEvalRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

