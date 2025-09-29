package com.xingchuan.charging.domain.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * banner管理列表接受类
 */
@Data
public class BannerPageRequest {

    /**
     * banner名称
     */
    @ApiModelProperty(name = "banner名称")
    private String name;

    /**
     * 是否开启（0:不开启；1:开启）
     */
    @ApiModelProperty(name = "是否开启 0=:不开启；1:开启")
    private Integer open;
}
