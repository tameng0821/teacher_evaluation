package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.InspectorEvalTaskItemDao;
import com.ambow.lyu.modules.eval.entity.InspectorEvalTaskItemEntity;
import com.ambow.lyu.modules.eval.service.InspectorEvalTaskItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("inspectorEvalTaskItemService")
public class InspectorEvalTaskItemServiceImpl extends ServiceImpl<InspectorEvalTaskItemDao, InspectorEvalTaskItemEntity> implements InspectorEvalTaskItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InspectorEvalTaskItemEntity> page = this.page(
                new Query<InspectorEvalTaskItemEntity>().getPage(params),
                new QueryWrapper<InspectorEvalTaskItemEntity>()
        );

        return new PageUtils(page);
    }

}
