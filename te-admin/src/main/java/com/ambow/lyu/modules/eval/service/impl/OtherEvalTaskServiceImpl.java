package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.utils.Constant;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.OtherEvalTaskDao;
import com.ambow.lyu.modules.eval.entity.EvalTaskEntity;
import com.ambow.lyu.modules.eval.entity.OtherEvalTaskEntity;
import com.ambow.lyu.modules.eval.service.OtherEvalTaskService;
import com.ambow.lyu.modules.sys.entity.SysDeptEntity;
import com.ambow.lyu.modules.sys.service.SysDeptService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("otherEvalTaskService")
public class OtherEvalTaskServiceImpl extends ServiceImpl<OtherEvalTaskDao, OtherEvalTaskEntity> implements OtherEvalTaskService {

    @Autowired
    private SysDeptService sysDeptService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OtherEvalTaskEntity> page = new Query<OtherEvalTaskEntity>().getPage(params);

        List<OtherEvalTaskEntity> list = baseMapper.pageGetList(page, EvalTaskEntity.Status.RELEASE.value(), (String) params.get(Constant.SQL_FILTER));

        for(OtherEvalTaskEntity entity : list){
            SysDeptEntity deptEntity = sysDeptService.getById(entity.getDeptId());
            entity.setDeptName(deptEntity.getName());
        }

        page.setRecords(list);
        return new PageUtils(page);
    }

}
