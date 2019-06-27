package com.ambow.lyu.modules.eval.service.impl;

import com.ambow.lyu.common.dto.EvalResultSummary;
import com.ambow.lyu.common.dto.EvalResultSummaryDeptDetail;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.utils.Query;
import com.ambow.lyu.modules.eval.dao.EvalResultDao;
import com.ambow.lyu.modules.eval.entity.*;
import com.ambow.lyu.modules.eval.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        IPage<EvalResultEntity> page = this.page(
                new Query<EvalResultEntity>().getPage(params),
                genarateQueryWrapper(params)
        );

        return new PageUtils(page);
    }

    @Override
    public List<EvalResultEntity> queryList(Map<String, Object> params) {
        return super.list(genarateQueryWrapper(params));
    }

    private QueryWrapper<EvalResultEntity> genarateQueryWrapper(Map<String, Object> params){
        Long taskId = (Long) params.get("task_id");
        String name = (String) params.get("name");
        String ranking1 = (String) params.get("ranking1");
        String ranking2 = (String) params.get("ranking2");
        String selectedDept = (String) params.get("selectedDept");
        String selectedRaking = (String) params.get("selectedRaking");

        Long ranking1Long = null;
        Long ranking2Long = null;
        if(StringUtils.isNotBlank(ranking1)&&StringUtils.isNotBlank(ranking2)){
            ranking1Long = Long.valueOf(ranking1);
            ranking2Long = Long.valueOf(ranking2);
            if(ranking1Long > ranking2Long){
                Long temp = ranking1Long;
                ranking1Long = ranking2Long;
                ranking2Long = temp;

            }
        }

        return new QueryWrapper<EvalResultEntity>()
                .eq(taskId != null,"task_id",taskId)
                .like(StringUtils.isNotBlank(name),"name",name)
                .between(ranking1Long != null,
                        "ranking",ranking1Long,ranking2Long)
                .eq(StringUtils.isNotBlank(selectedDept),"dept_name",selectedDept)
                .eq(StringUtils.isNotBlank(selectedRaking),"raking",selectedRaking);
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

        //设置排名，分数相同排名一样
        for(int i = 0 ; i < evalResultEntityList.size() ; ++i){
            EvalResultEntity evalResultEntity = evalResultEntityList.get(i);
            if(i > 0 && evalResultEntity.getAccountScore().equals(evalResultEntityList.get(i-1).getAccountScore())){
                evalResultEntity.setRanking(evalResultEntityList.get(i-1).getRanking());
            }else {
                evalResultEntity.setRanking((long) (i+1));
            }
        }

        //30%为优秀、30%~70%为良好、后30%为合格、小于60分为不合格
        long goodRanking = Math.round(evalResultEntityList.size() * 30.0 / 100 );
        long okRanking = Math.round(evalResultEntityList.size() * 70.0 / 100);
        for (EvalResultEntity evalResultEntity : evalResultEntityList) {
            if (evalResultEntity.getAccountScore() < 60) {
                evalResultEntity.setRating("不合格");
            } else if (evalResultEntity.getRanking() <= goodRanking) {
                evalResultEntity.setRating("优秀");
            } else if (evalResultEntity.getRanking() <= okRanking) {
                evalResultEntity.setRating("良好");
            } else {
                evalResultEntity.setRating("合格");
            }
            evalResultEntity.setUpdateTime(new Date());
            super.updateById(evalResultEntity);
        }
    }

    @Override
    public EvalResultSummary querySummary(Long taskId) {

        double averageTotal = baseMapper.getAverageTotal(taskId,null);
        double averageStudent = baseMapper.getAverageStudent(taskId,null);
        double averageColleague = baseMapper.getAverageColleague(taskId,null);
        double averageInspector = baseMapper.getAverageInspector(taskId,null);
        double averageOther = baseMapper.getAverageOther(taskId,null);
        double maxTotal = baseMapper.getMaxTotal(taskId,null);
        double maxStudent = baseMapper.getMaxStudent(taskId,null);
        double maxColleague = baseMapper.getMaxColleague(taskId,null);
        double maxInspector = baseMapper.getMaxInspector(taskId,null);
        double maxOther = baseMapper.getMaxOther(taskId,null);
        double minTotal = baseMapper.getMinTotal(taskId,null);
        double minStudent = baseMapper.getMinStudent(taskId,null);
        double minColleague = baseMapper.getMinColleague(taskId,null);
        double minInspector = baseMapper.getMinInspector(taskId,null);
        double minOther = baseMapper.getMinOther(taskId,null);

        long goodCount = super.count(new QueryWrapper<EvalResultEntity>().eq("task_id",taskId).eq("rating","优秀"));
        long fineCount = super.count(new QueryWrapper<EvalResultEntity>().eq("task_id",taskId).eq("rating","良好"));
        long passCount = super.count(new QueryWrapper<EvalResultEntity>().eq("task_id",taskId).eq("rating","合格"));
        long failCount = super.count(new QueryWrapper<EvalResultEntity>().eq("task_id",taskId).eq("rating","不合格"));


        List<String> deptList = baseMapper.getDeptList(taskId);

        List<Double> deptAverageStudentList = new ArrayList<>(deptList.size());
        List<Double> deptAverageColleagueList = new ArrayList<>(deptList.size());
        List<Double> deptAverageInspectorList = new ArrayList<>(deptList.size());
        List<Double> deptAverageOtherList = new ArrayList<>(deptList.size());
        List<Double> deptAverageTotalList = new ArrayList<>(deptList.size());
        for(int i = 0 ; i < deptList.size() ; ++i){
            String deptName = deptList.get(i);
            deptAverageTotalList.add(baseMapper.getAverageTotal(taskId,deptName));
            deptAverageStudentList.add(baseMapper.getAverageStudent(taskId,deptName));
            deptAverageColleagueList.add(baseMapper.getAverageColleague(taskId,deptName));
            deptAverageInspectorList.add(baseMapper.getAverageInspector(taskId,deptName));
            deptAverageOtherList.add(baseMapper.getAverageOther(taskId,deptName));
        }

        List<EvalResultSummaryDeptDetail> deptDetails = new ArrayList<>();
        for(String deptName : deptList){
            List<EvalResultEntity> evalResultEntityList = super.list(new QueryWrapper<EvalResultEntity>()
                    .eq("task_id",taskId).eq("dept_name",deptName));

            List<String> personList = new ArrayList<>(evalResultEntityList.size());
            List<Double> personStudentList = new ArrayList<>(evalResultEntityList.size());
            List<Double> personColleagueList = new ArrayList<>(evalResultEntityList.size());
            List<Double> personInspectorList = new ArrayList<>(evalResultEntityList.size());
            List<Double> personOtherList = new ArrayList<>(evalResultEntityList.size());
            List<Double> personTotalList = new ArrayList<>(evalResultEntityList.size());
            for(int i = 0 ; i < evalResultEntityList.size() ; ++i){
                EvalResultEntity evalResult = evalResultEntityList.get(i);
                personList.add(evalResult.getName());
                personTotalList.add(evalResult.getAccountScore());
                personStudentList.add(evalResult.getStudentEvalScore());
                personColleagueList.add(evalResult.getColleagueEvalScore());
                personInspectorList.add(evalResult.getInspectorEvalScore());
                personOtherList.add(evalResult.getOtherEvalScore());
            }

            EvalResultSummaryDeptDetail deptDetail = new EvalResultSummaryDeptDetail();
            deptDetail.setDeptName(deptName);
            deptDetail.setPersonList(personList);
            deptDetail.setPersonStudentList(personStudentList);
            deptDetail.setPersonColleagueList(personColleagueList);
            deptDetail.setPersonInspectorList(personInspectorList);
            deptDetail.setPersonOtherList(personOtherList);
            deptDetail.setPersonTotalList(personTotalList);
            deptDetails.add(deptDetail);
        }

        //所有人得分情况
        List<EvalResultEntity> evalResultEntityList = super.list(new QueryWrapper<EvalResultEntity>()
                .eq("task_id",taskId));
        List<String> personList = new ArrayList<>(evalResultEntityList.size());
        List<Double> personStudentList = new ArrayList<>(evalResultEntityList.size());
        List<Double> personColleagueList = new ArrayList<>(evalResultEntityList.size());
        List<Double> personInspectorList = new ArrayList<>(evalResultEntityList.size());
        List<Double> personOtherList = new ArrayList<>(evalResultEntityList.size());
        List<Double> personTotalList = new ArrayList<>(evalResultEntityList.size());
        for(int i = 0 ; i < evalResultEntityList.size() ; ++i){
            EvalResultEntity evalResult = evalResultEntityList.get(i);
            personList.add(evalResult.getName());
            personTotalList.add(evalResult.getAccountScore());
            personStudentList.add(evalResult.getStudentEvalScore());
            personColleagueList.add(evalResult.getColleagueEvalScore());
            personInspectorList.add(evalResult.getInspectorEvalScore());
            personOtherList.add(evalResult.getOtherEvalScore());
        }




        EvalResultSummary summary = new EvalResultSummary();
        summary.setAverageTotal(averageTotal);
        summary.setAverageStudent(averageStudent);
        summary.setAverageColleague(averageColleague);
        summary.setAverageInspector(averageInspector);
        summary.setAverageOther(averageOther);

        summary.setMaxTotal(maxTotal);
        summary.setMaxStudent(maxStudent);
        summary.setMaxColleague(maxColleague);
        summary.setMaxInspector(maxInspector);
        summary.setMaxOther(maxOther);

        summary.setMinTotal(minTotal);
        summary.setMinStudent(minStudent);
        summary.setMinColleague(minColleague);
        summary.setMinInspector(minInspector);
        summary.setMinOther(minOther);

        summary.setGoodCount(goodCount);
        summary.setFineCount(fineCount);
        summary.setPassCount(passCount);
        summary.setFailCount(failCount);

        summary.setDeptList(deptList);
        summary.setDeptAverageTotalList(deptAverageTotalList);
        summary.setDeptAverageStudentList(deptAverageStudentList);
        summary.setDeptAverageColleagueList(deptAverageColleagueList);
        summary.setDeptAverageInspectorList(deptAverageInspectorList);
        summary.setDeptAverageOtherList(deptAverageOtherList);

        summary.setPersonList(personList);
        summary.setPersonTotalList(personTotalList);
        summary.setPersonStudentList(personStudentList);
        summary.setPersonColleagueList(personColleagueList);
        summary.setPersonInspectorList(personInspectorList);
        summary.setPersonOtherList(personOtherList);

        summary.setDeptDetails(deptDetails);

        return summary;
    }

}
