package com.ambow.lyu.modules.eval.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;

import com.ambow.lyu.modules.eval.dao.ColleagueEvalTaskDao;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalTaskEntity;
import com.ambow.lyu.modules.eval.service.ColleagueEvalTaskService;


@Service("colleagueEvalTaskService")
public class ColleagueEvalTaskServiceImpl extends ServiceImpl<ColleagueEvalTaskDao, ColleagueEvalTaskEntity> implements ColleagueEvalTaskService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ColleagueEvalTaskEntity> page = this.page(
                new Query<ColleagueEvalTaskEntity>().getPage(params),
                new QueryWrapper<ColleagueEvalTaskEntity>()
        );

        return new PageUtils(page);
    }

}
