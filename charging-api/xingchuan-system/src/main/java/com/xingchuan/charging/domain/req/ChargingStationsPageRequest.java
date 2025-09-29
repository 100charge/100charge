package com.xingchuan.charging.domain.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 场站分页对象
 *
 * @author ruoyi
 */
@Data
public class ChargingStationsPageRequest {

    /**
     * 场站名称
     */
    @ApiModelProperty("场站名称")
    private String name;

    /**
     * 运营商id
     */
    @ApiModelProperty("运营商id")
    private Long tenantId;
}
