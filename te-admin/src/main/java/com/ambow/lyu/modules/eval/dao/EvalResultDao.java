package com.ambow.lyu.modules.eval.dao;

import com.ambow.lyu.modules.eval.entity.EvalResultEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 评价结果
 * 
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-06-24 14:54:15
 */
@Mapper
public interface EvalResultDao extends BaseMapper<EvalResultEntity> {

    Double getAverageTotal(Long taskId,String deptName);
    Double getAverageStudent(Long taskId,String deptName);
    Double getAverageColleague(Long taskId,String deptName);
    Double getAverageInspector(Long taskId,String deptName);
    Double getAverageOther(Long taskId,String deptName);

    Double getMaxTotal(Long taskId,String deptName);
    Double getMaxStudent(Long taskId,String deptName);
    Double getMaxColleague(Long taskId,String deptName);
    Double getMaxInspector(Long taskId,String deptName);
    Double getMaxOther(Long taskId,String deptName);

    Double getMinTotal(Long taskId,String deptName);
    Double getMinStudent(Long taskId,String deptName);
    Double getMinColleague(Long taskId,String deptName);
    Double getMinInspector(Long taskId,String deptName);
    Double getMinOther(Long taskId,String deptName);

    List<String> getDeptList(Long taskId);

}
