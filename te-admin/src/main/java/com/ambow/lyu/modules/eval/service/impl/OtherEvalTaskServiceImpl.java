package com.ambow.lyu.modules.eval.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;

import com.ambow.lyu.modules.eval.dao.OtherEvalTaskDao;
import com.ambow.lyu.modules.eval.entity.OtherEvalTaskEntity;
import com.ambow.lyu.modules.eval.service.OtherEvalTaskService;


@Service("otherEvalTaskService")
public class OtherEvalTaskServiceImpl extends ServiceImpl<OtherEvalTaskDao, OtherEvalTaskEntity> implements OtherEvalTaskService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OtherEvalTaskEntity> page = this.page(
                new Query<OtherEvalTaskEntity>().getPage(params),
                new QueryWrapper<OtherEvalTaskEntity>()
        );

        return new PageUtils(page);
    }

}
