package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.ColleagueEvalBaseItemDao;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalBaseItemEntity;
import com.ambow.lyu.modules.eval.service.ColleagueEvalBaseItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("colleagueEvalBaseItemService")
public class ColleagueEvalBaseItemServiceImpl extends ServiceImpl<ColleagueEvalBaseItemDao, ColleagueEvalBaseItemEntity> implements ColleagueEvalBaseItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ColleagueEvalBaseItemEntity> page = this.page(
                new Query<ColleagueEvalBaseItemEntity>().getPage(params),
                new QueryWrapper<ColleagueEvalBaseItemEntity>()
        );

        return new PageUtils(page);
    }

}
