package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.modules.sys.entity.SysDeptEntity;
import com.ambow.lyu.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;

import com.ambow.lyu.modules.eval.dao.EvalTaskDao;
import com.ambow.lyu.modules.eval.entity.EvalTaskEntity;
import com.ambow.lyu.modules.eval.service.EvalTaskService;


@Service("evalTaskService")
public class EvalTaskServiceImpl extends ServiceImpl<EvalTaskDao, EvalTaskEntity> implements EvalTaskService {

    @Autowired
    private SysDeptService sysDeptService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<EvalTaskEntity> page = this.page(
                new Query<EvalTaskEntity>().getPage(params),
                new QueryWrapper<EvalTaskEntity>()
        );

        for (EvalTaskEntity evalTaskEntity : page.getRecords()) {
            SysDeptEntity sysDeptEntity = sysDeptService.getById(evalTaskEntity.getDeptId());
            evalTaskEntity.setDeptName(sysDeptEntity.getName());
        }

        return new PageUtils(page);
    }

    @Override
    public EvalTaskEntity getById(Serializable id) {

        EvalTaskEntity evalTask = super.getById(id);

        if(evalTask != null){
            SysDeptEntity sysDeptEntity = sysDeptService.getById(evalTask.getDeptId());
            evalTask.setDeptName(sysDeptEntity.getName());
        }

        return evalTask;
    }

}
