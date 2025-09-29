package com.xingchuan.charging.domain.model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.charging.enums.ChargeGunsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 表示充电订单处理日志实体类。
 */
@Data
public class ChargingRealtimeDataModel {

    // 设备信息
    @ApiModelProperty("设备序列号")
    private String deviceNo;

    @ApiModelProperty("交易流水号")
    private String tradeNo;

    // 充电枪详情
    @ApiModelProperty("枪编号")
    private String gunNo;

    @EnumValue
    @ApiModelProperty("枪状态")
    private ChargeGunsEnum status;

    @ApiModelProperty("枪复位信息")
    private String gunReset;

    @ApiModelProperty("枪是否已插入")
    private Boolean gunInserted;

    // 充电参数
    @ApiModelProperty("输出电压")
    private double outputVoltage;

    @ApiModelProperty("输出电流")
    private double outputCurrent;

    @ApiModelProperty("线路温度")
    private double lineTemperature;

    @ApiModelProperty("线路编号")
    private String lineNo;

    // 电池状态
    @ApiModelProperty("电池荷电状态（SOC）")
    private double soc;

    @ApiModelProperty("电池温度")
    private double batteryTemperature;

    // 过程详情
    @ApiModelProperty("累计充电时间(分钟)")
    private double duration;

    @ApiModelProperty("预计剩余时间")
    private double timeLeft;

    @ApiModelProperty("充电度数")
    private double totalPower;

    @ApiModelProperty("计损充电度数,未设置计损比例时等于充电度数")
    private double lostPower;

    @ApiModelProperty("已充金额（电费+服务费）*计损充电度数")
    private double totalAmount;

    // 故障或错误信息
    @ApiModelProperty("硬件故障")
    private String fault;

    // 充电桩id
    @ApiModelProperty("充电桩id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pileId;

    public ChargeGunsEnum getStatus() {
        if (status == null) {
            return ChargeGunsEnum.OFFLINE;
        }
        if (status.equals(ChargeGunsEnum.ONLINE) && gunInserted) {
            return ChargeGunsEnum.GUN_INSERTED;
        }
        return status;
    }
}
