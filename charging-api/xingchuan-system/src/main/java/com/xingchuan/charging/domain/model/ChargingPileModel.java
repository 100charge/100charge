package com.xingchuan.charging.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 场站充电桩、枪信息
 *
 * @author ruoyi
 */
@Data
public class ChargingPileModel {

    /**
     * 场站Id
     */
    @ApiModelProperty(name = "场站Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long stationId;

    /**
     * 桩编号
     */
    @ApiModelProperty(name = "桩编号")
    private String deviceNo;

    /**
     * 枪编号
     */
    @ApiModelProperty(name = "枪编号")
    private String gunsNo;

    /**
     * 最大功率
     */
    @ApiModelProperty(name = "最大功率")
    private Integer maxPower;

    /**
     * 充电方式
     */
    @ApiModelProperty(name = "充电方式")
    private Integer pileType;

    /**
     * 互联互通-运营商id
     */
    @JsonIgnore
    @ApiModelProperty(name = "互联互通-运营商id")
    private String operatorId;

}
