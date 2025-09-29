package com.xingchuan.charging.domain.excel;

import com.xingchuan.common.annotation.Excel;
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
@ApiModel(value = "充电桩对象导入")
public class ChargingPileExcelImport {

    /**
     * 所属场站
     */
    @Excel(name = "所属场站")
    @ApiModelProperty("所属场站")
    private String stationName;
    /**
     * 充电桩设备号
     */
    @Excel(name = "充电桩设备号")
    @ApiModelProperty("充电桩设备号")
    private String deviceNo;
    /**
     * 充电桩名称
     */
    @Excel(name = "充电桩名称")
    @ApiModelProperty("充电桩名称")
    private String name;
    /**
     * 充电桩类型
     */
    @Excel(name = "充电桩类型")
    @ApiModelProperty("充电桩类型")
    private String pileType;
    /**
     * 枪数量
     */
    @Excel(name = "枪数量")
    @ApiModelProperty("枪数量")
    private Integer gunNumber;
    /**
     * 硬件版本
     */
    @Excel(name = "硬件版本")
    @ApiModelProperty("硬件版本")
    private String hardwareVersion;
    /**
     * 最大输出电流
     */
    @Excel(name = "最大输出电流")
    @ApiModelProperty("最大输出电流")
    private BigDecimal maxCurrent;
    /**
     * 最小输出电流
     */
    @Excel(name = "最小输出电流")
    @ApiModelProperty("最小输出电流")
    private BigDecimal minCurrent;
    /**
     * 最大输出功率
     */
    @Excel(name = "最大输出功率")
    @ApiModelProperty("最大输出功率")
    private Integer maxPower;
    /**
     * 最小输出功率
     */
    @Excel(name = "最小输出功率")
    @ApiModelProperty("最小输出功率")
    private Integer minPower;
    /**
     * 最大输出电压
     */
    @Excel(name = "最大输出电压")
    @ApiModelProperty("最大输出电压")
    private BigDecimal maxVoltage;
    /**
     * 最小输出电压
     */
    @Excel(name = "最小输出电压")
    @ApiModelProperty("最小输出电压")
    private BigDecimal minVoltage;
    /**
     * 协议版本：0-云快充1.5；1-云快充1.6；2-特来电交流；3-特来电直流；
     */
    @Excel(name = "协议版本")
    @ApiModelProperty("协议版本：0-云快充1.5；1-云快充1.6；2-特来电交流；3-特来电直流；")
    private String protocolVersion;
    /**
     * 生产厂商
     */
    @Excel(name = "生产厂商")
    @ApiModelProperty("生产厂商")
    private String supplier;
    /**
     * 备注
     */
    @Excel(name = "备注")
    @ApiModelProperty("备注")
    private String remark;
}
