package com.xingchuan.charging.domain.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 计费规则对象 rules
 *
 * @author ruoyi
 */
@Data
@ApiModel(value = "计费规则更新请求实体类")
public class RulesUpdateRequest {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空!")
    @ApiModelProperty(value = "计费规则id")
    private Long id;

    /**
     * 所属运营商
     */
    @ApiModelProperty(value = "所属运营商")
    @NotNull(message = "所属运营商不能为空!")
    private Long tenantId;

    /**
     * 规则名称
     */
    @NotBlank(message = "规则名称不能为空!")
    @ApiModelProperty(value = "规则名称")
    private String name;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
