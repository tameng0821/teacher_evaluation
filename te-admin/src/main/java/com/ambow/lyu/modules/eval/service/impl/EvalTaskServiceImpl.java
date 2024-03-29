package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.exception.TeException;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.EvalTaskDao;
import com.ambow.lyu.modules.eval.entity.*;
import com.ambow.lyu.modules.eval.service.*;
import com.ambow.lyu.modules.sys.entity.SysDeptEntity;
import com.ambow.lyu.modules.sys.entity.SysUserEntity;
import com.ambow.lyu.modules.sys.service.SysDeptService;
import com.ambow.lyu.modules.sys.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;


@Service("evalTaskService")
public class EvalTaskServiceImpl extends ServiceImpl<EvalTaskDao, EvalTaskEntity> implements EvalTaskService {

    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private StudentEvalTaskService studentEvalTaskService;
    @Autowired
    private ColleagueEvalTaskItemService colleagueEvalTaskItemService;
    @Autowired
    private ColleagueEvalTaskService colleagueEvalTaskService;
    @Autowired
    private InspectorEvalTaskItemService inspectorEvalTaskItemService;
    @Autowired
    private InspectorEvalTaskService inspectorEvalTaskService;
    @Autowired
    private OtherEvalTaskService otherEvalTaskService;
    @Autowired
    private StudentEvalRecordService studentEvalRecordService;
    @Autowired
    private ColleagueEvalRecordService colleagueEvalRecordService;
    @Autowired
    private InspectorEvalRecordService inspectorEvalRecordService;
    @Autowired
    private OtherEvalRecordService otherEvalRecordService;
    @Autowired
    private EvalResultService evalResultService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        Integer status = (Integer) params.get("task_status");

        IPage<EvalTaskEntity> page = super.page(
                new Query<EvalTaskEntity>().getPage(params),
                new QueryWrapper<EvalTaskEntity>().eq(status != null,"status",status)
        );

        for (EvalTaskEntity evalTaskEntity : page.getRecords()) {
            SysDeptEntity sysDeptEntity = sysDeptService.getById(evalTaskEntity.getDeptId());
            evalTaskEntity.setDeptName(sysDeptEntity.getName());
        }

        return new PageUtils(page);
    }

    @Override
    public void generateResult(Long taskId) {
        EvalTaskEntity evalTask = super.getById(taskId);

        //查找参评人数
        int headcount = sysUserService.countByDept(evalTask.getDeptId());

        //查看评价结果进度，如果没有全部完成则不能生成评价结果

        //查询学生评价进度
        StudentEvalTaskEntity studentEvalTaskEntity = studentEvalTaskService.getOne(
                new QueryWrapper<StudentEvalTaskEntity>().eq("task_id", taskId));
        int studentCount = studentEvalRecordService.count(
                new QueryWrapper<StudentEvalRecordEntity>().eq("sub_task_id", studentEvalTaskEntity.getId()));

        if(studentCount!=headcount){
            throw new TeException("学生评价任务未完成，不能生成评价结果");
        }
        //查询同行评价进度
        List<ColleagueEvalTaskEntity> colleagueEvalTasks = colleagueEvalTaskService.list(
                new QueryWrapper<ColleagueEvalTaskEntity>().eq("task_id", taskId));
        for (ColleagueEvalTaskEntity colleagueEvalTask : colleagueEvalTasks) {
            //查询同行评价进度
            int colleagueCount = colleagueEvalRecordService.count(
                    new QueryWrapper<ColleagueEvalRecordEntity>().eq("sub_task_id", colleagueEvalTask.getId()));

           int colleagueHeadCount  = sysUserService.countByDept(colleagueEvalTask.getDeptId());

            if(colleagueCount!=colleagueHeadCount){
                throw new TeException("同行评价任务未完成，不能生成评价结果");
            }
        }
        //查询督导评价进度
        List<InspectorEvalTaskEntity> inspectorEvalTasks = inspectorEvalTaskService.list(
                new QueryWrapper<InspectorEvalTaskEntity>().eq("task_id", taskId));
        for (InspectorEvalTaskEntity inspectorEvalTask : inspectorEvalTasks) {
            int inspectorCount = inspectorEvalRecordService.count(
                    new QueryWrapper<InspectorEvalRecordEntity>().eq("sub_task_id", inspectorEvalTask.getId()));
            if(inspectorCount!=headcount){
                throw new TeException("督导评价任务未完成，不能生成评价结果");
            }
        }
        //查询其他评价进度
        OtherEvalTaskEntity otherEvalTaskEntity = otherEvalTaskService.getOne(
                new QueryWrapper<OtherEvalTaskEntity>().eq("task_id", taskId));
        int otherCount = otherEvalRecordService.count(
                new QueryWrapper<OtherEvalRecordEntity>().eq("sub_task_id", otherEvalTaskEntity.getId()));
        if(otherCount!=headcount){
            throw new TeException("其他评价任务未完成，不能生成评价结果");
        }
        //Map<userId,评价结果>
        Map<Long,EvalResultEntity> resultEntityMap = new HashMap<>(headcount);

        //设置学生评价结果
        List<StudentEvalRecordEntity> studentEvalRecordEntityList = studentEvalRecordService.list(
                new QueryWrapper<StudentEvalRecordEntity>().eq("sub_task_id", studentEvalTaskEntity.getId()));
        for(StudentEvalRecordEntity recordEntity : studentEvalRecordEntityList){

            SysUserEntity userEntity = sysUserService.getById(recordEntity.getUserId());
            SysDeptEntity deptEntity = sysDeptService.getById(userEntity.getDeptId());

            EvalResultEntity evalResultEntity = new EvalResultEntity();
            evalResultEntity.setTaskId(taskId);
            evalResultEntity.setUsername(userEntity.getUsername());
            evalResultEntity.setName(userEntity.getName());
            evalResultEntity.setDeptName(deptEntity.getName());
            evalResultEntity.setStudentEvalScore(recordEntity.getScore());

            resultEntityMap.put(recordEntity.getUserId(),evalResultEntity);
        }
        //设置同行评价结果
        for (ColleagueEvalTaskEntity colleagueEvalTask : colleagueEvalTasks) {
            List<ColleagueEvalRecordEntity> colleagueEvalRecordEntityList = colleagueEvalRecordService.list(
                    new QueryWrapper<ColleagueEvalRecordEntity>().eq("sub_task_id", colleagueEvalTask.getId()));
            for(ColleagueEvalRecordEntity recordEntity : colleagueEvalRecordEntityList){
                EvalResultEntity evalResultEntity = resultEntityMap.get(recordEntity.getUserId());
                evalResultEntity.setColleagueEvalScore(recordEntity.getScore());
                evalResultEntity.setColleagueEvalDetail(recordEntity.getDetail());
            }
        }
        //设置督导评价结果
        for (InspectorEvalTaskEntity inspectorEvalTask : inspectorEvalTasks) {
            List<InspectorEvalRecordEntity> inspectorEvalRecordEntityList = inspectorEvalRecordService.list(
                    new QueryWrapper<InspectorEvalRecordEntity>().eq("sub_task_id", inspectorEvalTask.getId())
            );
            for(InspectorEvalRecordEntity recordEntity : inspectorEvalRecordEntityList){
                EvalResultEntity evalResultEntity = resultEntityMap.get(recordEntity.getUserId());
                if(evalResultEntity.getInspectorEvalScore() != null){
                    Double score = (recordEntity.getScore() + evalResultEntity.getInspectorEvalScore()) / 2;
                    evalResultEntity.setInspectorEvalScore(score);
                    evalResultEntity.setInspectorEvalDetail(evalResultEntity.getInspectorEvalDetail()
                            + "#" + inspectorEvalTask.getUserId().toString() + "_" + recordEntity.getDetail());
                }else{
                    evalResultEntity.setInspectorEvalScore(recordEntity.getScore());
                    evalResultEntity.setInspectorEvalDetail(""+inspectorEvalTask.getUserId()+"_"+recordEntity.getDetail());
                }
            }
        }
        //设置其他评价结果
        List<OtherEvalRecordEntity> otherEvalRecordEntityList = otherEvalRecordService.list(
                new QueryWrapper<OtherEvalRecordEntity>().eq("sub_task_id", otherEvalTaskEntity.getId()));
        for(OtherEvalRecordEntity recordEntity : otherEvalRecordEntityList){
            EvalResultEntity evalResultEntity = resultEntityMap.get(recordEntity.getUserId());
            evalResultEntity.setOtherEvalScore(recordEntity.getScore());
        }
        //保存评价结果
        for(EvalResultEntity resultEntity : resultEntityMap.values()){
            evalResultService.save(resultEntity);
        }

        //对评价结果进行排名
        evalResultService.sortEvalResult(taskId);

        //修改评价状态为完成状态
        evalTask.setHeadcount(headcount);
        evalTask.setStatus(EvalTaskEntity.Status.COMPLETE.value());
        super.updateById(evalTask);
    }

    @Override
    public EvalTaskEntity.Status getTaskStatus(Long taskId) {
        EvalTaskEntity evalTask = super.getOne(
                new QueryWrapper<EvalTaskEntity>().select("status").eq("id", taskId));
        if (evalTask != null) {
            return EvalTaskEntity.Status.valueOf(evalTask.getStatus());
        }
        return null;
    }

    @Override
    public boolean updateTaskStatus(Long taskId, EvalTaskEntity.Status status) {
        EvalTaskEntity evalTask = super.getById(taskId);
        evalTask.setStatus(status.value());
        return super.updateById(evalTask);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Collection<? extends Serializable> idList) {

        //删除学生评价记录,删除学生任务
        List<StudentEvalTaskEntity> studentTasks = studentEvalTaskService.list(new QueryWrapper<StudentEvalTaskEntity>().in("task_id", idList));
        for(StudentEvalTaskEntity entity : studentTasks){
            studentEvalRecordService.remove(new QueryWrapper<StudentEvalRecordEntity>().eq("sub_task_id", entity.getTaskId()));
            studentEvalTaskService.removeById(entity.getTaskId());
        }

        //删除同行评价项目
        colleagueEvalTaskItemService.remove(new QueryWrapper<ColleagueEvalTaskItemEntity>().in("task_id", idList));

        //删除同行评价记录,删除同行任务
        List<ColleagueEvalTaskEntity> colleagueTasks = colleagueEvalTaskService.list(new QueryWrapper<ColleagueEvalTaskEntity>().in("task_id", idList));
       for(ColleagueEvalTaskEntity colleagueEvalTaskEntity : colleagueTasks){
           colleagueEvalRecordService.remove(new QueryWrapper<ColleagueEvalRecordEntity>().eq("sub_task_id", colleagueEvalTaskEntity.getId()));
           colleagueEvalTaskService.removeById(colleagueEvalTaskEntity.getId());
       }
        //删除督导评价项目
        inspectorEvalTaskItemService.remove(new QueryWrapper<InspectorEvalTaskItemEntity>().in("task_id", idList));

        //删除督导评价记录,删除督导任务
        List<InspectorEvalTaskEntity> inspectorTasks = inspectorEvalTaskService.list(new QueryWrapper<InspectorEvalTaskEntity>().in("task_id", idList));
        for(InspectorEvalTaskEntity inspectorEvalTaskEntity : inspectorTasks){
            inspectorEvalRecordService.remove(new QueryWrapper<InspectorEvalRecordEntity>().eq("sub_task_id", inspectorEvalTaskEntity.getId()));
            inspectorEvalTaskService.removeById(inspectorEvalTaskEntity.getId());
        }

        //删除其他评价记录,删除其他任务
        List<OtherEvalTaskEntity> otherTasks = otherEvalTaskService.list(new QueryWrapper<OtherEvalTaskEntity>().in("task_id", idList));
        for(OtherEvalTaskEntity entity : otherTasks){
            otherEvalRecordService.remove(new QueryWrapper<OtherEvalRecordEntity>().eq("sub_task_id", entity.getId()));
            otherEvalTaskService.removeById(entity.getId());
        }

        //删除评价结果
        evalResultService.remove(new QueryWrapper<EvalResultEntity>().in("task_id", idList));

        //删除评价任务
        super.removeByIds(idList);

    }

    @Override
    public EvalTaskEntity findById(Serializable id) {

        EvalTaskEntity evalTask = super.getById(id);

        if (evalTask != null) {
            SysDeptEntity sysDeptEntity = sysDeptService.getById(evalTask.getDeptId());
            evalTask.setDeptName(sysDeptEntity.getName());

            //学生评价
            StudentEvalTaskEntity studentEvalTaskEntity = studentEvalTaskService.getOne(
                    new QueryWrapper<StudentEvalTaskEntity>().eq("task_id", id));

            //查询学生评价进度
            Integer studentCount = studentEvalRecordService.count(
                    new QueryWrapper<StudentEvalRecordEntity>().eq("sub_task_id", studentEvalTaskEntity.getId()));
            studentEvalTaskEntity.setSchedule(studentCount);

            //vo添加学生评价
            evalTask.setStudentPercentage(studentEvalTaskEntity.getPercentage());
            evalTask.setStudentEvalTask(studentEvalTaskEntity);

            //同行评价项目
            List<ColleagueEvalTaskItemEntity> colleagueEvalTaskItems = colleagueEvalTaskItemService.list(
                    new QueryWrapper<ColleagueEvalTaskItemEntity>().eq("task_id", id));
            evalTask.setColleagueEvalTaskItems(colleagueEvalTaskItems);

            //同行评价详情
            List<ColleagueEvalTaskEntity> colleagueEvalTasks = colleagueEvalTaskService.list(
                    new QueryWrapper<ColleagueEvalTaskEntity>().eq("task_id", id));
            for (ColleagueEvalTaskEntity colleagueEvalTask : colleagueEvalTasks) {
                //查找系主任/同行姓名
                SysUserEntity taskUser = sysUserService.getById(colleagueEvalTask.getUserId());
                colleagueEvalTask.setUserName(taskUser.getName());
                //查找部门名称
                SysDeptEntity taskDept = sysDeptService.getById(colleagueEvalTask.getDeptId());
                colleagueEvalTask.setDeptName(taskDept.getName());
                //查询同行评价进度
                Integer colleagueCount = colleagueEvalRecordService.count(
                        new QueryWrapper<ColleagueEvalRecordEntity>().eq("sub_task_id", colleagueEvalTask.getId()));
                colleagueEvalTask.setSchedule(colleagueCount);
                //查询同行部门人数
                colleagueEvalTask.setHeadcount(sysUserService.countByDept(colleagueEvalTask.getDeptId()));
            }
            evalTask.setColleaguePercentage(colleagueEvalTasks.get(0).getPercentage());
            evalTask.setColleagueEvalTasks(colleagueEvalTasks);

            //督导评价项目
            List<InspectorEvalTaskItemEntity> inspectorEvalTaskItems = inspectorEvalTaskItemService.list(
                    new QueryWrapper<InspectorEvalTaskItemEntity>().eq("task_id", id));
            evalTask.setInspectorEvalTaskItems(inspectorEvalTaskItems);

            //督导评价详情
            List<InspectorEvalTaskEntity> inspectorEvalTasks = inspectorEvalTaskService.list(
                    new QueryWrapper<InspectorEvalTaskEntity>().eq("task_id", id));
            for (InspectorEvalTaskEntity inspectorEvalTask : inspectorEvalTasks) {
                //查找系主任/同行姓名
                SysUserEntity taskUser = sysUserService.getById(inspectorEvalTask.getUserId());
                inspectorEvalTask.setUserName(taskUser.getName());

                //查询督导评价进度
                Integer inspectorCount = inspectorEvalRecordService.count(
                        new QueryWrapper<InspectorEvalRecordEntity>().eq("sub_task_id", inspectorEvalTask.getId()));
                inspectorEvalTask.setSchedule(inspectorCount);
            }
            evalTask.setInspectorPercentage(inspectorEvalTasks.get(0).getPercentage());
            evalTask.setInspectorEvalTasks(inspectorEvalTasks);

            //其他评价
            OtherEvalTaskEntity otherEvalTaskEntity = otherEvalTaskService.getOne(
                    new QueryWrapper<OtherEvalTaskEntity>().eq("task_id", id));

            //查询其他评价进度
            Integer otherCount = otherEvalRecordService.count(
                    new QueryWrapper<OtherEvalRecordEntity>().eq("sub_task_id", otherEvalTaskEntity.getId()));
            otherEvalTaskEntity.setSchedule(otherCount);

            evalTask.setOtherPercentage(otherEvalTaskEntity.getPercentage());
            evalTask.setOtherEvalTask(otherEvalTaskEntity);

            //查找参评人数
            if(evalTask.getHeadcount() == null){
                evalTask.setHeadcount(sysUserService.countByDept(evalTask.getDeptId()));
            }


        }

        return evalTask;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(EvalTaskEntity entity) {
        //保存评价任务
        entity.setCreateTime(new Date());
        entity.setStatus(EvalTaskEntity.Status.NEW.value());
        super.save(entity);

        //保存学生评价任务
        StudentEvalTaskEntity studentEvalTaskEntity = entity.getStudentEvalTask();
        if (studentEvalTaskEntity == null) {
            studentEvalTaskEntity = new StudentEvalTaskEntity();
        }
        studentEvalTaskEntity.setTaskId(entity.getId());
        studentEvalTaskEntity.setPercentage(entity.getStudentPercentage());
        studentEvalTaskService.save(studentEvalTaskEntity);

        //保存同行评价项目
        for (ColleagueEvalTaskItemEntity colleagueEvalTaskItemEntity : entity.getColleagueEvalTaskItems()) {
            colleagueEvalTaskItemEntity.setTaskId(entity.getId());
            colleagueEvalTaskItemService.save(colleagueEvalTaskItemEntity);
        }

        //保存同行评价详情
        for (ColleagueEvalTaskEntity colleagueEvalTaskEntity : entity.getColleagueEvalTasks()) {
            colleagueEvalTaskEntity.setTaskId(entity.getId());
            colleagueEvalTaskEntity.setPercentage(entity.getColleaguePercentage());
            colleagueEvalTaskService.save(colleagueEvalTaskEntity);
        }

        //保存督导评价项目
        for (InspectorEvalTaskItemEntity inspectorEvalTaskItemEntity : entity.getInspectorEvalTaskItems()) {
            inspectorEvalTaskItemEntity.setTaskId(entity.getId());
            inspectorEvalTaskItemService.save(inspectorEvalTaskItemEntity);
        }

        //保存督导评价详情
        for (InspectorEvalTaskEntity inspectorEvalTaskEntity : entity.getInspectorEvalTasks()) {
            inspectorEvalTaskEntity.setTaskId(entity.getId());
            inspectorEvalTaskEntity.setPercentage(entity.getInspectorPercentage());
            inspectorEvalTaskService.save(inspectorEvalTaskEntity);
        }

        //保存其他评价
        OtherEvalTaskEntity otherEvalTaskEntity = entity.getOtherEvalTask();
        if (otherEvalTaskEntity == null) {
            otherEvalTaskEntity = new OtherEvalTaskEntity();
        }
        otherEvalTaskEntity.setTaskId(entity.getId());
        otherEvalTaskEntity.setPercentage(entity.getOtherPercentage());
        otherEvalTaskService.save(otherEvalTaskEntity);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean modifyById(EvalTaskEntity viewTask) {
        EvalTaskEntity dataTask = super.getById(viewTask.getId());

        //学生评价
        StudentEvalTaskEntity dataStudentEvalTask = dataTask.getStudentEvalTask();
        if (!dataStudentEvalTask.getPercentage().equals(viewTask.getStudentPercentage())) {
            dataStudentEvalTask.setPercentage(viewTask.getStudentPercentage());
            studentEvalTaskService.updateById(dataStudentEvalTask);
        }

        //同行评价项目
        updateColleagueSubTaskItems(viewTask, dataTask);

        //同行评价详情
        updateColleagueSubTasks(viewTask, dataTask);

        //督导评价项目
        updateInspectorSubTaskItems(viewTask, dataTask);

        //督导评价详情
        updateInspectorSubTasks(viewTask, dataTask);

        //其他评价
        OtherEvalTaskEntity otherEvalTaskEntity = dataTask.getOtherEvalTask();
        if (!otherEvalTaskEntity.getPercentage().equals(viewTask.getOtherPercentage())) {
            otherEvalTaskEntity.setPercentage(viewTask.getOtherPercentage());
            otherEvalTaskService.updateById(otherEvalTaskEntity);
        }

        //评价任务
        super.updateById(viewTask);

        return true;
    }

    private void updateInspectorSubTasks(EvalTaskEntity viewTask, EvalTaskEntity dataTask) {
        List<Long> removeInspectorTaskIds = new ArrayList<>();
        Map<Long, InspectorEvalTaskEntity> colleagueEvalTaskMap = new HashMap<>();
        for (InspectorEvalTaskEntity viewSubTask : viewTask.getInspectorEvalTasks()) {
            if (viewSubTask.getId() == null) {
                viewSubTask.setTaskId(viewTask.getId());
                viewSubTask.setPercentage(viewTask.getInspectorPercentage());
                inspectorEvalTaskService.save(viewSubTask);
            } else {
                colleagueEvalTaskMap.put(viewSubTask.getId(), viewSubTask);
            }
        }
        for (InspectorEvalTaskEntity dataSubTask : dataTask.getInspectorEvalTasks()) {
            InspectorEvalTaskEntity viewSubTask = colleagueEvalTaskMap.get(dataSubTask.getId());
            if (viewSubTask != null) {
                viewSubTask.setPercentage(viewTask.getInspectorPercentage());
                inspectorEvalTaskService.updateById(viewSubTask);
            } else {
                removeInspectorTaskIds.add(dataSubTask.getId());
            }
        }
        if (removeInspectorTaskIds.size() > 0) {
            inspectorEvalTaskService.removeByIds(removeInspectorTaskIds);
        }
    }

    private void updateInspectorSubTaskItems(EvalTaskEntity viewTask, EvalTaskEntity dataTask) {
        List<Long> removeInspectorTaskItemIds = new ArrayList<>();
        Map<Long, InspectorEvalTaskItemEntity> colleagueEvalTaskItemMap = new HashMap<>();
        for (InspectorEvalTaskItemEntity viewTaskItem : viewTask.getInspectorEvalTaskItems()) {
            if (viewTaskItem.getId() == null) {
                viewTaskItem.setTaskId(viewTask.getId());
                inspectorEvalTaskItemService.save(viewTaskItem);
            } else {
                colleagueEvalTaskItemMap.put(viewTaskItem.getId(), viewTaskItem);
            }
        }
        for (InspectorEvalTaskItemEntity dataTaskItem : dataTask.getInspectorEvalTaskItems()) {
            InspectorEvalTaskItemEntity viewTaskItem = colleagueEvalTaskItemMap.get(dataTaskItem.getId());
            if (viewTaskItem != null) {
                inspectorEvalTaskItemService.updateById(viewTaskItem);
            } else {
                removeInspectorTaskItemIds.add(dataTaskItem.getId());
            }
        }
        if (removeInspectorTaskItemIds.size() > 0) {
            inspectorEvalTaskItemService.removeByIds(removeInspectorTaskItemIds);
        }
    }

    private void updateColleagueSubTasks(EvalTaskEntity viewTask, EvalTaskEntity dataTask) {
        List<Long> removeColleagueTaskIds = new ArrayList<>();
        Map<Long, ColleagueEvalTaskEntity> colleagueEvalTaskMap = new HashMap<>();
        for (ColleagueEvalTaskEntity viewSubTask : viewTask.getColleagueEvalTasks()) {
            if (viewSubTask.getId() == null) {
                viewSubTask.setTaskId(viewTask.getId());
                viewSubTask.setPercentage(viewTask.getColleaguePercentage());
                colleagueEvalTaskService.save(viewSubTask);
            } else {
                colleagueEvalTaskMap.put(viewSubTask.getId(), viewSubTask);
            }
        }
        for (ColleagueEvalTaskEntity dataSubTask : dataTask.getColleagueEvalTasks()) {
            ColleagueEvalTaskEntity viewSubTask = colleagueEvalTaskMap.get(dataSubTask.getId());
            if (viewSubTask != null) {
                viewSubTask.setPercentage(viewTask.getColleaguePercentage());
                colleagueEvalTaskService.updateById(viewSubTask);
            } else {
                removeColleagueTaskIds.add(dataSubTask.getId());
            }
        }
        if (removeColleagueTaskIds.size() > 0) {
            colleagueEvalTaskService.removeByIds(removeColleagueTaskIds);
        }
    }

    private void updateColleagueSubTaskItems(EvalTaskEntity viewTask, EvalTaskEntity dataTask) {
        List<Long> removeColleagueTaskItemIds = new ArrayList<>();
        Map<Long, ColleagueEvalTaskItemEntity> colleagueEvalTaskItemMap = new HashMap<>();
        for (ColleagueEvalTaskItemEntity viewTaskItem : viewTask.getColleagueEvalTaskItems()) {
            if (viewTaskItem.getId() == null) {
                viewTaskItem.setTaskId(viewTask.getId());
                colleagueEvalTaskItemService.save(viewTaskItem);
            } else {
                colleagueEvalTaskItemMap.put(viewTaskItem.getId(), viewTaskItem);
            }
        }
        for (ColleagueEvalTaskItemEntity dataTaskItem : dataTask.getColleagueEvalTaskItems()) {
            ColleagueEvalTaskItemEntity viewTaskItem = colleagueEvalTaskItemMap.get(dataTaskItem.getId());
            if (viewTaskItem != null) {
                colleagueEvalTaskItemService.updateById(viewTaskItem);
            } else {
                removeColleagueTaskItemIds.add(dataTaskItem.getId());
            }
        }
        if (removeColleagueTaskItemIds.size() > 0) {
            colleagueEvalTaskItemService.removeByIds(removeColleagueTaskItemIds);
        }
    }
}
