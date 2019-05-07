package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.StudentEvalRecordDao;
import com.ambow.lyu.modules.eval.entity.StudentEvalRecordEntity;
import com.ambow.lyu.modules.eval.service.StudentEvalRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("studentEvalRecordService")
public class StudentEvalRecordServiceImpl extends ServiceImpl<StudentEvalRecordDao, StudentEvalRecordEntity> implements StudentEvalRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<StudentEvalRecordEntity> page = this.page(
                new Query<StudentEvalRecordEntity>().getPage(params),
                new QueryWrapper<StudentEvalRecordEntity>()
        );

        return new PageUtils(page);
    }

}
