package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * banner列表分页返回类
 */
@Data
public class BannerListResponse {
    /**
     * $column.columnComment
     */
    @ApiModelProperty(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * banner名称
     */
    @ApiModelProperty(name = "banner名称")
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
    private String imageUrl;

    /**
     * 是否开启（0:不开启；1:开启）
     */
    @ApiModelProperty(name = "是否开启 0=:不开启；1:开启")
    private Integer open;
}
