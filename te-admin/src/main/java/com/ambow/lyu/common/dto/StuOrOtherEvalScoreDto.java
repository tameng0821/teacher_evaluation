package com.ambow.lyu.common.dto;

import lombok.Data;

/**
 * 学生或其他评价DTO
 * @author woondeewyy
 * @date 2019/6/20
 */
@Data
public class StuOrOtherEvalScoreDto {
    /**
     * 用户名/工号
     */
    private String username;
    /**
     * 姓名
     */
    private String name;
    /**
     * 成绩
     */
    private String score;
    /**
     * 分数
     */
    private String reason;

}
