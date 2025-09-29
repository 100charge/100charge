package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.common.config.BigDecimalSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 首页-经营趋势返回对象
 */
@Data
@ApiModel("首页-经营趋势返回对象")
public class HomeBusinessTrendResponse {

    /**
     * 日期（7月27日）
     */
    @ApiModelProperty("日期")
    private String time;
    /**
     * 总充电量
     */
    @ApiModelProperty("总充电量")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal totalChargingAmount;
    /**
     * 订单总额
     */
    @ApiModelProperty("订单总额")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal totalOrderAmount;
    /**
     * 充电服务费
     */
    @ApiModelProperty("充电服务费")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal chargingServiceFee;
    /**
     * 总充电量对比
     */
    @ApiModelProperty("总充电量对比")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal totalChargingAmountComparison;
    /**
     * 订单总额对比
     */
    @ApiModelProperty("订单总额对比")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal totalOrderAmountComparison;
    /**
     * 充电服务费对比
     */
    @ApiModelProperty("充电服务费对比")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal chargingServiceFeeComparison;

    public void setTotalChargingAmount(BigDecimal totalChargingAmount) {
        if (ObjectUtils.isEmpty(totalChargingAmount)) {
            totalChargingAmount = BigDecimal.ZERO;
        }
        this.totalChargingAmount = totalChargingAmount.setScale(3, RoundingMode.HALF_UP);
    }

    public void setTotalChargingAmountComparison(BigDecimal totalChargingAmountComparison) {
        if (ObjectUtils.isEmpty(totalChargingAmountComparison)) {
            totalChargingAmountComparison = BigDecimal.ZERO;
        }
        this.totalChargingAmountComparison = totalChargingAmountComparison.setScale(3, RoundingMode.HALF_UP);
    }

}
