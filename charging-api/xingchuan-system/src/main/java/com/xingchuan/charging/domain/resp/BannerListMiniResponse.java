package com.xingchuan.charging.domain.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 小程序首页banner列表返回类
 */
@Data
public class BannerListMiniResponse {

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

}
