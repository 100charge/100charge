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
 * 首页-今日、昨日数据返回对象
 *
 * @author ruoyi
 */
@Data
@ApiModel("首页-今日、昨日数据返回对象")
public class HomeDayChargingDataResponse {

    /**
     * 今日充电量（度）
     */
    @ApiModelProperty("今日充电量（度）")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal todayCharge;
    /**
     * 今日充电量（度）
     */
    @ApiModelProperty("昨日充电量（度）")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal yesterdayCharge;
    /**
     * 今日充电订单数量
     */
    @ApiModelProperty("今日充电订单数量")
    private Integer todayOrderQty;
    /**
     * 昨日充电订单数量
     */
    @ApiModelProperty("昨日充电订单数量")
    private Integer yesterdayOrderQty;
    /**
     * 今日充电订单总额
     */
    @ApiModelProperty("今日充电订单总额")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal todayOrderTotalAmount;
    /**
     * 昨日充电订单总额
     */
    @ApiModelProperty("昨日充电订单总额")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal yesterdayOrderTotalAmount;
    /**
     * 今日充电订单服务费
     */
    @ApiModelProperty("今日充电订单服务费")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal todayOrderServiceFee;
    /**
     * 昨日充电订单服务费
     */
    @ApiModelProperty("昨日充电订单服务费")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal yesterdayOrderServiceFee;


    public void setTodayCharge(BigDecimal todayCharge) {
        if (ObjectUtils.isEmpty(todayCharge)) {
            todayCharge = BigDecimal.ZERO;
        }
        this.todayCharge = todayCharge.setScale(3, RoundingMode.HALF_UP);
    }

    public void setYesterdayCharge(BigDecimal yesterdayCharge) {
        if (ObjectUtils.isEmpty(yesterdayCharge)) {
            yesterdayCharge = BigDecimal.ZERO;
        }
        this.yesterdayCharge = yesterdayCharge.setScale(3, RoundingMode.HALF_UP);
    }

}
