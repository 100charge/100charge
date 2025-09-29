package com.xingchuan.charging.domain.resp;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.common.config.BigDecimalSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 准备充电返回请求
 *
 * @author ruoyi
 */
@Data
public class ReadyChargingResponse {

    /**
     * 场站id
     */
    @ApiModelProperty("场站id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long stationId;

    @ApiModelProperty("场站名称")
    private String stationName;

    /**
     * 场站计费规则id
     */
    @JSONField(serialize = false)
    @ApiModelProperty("场站计费规则id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long ruleId;

    /**
     * 订单号（唯一）
     */
    @ApiModelProperty(value = "订单号")
    private String tradeNo;

    /**
     * 桩名称（xxx号直流）
     */
    @ApiModelProperty("桩名称（xxx号直流）")
    private String deviceName;

    /**
     * 桩编号
     */
    @ApiModelProperty("桩编号")
    private String deviceNo;

    /**
     * 枪编号
     */
    @ApiModelProperty("枪编号")
    private String gunsNo;

    /**
     * 枪状态
     */
    @ApiModelProperty("枪状态")
    private String gunsStatus;

    /**
     * 功率
     */
    @ApiModelProperty("功率")
    private Integer maxPower;

    /**
     * 场站详细地址
     */
    @ApiModelProperty("场站详细地址")
    private String address;

    /**
     * 停车费描述
     */
    @ApiModelProperty("停车费描述")
    private String parkingFeeTip;

    /**
     * 是否快充
     */
    @ApiModelProperty("是否快充")
    private Boolean fastCharging;

    /**
     * 当前时间段
     */
    @ApiModelProperty("当前时间段")
    private String currentTime;

    /**
     * 当前时段充电费用
     */
    @ApiModelProperty("当前时段充电费用")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal chargeFee;

    /**
     * 当前充电进度
     */
    @ApiModelProperty("当前充电进度")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal soc;

    /**
     * 互联互通-运营商id
     */
    @JsonIgnore
    @ApiModelProperty("互联互通-运营商id")
    private String operatorId;

}
