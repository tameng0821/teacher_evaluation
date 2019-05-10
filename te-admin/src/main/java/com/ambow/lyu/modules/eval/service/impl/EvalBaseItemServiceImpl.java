package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.EvalBaseItemDao;
import com.ambow.lyu.modules.eval.entity.EvalBaseItemEntity;
import com.ambow.lyu.modules.eval.service.EvalBaseItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("evalBaseItemService")
public class EvalBaseItemServiceImpl extends ServiceImpl<EvalBaseItemDao, EvalBaseItemEntity> implements EvalBaseItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<EvalBaseItemEntity> page = this.page(
                new Query<EvalBaseItemEntity>().getPage(params),
                new QueryWrapper<EvalBaseItemEntity>()
        );

        return new PageUtils(page);
    }

}
