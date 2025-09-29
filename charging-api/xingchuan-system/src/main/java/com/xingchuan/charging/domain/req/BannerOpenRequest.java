package com.xingchuan.charging.domain.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 开启/关闭banner接受类
 */
@Data
public class BannerOpenRequest {

    @ApiModelProperty(name = "id")
    @NotNull(message = "id不能为空！")
    private Long id;

    /**
     * 是否开启（0:不开启；1:开启）
     */
    @ApiModelProperty(name = "open")
    @NotNull(message = "状态不能为空！")
    private Integer open;
}
