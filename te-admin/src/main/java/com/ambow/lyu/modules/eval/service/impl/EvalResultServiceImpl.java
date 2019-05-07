package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.EvalResultDao;
import com.ambow.lyu.modules.eval.entity.EvalResultEntity;
import com.ambow.lyu.modules.eval.service.EvalResultService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("evalResultService")
public class EvalResultServiceImpl extends ServiceImpl<EvalResultDao, EvalResultEntity> implements EvalResultService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<EvalResultEntity> page = this.page(
                new Query<EvalResultEntity>().getPage(params),
                new QueryWrapper<EvalResultEntity>()
        );

        return new PageUtils(page);
    }

}
