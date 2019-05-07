package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.InspectorEvalRecordDao;
import com.ambow.lyu.modules.eval.entity.InspectorEvalRecordEntity;
import com.ambow.lyu.modules.eval.service.InspectorEvalRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("inspectorEvalRecordService")
public class InspectorEvalRecordServiceImpl extends ServiceImpl<InspectorEvalRecordDao, InspectorEvalRecordEntity> implements InspectorEvalRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InspectorEvalRecordEntity> page = this.page(
                new Query<InspectorEvalRecordEntity>().getPage(params),
                new QueryWrapper<InspectorEvalRecordEntity>()
        );

        return new PageUtils(page);
    }

}
