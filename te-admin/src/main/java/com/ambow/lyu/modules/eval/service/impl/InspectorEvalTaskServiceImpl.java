package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.InspectorEvalTaskDao;
import com.ambow.lyu.modules.eval.entity.InspectorEvalTaskEntity;
import com.ambow.lyu.modules.eval.service.InspectorEvalTaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("inspectorEvalTaskService")
public class InspectorEvalTaskServiceImpl extends ServiceImpl<InspectorEvalTaskDao, InspectorEvalTaskEntity> implements InspectorEvalTaskService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InspectorEvalTaskEntity> page = this.page(
                new Query<InspectorEvalTaskEntity>().getPage(params),
                new QueryWrapper<InspectorEvalTaskEntity>()
        );

        return new PageUtils(page);
    }

}
