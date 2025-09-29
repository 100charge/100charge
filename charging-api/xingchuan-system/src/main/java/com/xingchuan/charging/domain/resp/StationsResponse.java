package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 场站对象 charging_stations
 *
 * @author ruoyi
 */
@Data
@ApiModel("车队详情-场站信息返回实体")
public class StationsResponse {

    /**
     * 场站ID
     */
    @ApiModelProperty("场站ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 场站名称
     */
    @ApiModelProperty("场站名称")
    private String name;

    /**
     * 是否互联互通场站
     */
    private Boolean operateStation;
}
