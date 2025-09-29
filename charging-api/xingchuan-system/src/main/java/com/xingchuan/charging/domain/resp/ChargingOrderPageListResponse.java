package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.common.config.BigDecimalSerializer;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * 充电订单分页查询返回实体
 */
@Data
public class ChargingOrderPageListResponse {

    /**
     * id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 订单号（唯一）
     */
    private String tradeNo;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /**
     * 场站名称
     */
    private String stationName;

    /**
     * 订单支付状态描述
     */
    private String payStatusDesc;

    /**
     * 订单状态描述
     */
    private String orderStateDesc;

    /**
     * 耗电量 单位 kw
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal totalPower;

    /**
     * 支付金额（充电费+服务费+超时占用费+停车费）
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal totalAmount;

    /**
     * 电费
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal chargeFee;

    /**
     * 服务费
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal serviceFee;

    /**
     * 超时占用费
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal overTimeFee;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 充电开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 充电结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 来源平台
     */
    private String platform;

    /**
     * 三方订单号（唯一）
     */
    private String outTradeNo;

    /**
     * 设备来源(互联互通商户)
     */
    private String equipmentSourceOperator;

    /**
     * 三方互联订单号
     */
    private String resaleOrderNo;

    /**
     * 转售系数
     */
    private BigDecimal resaleCoefficient;

    public void setTotalPower(BigDecimal totalPower) {
        if (ObjectUtils.isEmpty(totalPower)) {
            totalPower = BigDecimal.ZERO;
        }
        this.totalPower = totalPower.setScale(3, RoundingMode.HALF_UP);
    }
}
