package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.utils.Constant;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.ColleagueEvalRecordDao;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalRecordEntity;
import com.ambow.lyu.modules.eval.service.ColleagueEvalRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("colleagueEvalRecordService")
public class ColleagueEvalRecordServiceImpl extends ServiceImpl<ColleagueEvalRecordDao, ColleagueEvalRecordEntity> implements ColleagueEvalRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        Long subTaskId = (Long) params.get(Constant.SUB_TASK_ID);
        String name = (String) params.get("name");

        IPage<ColleagueEvalRecordEntity> page = new Query<ColleagueEvalRecordEntity>().getPage(params);

        List<ColleagueEvalRecordEntity> list = baseMapper.pageGetList(page,
                subTaskId,name, (String) params.get(Constant.SQL_FILTER));

        page.setRecords(list);
        return new PageUtils(page);
    }

}
