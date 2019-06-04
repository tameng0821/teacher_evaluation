package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.annotation.DataFilter;
import com.ambow.lyu.common.utils.Constant;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.InspectorEvalTaskDao;
import com.ambow.lyu.modules.eval.entity.EvalTaskEntity;
import com.ambow.lyu.modules.eval.entity.InspectorEvalTaskEntity;
import com.ambow.lyu.modules.eval.service.InspectorEvalTaskService;
import com.ambow.lyu.modules.sys.entity.SysDeptEntity;
import com.ambow.lyu.modules.sys.service.SysDeptService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("inspectorEvalTaskService")
public class InspectorEvalTaskServiceImpl extends ServiceImpl<InspectorEvalTaskDao, InspectorEvalTaskEntity> implements InspectorEvalTaskService {

    @Autowired
    private SysDeptService sysDeptService;

    @Override
    @DataFilter(roleDept = false, userDept = false, user = true)
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InspectorEvalTaskEntity> page = new Query<InspectorEvalTaskEntity>().getPage(params);

        List<InspectorEvalTaskEntity> list = baseMapper.pageGetList(page, EvalTaskEntity.Status.RELEASE.value(), (String) params.get(Constant.SQL_FILTER));

        for(InspectorEvalTaskEntity entity : list){
            SysDeptEntity deptEntity = sysDeptService.getById(entity.getDeptId());
            entity.setDeptName(deptEntity.getName());
        }

        page.setRecords(list);
        return new PageUtils(page);
    }

}
