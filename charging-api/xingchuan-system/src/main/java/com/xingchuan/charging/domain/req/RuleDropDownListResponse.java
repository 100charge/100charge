package com.xingchuan.charging.domain.req;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "计费规则下拉列表")
public class RuleDropDownListResponse {

    /**
     * 主键
     */
    @ApiModelProperty(value = "计费规则id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称")
    private String name;
}
