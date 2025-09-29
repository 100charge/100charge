package com.xingchuan.charging.domain.req;

import com.xingchuan.common.constant.MessageConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 启用或禁用请求
 */
@Data
public class EnableOrDisableRequest {

    @ApiModelProperty(value = "id")
    @NotNull(message = MessageConstants.CHARGING_STATIONS_NOT_EXIST)
    private Long id;
}
