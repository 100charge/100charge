package com.xingchuan.charging.domain.req;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.xingchuan.charging.enums.TimeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 首页-经营趋势请求对象
 */
@Data
@ApiModel(value = "首页-经营趋势请求对象", description = "首页-经营趋势请求对象")
public class HomeBusinessTrendRequest {

    /**
     * 场站id
     */
    @NotNull(message = "场站信息不能为空!")
    @ApiModelProperty(value = "场站id", notes = "场站id")
    private Long stationId;

    /**
     * 时间类型
     */
    @EnumValue
    @NotBlank(message = "时间类型不能为空")
    @ApiModelProperty(value = "时间类型", notes = "时间类型")
    private TimeType timeType;

    /**
     * 日期数
     */
    @NotNull(message = "日期数不能为空!")
    @ApiModelProperty(value = "日期数", notes = "日期数")
    private Integer dateNumber;


    public Integer getDateNumber() {
        return (dateNumber - 1) * -1;
    }
}
