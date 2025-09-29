package com.xingchuan.charging.domain.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "车辆认证请求参数")
public class CarAuditRequest {

    @ApiModelProperty(value = "用户车辆id")
    @NotNull(message = "用户车辆id不能为空")
    private Long id;

    @ApiModelProperty(value = "审核结果:true:审核通过 false:审核驳回")
    @NotNull(message = "审核结果不能为空")
    private Boolean auditStatus;

    @ApiModelProperty(value = "驳回原因(审核驳回时必填)")
    private String rejectReason;
}
