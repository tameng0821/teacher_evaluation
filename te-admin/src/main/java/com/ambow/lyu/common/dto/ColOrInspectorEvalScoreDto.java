package com.ambow.lyu.common.dto;

import lombok.Data;

import java.util.List;

/**
 * 同行或督导评价DTO
 * @author woondeewyy
 * @date 2019/6/20
 */
@Data
public class ColOrInspectorEvalScoreDto {

    /**
     * 用户名/工号
     */
    private String username;
    /**
     * 姓名
     */
    private String name;

    /**
     * 得分详情
     */
    private List<EvalTaskItemScoreDto> details;

    /**
     * 成绩
     */
    private String score;
    /**
     * 分数
     */
    private String reason;

}
