package com.xingchuan.charging.domain.resp;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.common.config.BigDecimalSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@ApiModel(value = "场站数据统计返回对象")
public class StationDataStatisticsResponse {

    @ApiModelProperty(value = "总用电量")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal totalPower;

    @ApiModelProperty(value = "客户数量")
    private Integer userQty;

    @ApiModelProperty(value = "充电总金额")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal totalAmount;


    public void setTotalPower(BigDecimal totalPower) {
        if (ObjectUtils.isEmpty(totalPower)) {
            totalPower = BigDecimal.ZERO;
        }
        this.totalPower = totalPower.setScale(3, RoundingMode.HALF_UP);
    }

    public Integer getUserQty() {
        if (userQty == null) {
            return 0;
        }
        return userQty;
    }
}
