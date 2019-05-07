package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.OtherEvalRecordDao;
import com.ambow.lyu.modules.eval.entity.OtherEvalRecordEntity;
import com.ambow.lyu.modules.eval.service.OtherEvalRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("otherEvalRecordService")
public class OtherEvalRecordServiceImpl extends ServiceImpl<OtherEvalRecordDao, OtherEvalRecordEntity> implements OtherEvalRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OtherEvalRecordEntity> page = this.page(
                new Query<OtherEvalRecordEntity>().getPage(params),
                new QueryWrapper<OtherEvalRecordEntity>()
        );

        return new PageUtils(page);
    }

}
