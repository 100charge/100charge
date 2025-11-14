package com.xingchuan.charging.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.charging.domain.entity.ChargingOrder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;

/**
 * 场站余额限制
 */
@Data
@NoArgsConstructor
public class StationChargingLimitModel {
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 设备号
     */
    private String deviceNo;
    /**
     * 枪号
     */
    private String gunNo;
    /**
     * 协议类型
     */
    private int protocolVersion;
    /**
     * 场站充电金额限制
     */
    private BigDecimal chargingLimitAmount;
    /**
     * 订单所有者余额
     */
    private BigDecimal balance;
    /**
     * 车队ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fleetId;
    /**
     * 用户openId
     */
    private String openId;


    public StationChargingLimitModel(ChargingOrder chargingOrder, BigDecimal limitAmount, Integer protocolVersion, BigDecimal balance) {
        this.orderNo = chargingOrder.getTradeNo();
        this.deviceNo = chargingOrder.getDeviceNo();
        this.gunNo = chargingOrder.getGunNo();
        this.protocolVersion = protocolVersion;
        this.chargingLimitAmount = limitAmount;
        this.openId = chargingOrder.getOpenId();
        this.balance = balance;

        if (ObjectUtils.isNotEmpty(chargingOrder.getFleetId())) {
            this.fleetId = chargingOrder.getFleetId();
        }
    }

}
