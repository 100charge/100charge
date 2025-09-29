package com.xingchuan.charging.domain.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 开放城市返回类
 *
 * @author ruoyi
 */
@Data
public class AreaOpenCitiesResponse {

    /**
     * 城市名称
     */
    @ApiModelProperty("城市名称")
    private String name;

    /**
     * 城市行政编码
     */
    @ApiModelProperty("城市行政编码")
    private String code;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private BigDecimal lat;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private BigDecimal lng;

}
