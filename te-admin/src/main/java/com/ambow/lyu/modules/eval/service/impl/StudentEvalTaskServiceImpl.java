package com.ambow.lyu.modules.eval.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;

import com.ambow.lyu.modules.eval.dao.StudentEvalTaskDao;
import com.ambow.lyu.modules.eval.entity.StudentEvalTaskEntity;
import com.ambow.lyu.modules.eval.service.StudentEvalTaskService;


@Service("studentEvalTaskService")
public class StudentEvalTaskServiceImpl extends ServiceImpl<StudentEvalTaskDao, StudentEvalTaskEntity> implements StudentEvalTaskService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<StudentEvalTaskEntity> page = this.page(
                new Query<StudentEvalTaskEntity>().getPage(params),
                new QueryWrapper<StudentEvalTaskEntity>()
        );

        return new PageUtils(page);
    }

}
