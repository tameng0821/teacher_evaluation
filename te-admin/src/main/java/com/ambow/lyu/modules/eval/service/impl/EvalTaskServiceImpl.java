package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.EvalTaskDao;
import com.ambow.lyu.modules.eval.entity.EvalTaskEntity;
import com.ambow.lyu.modules.eval.service.EvalTaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("evalTaskService")
public class EvalTaskServiceImpl extends ServiceImpl<EvalTaskDao, EvalTaskEntity> implements EvalTaskService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<EvalTaskEntity> page = this.page(
                new Query<EvalTaskEntity>().getPage(params),
                new QueryWrapper<EvalTaskEntity>()
        );

        return new PageUtils(page);
    }

}
