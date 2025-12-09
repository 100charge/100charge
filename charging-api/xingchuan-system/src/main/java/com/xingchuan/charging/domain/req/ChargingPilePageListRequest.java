package com.xingchuan.charging.domain.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 充电桩对象 charging_pile
 *
 * @author ruoyi
 */
@Data
@ApiModel(value = "充电桩分页查询请求对象")
public class ChargingPilePageListRequest {

    /**
     * 场站名称
     */
    @ApiModelProperty(value = "场站名称")
    private String stationName;

    @ApiModelProperty(value = "场站ID")
    private Long stationId;
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
     * 充电桩类型 0:交流 1:直流
     */
    @ApiModelProperty(value = "充电桩类型")
    private Integer pileType;

    /**
     * 设备状态  1:启用  0:禁用
     */
    @ApiModelProperty(value = "设备状态")
    private Integer pileStatus;

}
