package com.xingchuan.charging.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GunInfoModel {

    /**
     * 桩编号
     */
    @ApiModelProperty("桩编号")
    private String deviceNo;
    /**
     * 枪编号
     */
    @ApiModelProperty("枪编号")
    private String gunNo;
    /**
     * 桩id
     */
    @ApiModelProperty("桩id")
    private Long pileId;
}
