package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.EvalResultDao;
import com.ambow.lyu.modules.eval.entity.*;
import com.ambow.lyu.modules.eval.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("evalResultService")
public class EvalResultServiceImpl extends ServiceImpl<EvalResultDao, EvalResultEntity> implements EvalResultService {

    @Autowired
    private StudentEvalTaskService studentEvalTaskService;
    @Autowired
    private ColleagueEvalTaskService colleagueEvalTaskService;
    @Autowired
    private InspectorEvalTaskService inspectorEvalTaskService;
    @Autowired
    private OtherEvalTaskService otherEvalTaskService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        Long taskId = (Long) params.get("task_id");

        IPage<EvalResultEntity> page = this.page(
                new Query<EvalResultEntity>().getPage(params),
                new QueryWrapper<EvalResultEntity>().eq(taskId != null,"task_id",taskId)
        );

        return new PageUtils(page);
    }

    @Override
    public void sortEvalResult(Long taskId) {
        //查找评分标准
        StudentEvalTaskEntity studentEvalTask = studentEvalTaskService.getOne(
                new QueryWrapper<StudentEvalTaskEntity>().eq("task_id", taskId));
        ColleagueEvalTaskEntity colleagueEvalTask = colleagueEvalTaskService.list(
                new QueryWrapper<ColleagueEvalTaskEntity>().eq("task_id", taskId)).get(0);
        InspectorEvalTaskEntity inspectorEvalTask = inspectorEvalTaskService.list(
                new QueryWrapper<InspectorEvalTaskEntity>().eq("task_id", taskId)).get(0);
        OtherEvalTaskEntity otherEvalTask = otherEvalTaskService.getOne(
                new QueryWrapper<OtherEvalTaskEntity>().eq("task_id", taskId));
        //计算总分
        List<EvalResultEntity> evalResultEntityList = super.list(new QueryWrapper<EvalResultEntity>().eq("task_id",taskId));
        for(EvalResultEntity evalResultEntity : evalResultEntityList){
            Double accountScore = evalResultEntity.getStudentEvalScore()*studentEvalTask.getPercentage()/100 +
                    evalResultEntity.getColleagueEvalScore()*colleagueEvalTask.getPercentage()/100 +
                    evalResultEntity.getInspectorEvalScore()*inspectorEvalTask.getPercentage()/100 +
                    evalResultEntity.getOtherEvalScore()*otherEvalTask.getPercentage()/100;
            evalResultEntity.setAccountScore(accountScore);
        }
        //按照成绩降序排序
        evalResultEntityList.sort((EvalResultEntity e1,EvalResultEntity e2) -> e2.getAccountScore().compareTo(e1.getAccountScore()));
        //30%为优秀、30%~70%为良好、后30%为合格、小于60分为不合格
        int goodLine = evalResultEntityList.size() * 30 / 100;
        int okLine = evalResultEntityList.size() * 70 / 100;
        for(int i = 0 ; i < evalResultEntityList.size() ; ++i){
            EvalResultEntity evalResultEntity = evalResultEntityList.get(i);
            if(evalResultEntity.getAccountScore() < 60){
                evalResultEntity.setRating("不合格");
            }else if( i <= goodLine){
                evalResultEntity.setRating("优秀");
            }else if(i <= okLine){
                evalResultEntity.setRating("良好");
            }else {
                evalResultEntity.setRating("合格");
            }
            evalResultEntity.setRanking((long) (i+1));
            evalResultEntity.setUpdateTime(new Date());
            super.updateById(evalResultEntity);
        }
    }

}
