package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 充电桩-充电枪对象 charging_guns
 *
 * @author ruoyi
 */
@Data
@ApiModel(value = "充电枪查询返回对象")
public class ChargingGunsResponse {

    /**
     * 充电枪id
     */
    @ApiModelProperty("充电枪id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 充电枪编号
     */
    @ApiModelProperty("充电枪编号")
    private String no;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 枪状态
     */
    @ApiModelProperty("枪状态")
    private String gunStatus;

    /**
     * 累计充电量
     */
    @ApiModelProperty("累计充电量")
    private String accumulatedChargingCapacity;

    /**
     * 电流
     */
    @ApiModelProperty("电流")
    private String current;

    /**
     * 电压
     */
    @ApiModelProperty("电压")
    private String voltage;

    /**
     * SOC
     */
    @ApiModelProperty("SOC")
    private String soc;

}
