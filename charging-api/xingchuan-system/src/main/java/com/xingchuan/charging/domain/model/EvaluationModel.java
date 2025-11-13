package com.xingchuan.charging.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "场站评价各类型统计模型")
public class EvaluationModel {

    @ApiModelProperty(value = "评价标签值")
    private String value;

    @ApiModelProperty(value = "是否已选")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long qty = 0L;
}
