package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.common.config.BigDecimalSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class OrderStatisticsResponse {

    @ApiModelProperty(value = "订单量")
    private Integer orderCount = 0;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "订单总额")
    private BigDecimal orderTotalAmount;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "电费")
    private BigDecimal electricityFee;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "服务费")
    private BigDecimal serviceFee;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "第三方订单总额")
    private BigDecimal thirdPartyOrderTotalAmount;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "第三方订单电费")
    private BigDecimal thirdPartyElectricityFee;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "第三方订单服务费")
    private BigDecimal thirdPartyServiceFee;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "停车费")
    private BigDecimal parkingFee;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "超时占用费")
    private BigDecimal occupancyFee;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "充电量")
    private BigDecimal chargingCapacity;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "充电时长")
    private String chargingDuration = "0";


    public void setChargingCapacity(BigDecimal chargingCapacity) {
        if (ObjectUtils.isEmpty(chargingCapacity)) {
            chargingCapacity = BigDecimal.ZERO;
        }
        this.chargingCapacity = chargingCapacity.setScale(3, RoundingMode.HALF_UP);
    }

    public BigDecimal getThirdPartyOrderTotalAmount() {
        if (ObjectUtils.isEmpty(thirdPartyOrderTotalAmount)) {
            thirdPartyOrderTotalAmount = BigDecimal.ZERO;
        }
        return thirdPartyOrderTotalAmount;
    }

    public BigDecimal getThirdPartyElectricityFee() {
        if (ObjectUtils.isEmpty(thirdPartyElectricityFee)) {
            thirdPartyElectricityFee = BigDecimal.ZERO;
        }
        return thirdPartyElectricityFee;
    }

    public BigDecimal getThirdPartyServiceFee() {
        if (ObjectUtils.isEmpty(thirdPartyServiceFee)) {
            thirdPartyServiceFee = BigDecimal.ZERO;
        }
        return thirdPartyServiceFee;
    }
}
