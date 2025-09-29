package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.common.config.BigDecimalSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 充电桩对象 charging_pile
 *
 * @author ruoyi
 */
@Data
@ApiModel(value = "充电桩分页查询返回对象")
public class ChargingPilePageListResponse {

    /**
     * id
     */
    @ApiModelProperty(value = "充电桩id", notes = "充电桩id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 场站id
     */
    @ApiModelProperty(value = "场站id", notes = "场站id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long stationId;

    /**
     * 场站名称
     */
    @ApiModelProperty(value = "场站名称", notes = "场站名称")
    private String stationName;

    /**
     * 充电桩设备号
     */
    @ApiModelProperty(value = "充电桩设备号")
    private String deviceNo;

    /**
     * 充电桩名称
     */
    @ApiModelProperty(value = "充电桩名称")
    private String chargingPileName;

    /**
     * 额定功率/输出功率范围-最高
     */
    @ApiModelProperty(value = "额定功率/输出功率范围-最高")
    private Integer maxPower;

    /**
     * 额定功率/输出功率范围-最低
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "额定功率/输出功率范围-最低")
    private BigDecimal minPower;

    /**
     * 额定电压/输出电压范围-最高
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "额定电压/输出电压范围-最高")
    private BigDecimal maxVoltage;

    /**
     * 额定电压/输出电压范围-最低
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "额定电压/输出电压范围-最低")
    private BigDecimal minVoltage;

    /**
     * 额定电流/输出电流范围-最高
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "额定电流/输出电流范围-最高")
    private BigDecimal maxCurrent;

    /**
     * 额定电流/输出电流范围-最低
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    @ApiModelProperty(value = "额定电流/输出电流范围-最低")
    private BigDecimal minCurrent;

    /**
     * 充电桩类型 0:交流 1:直流
     */
    @ApiModelProperty(value = "充电桩类型 0:交流 1:直流")
    private Integer pileType;

    /**
     * 充电桩类型 0:交流 1:直流
     */
    @ApiModelProperty(value = "充电桩类型描述")
    private String pileTypeDesc;

    /**
     * 设备状态  1:启用  0:禁用
     */
    @ApiModelProperty(value = "设备状态  1:启用  0:禁用")
    private Integer pileStatus;

    /**
     * 设备状态  1:启用  0:禁用
     */
    @ApiModelProperty(value = "设备状态描述")
    private String pileStatusDesc;

    /**
     * 生产厂商
     */
    @ApiModelProperty(value = "生产厂商")
    private String supplier;

    /**
     * 硬件版本
     */
    @ApiModelProperty(value = "硬件版本")
    private String hardwareVersion;

    /**
     * 协议版本
     */
    @ApiModelProperty(value = "协议版本")
    private String protocolVersion;

    /**
     * 枪数量
     */
    @ApiModelProperty(value = "枪数量")
    private Integer gunNumber;

    /**
     * 设备实时状态  空闲/故障/离线/占用
     */
    @ApiModelProperty(value = "设备实时状态(预留)")
    private String pileRealTimeStatus;

    /**
     * 机型
     */
    @ApiModelProperty(value = "机型(预留)")
    private String model;

    /**
     * 充电设备接口类型
     */
    @ApiModelProperty(value = "充电设备接口类型")
    private Integer connectorType;

    /**
     * 国家标准
     */
    @ApiModelProperty(value = "国家标准")
    private Integer nationalStandard;

}
