package com.xingchuan.charging.domain.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 场站评价分页对象
 *
 * @author ruoyi
 */
@Data
public class ChargingStationEvaluationPageRequest {
    /**
     * 场站id
     */
    @ApiModelProperty("场站id")
    private Long stationId;
}
