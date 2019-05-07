package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.ColleagueEvalRecordDao;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalRecordEntity;
import com.ambow.lyu.modules.eval.service.ColleagueEvalRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("colleagueEvalRecordService")
public class ColleagueEvalRecordServiceImpl extends ServiceImpl<ColleagueEvalRecordDao, ColleagueEvalRecordEntity> implements ColleagueEvalRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ColleagueEvalRecordEntity> page = this.page(
                new Query<ColleagueEvalRecordEntity>().getPage(params),
                new QueryWrapper<ColleagueEvalRecordEntity>()
        );

        return new PageUtils(page);
    }

}
