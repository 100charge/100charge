package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "小程序充电订单详情信息")
public class MiniOrderDetailResponse {

    /**
     * 订单号（唯一）
     */
    @ApiModelProperty(value = "订单号")
    private String tradeNo;

    /**
     * 场站id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "场站Id")
    private Long stationId;

    /**
     * 场站名称
     */
    @ApiModelProperty(value = "场站名称")
    private String stationName;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态")
    private String orderState;

    /**
     * 桩编号
     */
    @ApiModelProperty(value = "桩编号")
    private String deviceNo;

    /**
     * 枪编号
     */
    @ApiModelProperty(value = "枪编号")
    private String gunsNo;

    /**
     * 运营商ID
     */
    @ApiModelProperty(value = "运营商ID")
    private String operatorId;
}
