package com.ambow.lyu.modules.eval.entity;

import com.ambow.lyu.common.validator.group.AddGroup;
import com.ambow.lyu.common.validator.group.UpdateGroup;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 评分标准基础项目
 *
 * @author wonderwhyy
 * @email wonderwhyy@163.com
 * @date 2019-05-10 16:32:25
 */
@Data
@TableName("tb_eval_base_item")
public class EvalBaseItemEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String name;
    /**
     * 百分比
     */
    @NotNull(message = "百分比不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Max(message = "百分比最大不能超多100", value = 100, groups = {AddGroup.class, UpdateGroup.class})
    @Min(message = "百分比最小不能超过0", value = 0, groups = {AddGroup.class, UpdateGroup.class})
    private Integer percentage;
    /**
     * 备注
     */
    private String remark;

}
