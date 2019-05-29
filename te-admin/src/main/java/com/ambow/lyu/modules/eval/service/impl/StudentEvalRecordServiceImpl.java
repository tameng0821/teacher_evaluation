package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.utils.Constant;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.StudentEvalRecordDao;
import com.ambow.lyu.modules.eval.entity.StudentEvalRecordEntity;
import com.ambow.lyu.modules.eval.service.StudentEvalRecordService;
import com.ambow.lyu.modules.sys.entity.SysUserEntity;
import com.ambow.lyu.modules.sys.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;


@Service("studentEvalRecordService")
public class StudentEvalRecordServiceImpl extends ServiceImpl<StudentEvalRecordDao, StudentEvalRecordEntity> implements StudentEvalRecordService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Long subTaskId = (Long) params.get(Constant.SUB_TASK_ID);

        IPage<StudentEvalRecordEntity> page = this.page(
                new Query<StudentEvalRecordEntity>().getPage(params),
                new QueryWrapper<StudentEvalRecordEntity>()
                        .eq(subTaskId != null, Constant.SUB_TASK_ID, subTaskId)
        );

        for (StudentEvalRecordEntity record : page.getRecords()) {
            SysUserEntity userEntity = sysUserService.getById(record.getUserId());
            record.setUserName(userEntity.getName());
        }

        return new PageUtils(page);
    }

    @Override
    public StudentEvalRecordEntity getById(Serializable id) {
        StudentEvalRecordEntity result = super.getById(id);
        SysUserEntity userEntity = sysUserService.getById(result.getUserId());
        result.setUserName(userEntity.getName());
        return result;
    }
}
