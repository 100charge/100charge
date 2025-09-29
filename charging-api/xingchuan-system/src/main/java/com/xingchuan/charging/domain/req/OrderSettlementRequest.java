package com.xingchuan.charging.domain.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 订单支付 请求对象
 */
@Data
@ApiModel(value = "订单支付 请求对象")
public class OrderSettlementRequest {
    /**
     * 订单id
     */
    @NotBlank(message = "订单不能为空")
    private String tradeNo;
    /**
     * 使用优惠券id
     */
    @ApiModelProperty(value = "使用优惠券id", name = "使用优惠券id")
    private Long promotionId;

}
