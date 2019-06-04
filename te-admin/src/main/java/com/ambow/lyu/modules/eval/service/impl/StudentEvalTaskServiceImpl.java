package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.utils.Constant;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.StudentEvalTaskDao;
import com.ambow.lyu.modules.eval.entity.EvalTaskEntity;
import com.ambow.lyu.modules.eval.entity.StudentEvalTaskEntity;
import com.ambow.lyu.modules.eval.service.StudentEvalTaskService;
import com.ambow.lyu.modules.sys.entity.SysDeptEntity;
import com.ambow.lyu.modules.sys.service.SysDeptService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("studentEvalTaskService")
public class StudentEvalTaskServiceImpl extends ServiceImpl<StudentEvalTaskDao, StudentEvalTaskEntity> implements StudentEvalTaskService {

    @Autowired
    private SysDeptService sysDeptService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        IPage<StudentEvalTaskEntity> page = new Query<StudentEvalTaskEntity>().getPage(params);
        List<StudentEvalTaskEntity> list = baseMapper.pageGetList(page, EvalTaskEntity.Status.RELEASE.value(), (String) params.get(Constant.SQL_FILTER));

        for (StudentEvalTaskEntity vo : list) {
            SysDeptEntity sysDeptEntity = sysDeptService.getById(vo.getDeptId());
            vo.setDeptName(sysDeptEntity.getName());
        }

        page.setRecords(list);
        return new PageUtils(page);
    }

}
