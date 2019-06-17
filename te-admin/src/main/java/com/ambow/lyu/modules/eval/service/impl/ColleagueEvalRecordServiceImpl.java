package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.dto.EvalTaskItemScoreDto;
import com.ambow.lyu.common.exception.TeException;
import com.ambow.lyu.common.utils.Constant;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.ColleagueEvalRecordDao;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalRecordEntity;
import com.ambow.lyu.modules.eval.entity.EvalTaskEntity;
import com.ambow.lyu.modules.eval.service.ColleagueEvalRecordService;
import com.ambow.lyu.modules.eval.service.EvalTaskService;
import com.ambow.lyu.modules.sys.entity.SysUserEntity;
import com.ambow.lyu.modules.sys.service.SysDeptService;
import com.ambow.lyu.modules.sys.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("colleagueEvalRecordService")
public class ColleagueEvalRecordServiceImpl extends ServiceImpl<ColleagueEvalRecordDao, ColleagueEvalRecordEntity> implements ColleagueEvalRecordService {

    @Autowired
    private EvalTaskService evalTaskService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDeptService sysDeptService;

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

    @Override
    public boolean add(Long taskId, Long subTaskId, String name, List<EvalTaskItemScoreDto> scoreDtoList) {
        if(taskId == null){
            throw new TeException("评价任务异常，未找到正确的评价任务！");
        }
        if(subTaskId == null){
            throw new TeException("评价任务异常，未找到正确的评价任务！");
        }
        if(StringUtils.isBlank(name)){
            throw new TeException("姓名不能为空！");
        }

        //根据subTask获得任务所属部门
        EvalTaskEntity evalTaskEntity = evalTaskService.getById(taskId);
        if(evalTaskEntity == null){
            throw new TeException("无效的评价任务，请正确的使用系统！");
        }
        List<Long> deptIds = sysDeptService.getSubDeptIdList(evalTaskEntity.getDeptId());
        deptIds.add(evalTaskEntity.getDeptId());

        //根据姓名查找userId，现在考虑为一个学院没有重名的
        SysUserEntity user = sysUserService.getOne(
                new QueryWrapper<SysUserEntity>().eq("name",name).in("dept_id",deptIds));
        if(user == null){
            throw new TeException("无效的姓名，未能在当前部门内找到此教师！");
        }

        //根据subTaskId、userId查找是否存在之前的记录，如果是修改，否则添加
        ColleagueEvalRecordEntity record = this.getOne(
                new QueryWrapper<ColleagueEvalRecordEntity>().eq("sub_task_id",subTaskId).eq("user_id",user.getUserId()));
        if(record == null){
            record = new ColleagueEvalRecordEntity();
            record.setSubTaskId(subTaskId);
            record.setUserId(user.getUserId());
            record.setDetail(EvalTaskItemScoreDto.list2string(scoreDtoList));
            record.setScore(EvalTaskItemScoreDto.calculateScore(scoreDtoList));
            record.setUpdateTime(new Date());
            return super.save(record);
        }else {
            record.setDetail(EvalTaskItemScoreDto.list2string(scoreDtoList));
            record.setScore(EvalTaskItemScoreDto.calculateScore(scoreDtoList));
            record.setUpdateTime(new Date());
            return super.updateById(record);
        }
    }

}
