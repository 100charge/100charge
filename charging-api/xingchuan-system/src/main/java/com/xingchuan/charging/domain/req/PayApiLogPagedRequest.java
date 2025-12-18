package com.xingchuan.charging.domain.req;

import com.xingchuan.charging.enums.CallDirectionEnum;
import com.xingchuan.charging.enums.PayApiSourceEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

/**
 * 支付日志分页请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PayApiLogPagedRequest {
    /**
     * 请求地址
     */
    @ApiModelProperty()
    private String apiAddress;
    /**
     * 请求中包含的内容
     */
     @ApiModelProperty("请求中包含的内容")
    private String requestKey;
    /**
     * 返回中包含的内容
     */
    @ApiModelProperty("返回中包含的内容")
    private String responseKey;
    /**
     * 是否成功
     */
    @ApiModelProperty("是否成功")
    private Boolean success;
    /**
     * 请求方向
     */
    @ApiModelProperty("请求方向")
    private CallDirectionEnum callDirection;
    /**
     * 支付来源
     */
    @ApiModelProperty("支付来源")
    private PayApiSourceEnum payApiSource;
    /**
     * 错误日志
     */
    @ApiModelProperty("错误日志")
    private String errorMsg;
    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    /**
     * 页数
     */
     @ApiModelProperty("页数")
    private int pageNumber = 0;
    /**
     * 每页条数
     */
     @ApiModelProperty("每页条数")
    private int pageSize = 10;
    /**
     * 排序条件
     */
     @ApiModelProperty("排序条件")
    private Sort orders;
}
