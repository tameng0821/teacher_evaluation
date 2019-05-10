package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.InspectorEvalBaseItemDao;
import com.ambow.lyu.modules.eval.entity.InspectorEvalBaseItemEntity;
import com.ambow.lyu.modules.eval.service.InspectorEvalBaseItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("inspectorEvalBaseItemService")
public class InspectorEvalBaseItemServiceImpl extends ServiceImpl<InspectorEvalBaseItemDao, InspectorEvalBaseItemEntity> implements InspectorEvalBaseItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InspectorEvalBaseItemEntity> page = this.page(
                new Query<InspectorEvalBaseItemEntity>().getPage(params),
                new QueryWrapper<InspectorEvalBaseItemEntity>()
        );

        return new PageUtils(page);
    }

}
