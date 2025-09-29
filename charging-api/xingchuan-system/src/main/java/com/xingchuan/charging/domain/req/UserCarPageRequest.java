package com.xingchuan.charging.domain.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户车辆列表查询参数")
public class UserCarPageRequest {

    /**
     * 用户openId
     */
    @ApiModelProperty(value = "用户openId")
    private String openId;

    /**
     * 车牌号（保证唯一）
     */
    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    /**
     * 是否已认证(0:未认证，1：审核中，2：已认证，3：认证失败)
     */
    @ApiModelProperty(value = "是否已认证(0:未认证，1：审核中，2：已认证，3：认证失败)")
    private Integer verified;
}
