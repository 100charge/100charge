package com.xingchuan.charging.domain.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 价格对象 rule_details
 *
 * @author ruoyi
 */
@Data
@ApiModel(value = "计费规则费用类型及时间段新增请求类")
public class RuleDetailsAddRequest {

    @ApiModelProperty(value = "开始时间")
    @NotNull(message = "开始时间不能为空!")
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @NotNull(message = "结束时间不能为空!")
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "充电费用")
    @NotNull(message = "充电费用不能为空!")
    @DecimalMin(value = "0", message = "费用最小为0")
    private BigDecimal chargeFee;

    @ApiModelProperty(value = "服务费")
    @NotNull(message = "服务费不能为空!")
    @DecimalMin(value = "0", message = "服务费最小为0")
    private BigDecimal serviceFee;

    @ApiModelProperty(value = "停车费")
    @NotNull(message = "停车费不能为空!")
    @DecimalMin(value = "0", message = "停车费最小为0")
    private BigDecimal parkingFee;

    @ApiModelProperty(value = "超时占用费")
    @NotNull(message = "超时占用费不能为空!")
    @DecimalMin(value = "0", message = "超时占用费最小为0")
    private BigDecimal occupancyFee;

    /**
     * 时间段类型 0:尖 1:峰 2:平 3:谷 4:深谷
     */
    @ApiModelProperty(value = "时间段类型")
    @NotNull(message = "时间段类型不能为空!")
    private Integer type;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
