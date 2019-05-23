package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.OtherEvalTaskDao;
import com.ambow.lyu.modules.eval.entity.OtherEvalTaskEntity;
import com.ambow.lyu.modules.eval.service.OtherEvalTaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


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
