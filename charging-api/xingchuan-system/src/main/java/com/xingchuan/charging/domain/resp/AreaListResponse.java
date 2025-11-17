package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 省市区对象 area
 */
@Data
@ApiModel(value = "区域列表返回实体")
public class AreaListResponse {

    /**
     * id
     */
    @ApiModelProperty(name = "区域id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 区域名称
     */
    @ApiModelProperty(name = "区域名称")
    private String name;

}
