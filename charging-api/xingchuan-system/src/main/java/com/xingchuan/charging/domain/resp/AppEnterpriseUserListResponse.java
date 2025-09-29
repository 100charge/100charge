package com.xingchuan.charging.domain.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 微信-企业用户列表返回对象
 *
 * @author ruoyi
 */
@Data
public class AppEnterpriseUserListResponse {

    /**
     * 用户ID
     */
    @ApiModelProperty("id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 登录账号
     */
    @ApiModelProperty("登录账号")
    private String userName;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String enterpriseName;

    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    private String phoneNumber;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String nickName;


    /**
     * 帐号状态（0正常 1停用）
     */
    @ApiModelProperty("帐号状态（0正常 1停用）")
    private Integer status;

    /**
     * 帐号状态（0正常 1停用）
     */
    @ApiModelProperty("帐号状态（0正常 1停用）")
    private String statusDesc;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 车队id
     */
    @ApiModelProperty("车队id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fleetId;

    /**
     * 车队名称
     */
    @ApiModelProperty("车队名称")
    private String fleetName;

    /**
     * 企业id
     */
    @ApiModelProperty("企业id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long companyId;
}
