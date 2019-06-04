package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.annotation.DataFilter;
import com.ambow.lyu.common.utils.Constant;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.ColleagueEvalTaskDao;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalTaskEntity;
import com.ambow.lyu.modules.eval.entity.EvalTaskEntity;
import com.ambow.lyu.modules.eval.service.ColleagueEvalTaskService;
import com.ambow.lyu.modules.sys.entity.SysDeptEntity;
import com.ambow.lyu.modules.sys.service.SysDeptService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("colleagueEvalTaskService")
public class ColleagueEvalTaskServiceImpl extends ServiceImpl<ColleagueEvalTaskDao, ColleagueEvalTaskEntity> implements ColleagueEvalTaskService {

    @Autowired
    private SysDeptService sysDeptService;

    @Override
    @DataFilter(roleDept = false, userDept = false, user = true)
    public PageUtils queryPage(Map<String, Object> params) {

        IPage<ColleagueEvalTaskEntity> page = new Query<ColleagueEvalTaskEntity>().getPage(params);

        List<ColleagueEvalTaskEntity> list = baseMapper.pageGetList(page, EvalTaskEntity.Status.RELEASE.value(), (String) params.get(Constant.SQL_FILTER));

        for(ColleagueEvalTaskEntity entity : list){
            SysDeptEntity deptEntity = sysDeptService.getById(entity.getDeptId());
            entity.setDeptName(deptEntity.getName());
        }

        page.setRecords(list);
        return new PageUtils(page);
    }

}
