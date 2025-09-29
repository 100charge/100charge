package com.xingchuan.charging.domain.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "互联互通运营商下拉列表响应类")
public class OperatorListResponse {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "运营商名称")
    private String operatorName;

    public OperatorListResponse() {
    }

    public OperatorListResponse(String id, String operatorName) {
        this.id = id;
        this.operatorName = operatorName;
    }
}
