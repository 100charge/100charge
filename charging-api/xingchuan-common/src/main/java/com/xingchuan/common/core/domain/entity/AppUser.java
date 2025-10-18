package com.xingchuan.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xingchuan.common.annotation.Excel;
import com.xingchuan.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 微信-用户对象 app_user
 *
 * @author ruoyi
 */
@Data
@TableName("app_user")
public class AppUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 微信OpenId/支付宝唯一id/企业用户唯一id
     */
    @Excel(name = "微信OpenId/支付宝唯一id/企业用户唯一id")
    private String openId;

    /**
     * 登录账号
     */
    @Excel(name = "登录账号")
    private String userName;

    /**
     * 登录密码
     */
    @Excel(name = "登录密码")
    private String password;

    /**
     * 用户昵称
     */
    @Excel(name = "用户昵称")
    private String nickName;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码")
    private String phoneNumber;

    /**
     * 头像地址
     */
    @Excel(name = "头像地址")
    private String avatar;

    /**
     * 账户类型（0：企业客户；1：微信；2：支付宝;3:企业管理员）
     */
    @Excel(name = "账户类型", readConverterExp = "0=：企业客户；1：微信；2：支付宝;3:企业管理员")
    private Integer type;

    /**
     * 帐号状态（0正常 1停用）
     */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private Integer status;

    /**
     * 分组id
     */
    @Excel(name = "分组id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long groupId;

    /**
     * 租户id
     */
    @Excel(name = "租户id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tenantId;

    /**
     * 组织架构ID
     */
    @Excel(name = "组织架构ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 最后登录IP
     */
    @Excel(name = "最后登录IP")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date loginDate;

    /**
     * 授权协议号
     */
    private String authorizationCode;

    /**
     * 通联账户
     */
    private String allinPayAccount;

    /**
     * 剩余充值次数
     */
    private Integer rechargeCount;

    /**
     * 所属车队ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fleetId;
}
