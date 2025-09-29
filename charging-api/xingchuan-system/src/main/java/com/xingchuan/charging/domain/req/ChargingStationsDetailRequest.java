package com.xingchuan.charging.domain.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 场站详情请求对象
 *
 * @author ruoyi
 */
@Data
public class ChargingStationsDetailRequest {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 当前纬度
     */
    @NotNull(message = "当前纬度不能为空")
    @ApiModelProperty(value = "当前纬度")
    private BigDecimal lat;

    /**
     * 当前经度
     */
    @NotNull(message = "当前经度不能为空")
    @ApiModelProperty(value = "当前经度")
    private BigDecimal lng;

}
