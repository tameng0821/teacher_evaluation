package com.ambow.lyu.modules.eval.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 *  评价项目分数
 *
 * @author woondeewyy
 * @date 2019/6/12
 */
@Data
public class EvalTaskItemScoreDto {

    /**
     * 项目ID
     */
    @NotNull(message = "评价项目不能为空")
    private Long id;
    /**
     * 名称
     */
    @NotBlank(message = "评价项目不能为空")
    private String name;
    /**
     * 占比
     */
    @NotNull(message = "评价项目不能为空")
    private Integer percentage;
    /**
     * 得分
     */
    @NotNull(message = "评价项目得分不能为空")
    private Double score;

    @Override
    public String toString(){
        return ""+id+":"+name+":"+percentage+":"+score;
    }

    public static EvalTaskItemScoreDto fromString(String str){

        String[] strArray = str.split(":");

        if(strArray.length != 4){
            return null;
        }

        EvalTaskItemScoreDto dto = new EvalTaskItemScoreDto();
        dto.setId(Long.valueOf(strArray[0]));
        dto.setName(strArray[1]);
        dto.setPercentage(Integer.valueOf(strArray[2]));
        dto.setScore(Double.valueOf(strArray[3]));

        return dto;

    }

    public static double  calculateScore(List<EvalTaskItemScoreDto> list){
        double result = 0.0;
        for(EvalTaskItemScoreDto dto : list){
            result += (dto.getScore()*dto.getPercentage());
        }
        return result/100;
    }

    public static String list2string(List<EvalTaskItemScoreDto> list){
        StringBuilder sb = new StringBuilder();
        for(EvalTaskItemScoreDto dto : list){
            sb.append(dto.toString());
            sb.append(",");
        }
        if(sb.length() > 0){
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }

    public static List<EvalTaskItemScoreDto> string2list(String str){
        String[] strings = str.split(",");
        List<EvalTaskItemScoreDto> result = new ArrayList<>();
        for(String s : strings){
            EvalTaskItemScoreDto dto = fromString(s);
            if(dto != null){
                result.add(dto);
            }
        }
        return result;
    }


}
