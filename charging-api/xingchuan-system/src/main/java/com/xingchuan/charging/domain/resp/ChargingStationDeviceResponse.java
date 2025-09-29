package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xingchuan.common.config.BigDecimalSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 场站时段价格
 *
 * @author ruoyi
 */
@Data
public class ChargingStationDeviceResponse {

    /**
     * 枪编号
     */
    @ApiModelProperty(name = "枪编号")
    private String deviceNo;

    /**
     * 枪状态（离线、空闲、故障、充电中）
     */
    @ApiModelProperty(name = "枪状态（离线、空闲、故障、充电中）")
    private Integer deviceStatus;

    /**
     * 枪状态（离线、空闲、故障、充电中）
     */
    @ApiModelProperty(name = "枪状态（离线、空闲、故障、充电中）")
    private String deviceStatusName;

    /**
     * 充电百分比
     */
    @ApiModelProperty(name = "充电百分比")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal ratio;

    /**
     * 最大功率
     */
    @ApiModelProperty(name = "最大功率")
    private Integer maxPower;

    /**
     * 充电方式（直流、交流）
     */
    @ApiModelProperty(name = "充电方式（直流、交流）")
    private String pileType;

}
