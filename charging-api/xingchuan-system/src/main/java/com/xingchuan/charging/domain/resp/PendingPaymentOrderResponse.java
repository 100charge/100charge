package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.charging.enums.OrderPayStatusEnum;
import com.xingchuan.charging.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "获取用户待支付订单")
public class PendingPaymentOrderResponse {

    /**
     * id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 订单号（唯一）
     */
    @ApiModelProperty(value = "订单号")
    private String tradeNo;

    /**
     * 订单状态：0:未开始；1:充电中；2:充电结束（未拔枪）；3:充电结束；4:异常
     */
    @ApiModelProperty(value = "订单状态：0:未开始；1:充电中；2:充电结束（未拔枪）；3:充电结束；4:异常")
    private Integer orderState;

    /**
     * 订单支付状态：0:未支付；1:支付成功；2:支付失败
     */
    @ApiModelProperty(value = "订单支付状态：0:未支付；1:支付成功；2:支付失败")
    private Integer payStatus;

    /**
     * 订单状态：0:未开始；1:充电中；2:充电结束（未拔枪）；3:充电结束；4:异常
     */
    @ApiModelProperty(value = "订单状态描述")
    private String orderStateDesc;

    /**
     * 订单支付状态：0:未支付；1:支付成功；2:支付失败
     */
    @ApiModelProperty(value = "订单支付状态描述")
    private String payStatusDesc;

    public String getOrderStateDesc() {
        return orderStateDesc = OrderStatusEnum.getDescByCode(orderState);
    }

    public String getPayStatusDesc() {
        return payStatusDesc = OrderPayStatusEnum.getDescByCode(payStatus);
    }
}
