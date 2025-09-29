package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 价格对象 rule_details
 *
 * @author ruoyi
 */
@Data
@ApiModel(value = "计费规则详情查询返回时间段实体类")
public class RuleDetailsResponse {

    /**
     * 主键
     */
    @ApiModelProperty(value = "计费规则子表id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 计费规则id
     */
    @ApiModelProperty(value = "计费规则id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long ruleId;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "HH:mm:ss")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "HH:mm:ss")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "充电费用")
    private BigDecimal chargeFee;

    @ApiModelProperty(value = "服务费")
    private BigDecimal serviceFee;

    @ApiModelProperty(value = "停车费")
    private BigDecimal parkingFee;

    @ApiModelProperty(value = "超时占用费")
    private BigDecimal occupancyFee;

    /**
     * 电费类型 0:尖 1:峰 2:平 3:谷 4:深谷
     */
    @ApiModelProperty(value = "费用类型 0:尖 1:峰 2:平 3:谷 4:深谷")
    private Integer type;

    /**
     * 电费类型 0:尖 1:峰 2:平 3:谷 4:深谷
     */
    @ApiModelProperty(value = "费用类型描述")
    private String typeDesc;

    /**
     * 组织架构id
     */
    @ApiModelProperty(value = "组织架构id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;

}
