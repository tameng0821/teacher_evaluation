package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.exception.TeException;
import com.ambow.lyu.common.utils.Constant;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.StudentEvalRecordDao;
import com.ambow.lyu.modules.eval.entity.EvalTaskEntity;
import com.ambow.lyu.modules.eval.entity.StudentEvalRecordEntity;
import com.ambow.lyu.modules.eval.entity.StudentEvalTaskEntity;
import com.ambow.lyu.modules.eval.service.EvalTaskService;
import com.ambow.lyu.modules.eval.service.StudentEvalRecordService;
import com.ambow.lyu.modules.eval.service.StudentEvalTaskService;
import com.ambow.lyu.modules.sys.entity.SysUserEntity;
import com.ambow.lyu.modules.sys.service.SysDeptService;
import com.ambow.lyu.modules.sys.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("studentEvalRecordService")
public class StudentEvalRecordServiceImpl extends ServiceImpl<StudentEvalRecordDao, StudentEvalRecordEntity> implements StudentEvalRecordService {
    @Autowired
    private EvalTaskService evalTaskService;
    @Autowired
    private StudentEvalTaskService studentEvalTaskService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDeptService sysDeptService;

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
    public boolean add(Long subTaskId,String name, Double score) {

        if(subTaskId == null){
            throw new TeException("评价任务异常，未找到正确的评价任务！");
        }
        if(StringUtils.isBlank(name)){
            throw new TeException("姓名不能为空！");
        }
        if(score == null){
            throw new TeException("分数不能为空！");
        }

        //根据subTask获得任务所属部门
        StudentEvalTaskEntity studentEvalTaskEntity = studentEvalTaskService.getById(subTaskId);
        if(studentEvalTaskEntity == null){
            throw new TeException("无效的评价任务，请正确的使用系统！");
        }
        EvalTaskEntity evalTaskEntity = evalTaskService.getById(studentEvalTaskEntity.getTaskId());
        List<Long> deptIds = sysDeptService.getSubDeptIdList(evalTaskEntity.getDeptId());
        deptIds.add(evalTaskEntity.getDeptId());

        //根据姓名查找userId，现在考虑为一个学院没有重名的
        SysUserEntity user = sysUserService.getOne(
                new QueryWrapper<SysUserEntity>().eq("name",name).in("dept_id",deptIds));
        if(user == null){
            throw new TeException("无效的姓名，未能在系统内找到此教师！");
        }
        //根据subTaskId、userId查找是否存在之前的记录，如果是修改，否则添加
        StudentEvalRecordEntity record = this.getOne(
                new QueryWrapper<StudentEvalRecordEntity>().eq("sub_task_id",subTaskId).eq("user_id",user.getUserId()));

        if(record == null){
            record = new StudentEvalRecordEntity();
            record.setSubTaskId(subTaskId);
            record.setUserId(user.getUserId());
            record.setScore(score);
            record.setUpdateTime(new Date());
            return super.save(record);
        }else {
            record.setScore(score);
            record.setUpdateTime(new Date());
            return super.updateById(record);
        }
    }

    @Override
    public StudentEvalRecordEntity findById(Serializable id) {
        StudentEvalRecordEntity result = super.getById(id);
        SysUserEntity userEntity = sysUserService.getById(result.getUserId());
        result.setUserName(userEntity.getName());
        return result;
    }
}
