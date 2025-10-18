package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "场站评价列表返回实体")
public class EvaluationListResponse {

    @ApiModelProperty(value = "评价标签值")
    private String value;

    @ApiModelProperty(value = "评价标签")
    private String label;

    @ApiModelProperty(value = "是否已选")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long qty = 0L;
}
