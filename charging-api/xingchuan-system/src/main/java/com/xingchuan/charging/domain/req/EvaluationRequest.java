package com.xingchuan.charging.domain.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value = "场站评价请求实体")
public class EvaluationRequest {

    @ApiModelProperty(value = "场站id")
    @NotNull(message = "场站id不能为空!")
    private Long stationId;

    @ApiModelProperty(value = "订单id")
    @NotNull(message = "订单id不能为空!")
    private Long orderId;

    @ApiModelProperty(value = "评价标签列表")
    @NotNull(message = "请至少选择一个标签!")
    private List<String> labelList;
}
