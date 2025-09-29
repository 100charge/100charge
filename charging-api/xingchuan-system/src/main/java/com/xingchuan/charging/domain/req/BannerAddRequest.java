package com.xingchuan.charging.domain.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 新增banner接受类
 */
@Data
public class BannerAddRequest {

    /**
     * banner名称
     */
    @ApiModelProperty(name = "banner名称")
    @NotNull(message = "名称不能为空！")
    private String name;

    /**
     * 链接地址
     */
    @ApiModelProperty(name = "链接地址")
    private String link;

    /**
     * 图片地址
     */
    @ApiModelProperty(name = "图片地址")
    @NotNull(message = "图片地址不能为空！")
    private String imageUrl;

    /**
     * 是否开启（0:不开启；1:开启）
     */
    @ApiModelProperty(name = "是否开启 0:不开启；1:开启")
    private Integer open;
}
