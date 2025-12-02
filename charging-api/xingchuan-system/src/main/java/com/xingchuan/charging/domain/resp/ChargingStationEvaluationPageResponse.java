package com.xingchuan.charging.domain.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "场站评价列表返回实体")
public class ChargingStationEvaluationPageResponse {

    @ApiModelProperty(value = "场站ID")
    private Long chargingStationId;

    @ApiModelProperty(value = "场站名称")
    private String chargingStationName;

    @ApiModelProperty(value = "充电订单ID")
    private Long orderId;

    @ApiModelProperty(value = "评价用户id")
    private Long appUserId;

    @ApiModelProperty(value = "评价用户")
    private String appUser;

    @ApiModelProperty(value = "评价标签")
    private String rating;

    @ApiModelProperty(value = "评价标签_字符")
    private String ratingString;
}
