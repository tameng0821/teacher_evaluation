package com.ambow.lyu.modules.eval.controller;

import com.ambow.lyu.common.utils.Constant;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.eval.dto.EvalTaskItemScoreDto;
import com.ambow.lyu.modules.eval.entity.ColleagueEvalRecordEntity;
import com.ambow.lyu.modules.eval.service.ColleagueEvalRecordService;
import com.ambow.lyu.modules.sys.service.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * 同行评价记录
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-06-05 09:17:11
 */
@RestController
@RequestMapping("eval/colleagueevalrecord")
public class ColleagueEvalRecordController {
    @Autowired
    private ColleagueEvalRecordService colleagueEvalRecordService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 列表
     */
    @RequestMapping("/list/{subTaskId}")
    @RequiresPermissions("eval:colleagueevaltask:eval")
    public Response list(@PathVariable("subTaskId") Long subTaskId,@RequestParam Map<String, Object> params){

        params.put(Constant.SUB_TASK_ID, subTaskId);

        PageUtils page = colleagueEvalRecordService.queryPage(params);

        return Response.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("eval:colleagueevaltask:eval")
    public Response info(@PathVariable("id") Long id){
        ColleagueEvalRecordEntity colleagueEvalRecord = colleagueEvalRecordService.getById(id);

        //姓名
        String name = sysUserService.getById(colleagueEvalRecord.getUserId()).getName();
        colleagueEvalRecord.setUserName(name);

        //分数细节
        List<EvalTaskItemScoreDto> list = EvalTaskItemScoreDto.string2list(colleagueEvalRecord.getDetail());
        colleagueEvalRecord.setEvalItemResults(list);

        return Response.ok().put("colleagueEvalRecord", colleagueEvalRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("eval:colleagueevaltask:eval")
    public Response save(@RequestBody ColleagueEvalRecordEntity colleagueEvalRecord){

        ValidatorUtils.validateEntity(colleagueEvalRecord);

        colleagueEvalRecord.setDetail(EvalTaskItemScoreDto.list2string(colleagueEvalRecord.getEvalItemResults()));
        colleagueEvalRecord.setScore(EvalTaskItemScoreDto.calculateScore(colleagueEvalRecord.getEvalItemResults()));
        colleagueEvalRecord.setUpdateTime(new Date());

        colleagueEvalRecordService.save(colleagueEvalRecord);

        return Response.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("eval:colleagueevaltask:eval")
    public Response update(@RequestBody ColleagueEvalRecordEntity colleagueEvalRecord){
        ValidatorUtils.validateEntity(colleagueEvalRecord);

        ColleagueEvalRecordEntity dbData = colleagueEvalRecordService.getById(colleagueEvalRecord.getId());
        dbData.setDetail(EvalTaskItemScoreDto.list2string(colleagueEvalRecord.getEvalItemResults()));
        dbData.setScore(EvalTaskItemScoreDto.calculateScore(colleagueEvalRecord.getEvalItemResults()));
        dbData.setUpdateTime(new Date());

        colleagueEvalRecordService.updateById(dbData);
        
        return Response.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("eval:colleagueevaltask:eval")
    public Response delete(@RequestBody Long[] ids){
        colleagueEvalRecordService.removeByIds(Arrays.asList(ids));

        return Response.ok();
    }

}
