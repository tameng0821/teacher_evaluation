package com.ambow.lyu.modules.eval.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;

import com.ambow.lyu.modules.eval.dao.ColleagueEvalTaskItemDao;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalTaskItemEntity;
import com.ambow.lyu.modules.eval.service.ColleagueEvalTaskItemService;


@Service("colleagueEvalTaskItemService")
public class ColleagueEvalTaskItemServiceImpl extends ServiceImpl<ColleagueEvalTaskItemDao, ColleagueEvalTaskItemEntity> implements ColleagueEvalTaskItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ColleagueEvalTaskItemEntity> page = this.page(
                new Query<ColleagueEvalTaskItemEntity>().getPage(params),
                new QueryWrapper<ColleagueEvalTaskItemEntity>()
        );

        return new PageUtils(page);
    }

}
