package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * app用户新增对象
 *
 * @author ruoyi
 */
@Data
public class AppUserBalanceDetailsResponse {

    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private String amount;

    /**
     * 日期
     */
    @ApiModelProperty("日期")
    @DateTimeFormat(pattern = "MM-dd HH:mm:ss")
    @JsonFormat(pattern = "MM-dd HH:mm:ss")
    private Date time;

    /**
     * 明细名称
     */
    @ApiModelProperty("明细名称")
    private String name;

}
