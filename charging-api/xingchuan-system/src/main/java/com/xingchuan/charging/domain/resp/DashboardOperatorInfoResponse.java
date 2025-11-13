package com.xingchuan.charging.domain.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 首页场站信息
 */
@Data
@Builder
@ApiModel(value = "首页场站信息")
public class DashboardOperatorInfoResponse {


    /**
     * 电站数量
     */
    @ApiModelProperty(value = "电站数量")
    private Integer stationCount;

    /**
     * 桩数量
     */
    @ApiModelProperty(value = "桩数量")
    private Integer pileCount;

    /**
     * 终端数量
     */
    @ApiModelProperty(value = "终端数量")
    private Integer terminalCount;
}
