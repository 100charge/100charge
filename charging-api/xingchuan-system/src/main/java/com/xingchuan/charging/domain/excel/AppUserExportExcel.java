package com.xingchuan.charging.domain.excel;

import com.xingchuan.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 微信-用户对象 app_user
 *
 * @author ruoyi
 */
@Data
public class AppUserExportExcel {

    /**
     * 用户ID
     */
    @Excel(name = "编号", width = 30)
    private Long id;

    /**
     * 微信OpenId/支付宝唯一id
     */
    @Excel(name = "微信OpenId/支付宝唯一id", width = 35)
    private String openId;

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
     * 帐号余额
     */
    @Excel(name = "帐号余额")
    private BigDecimal userBalance;

    /**
     * 账户类型（0：企业客户；1：微信；2：支付宝）
     */
    @Excel(name = "账户类型", readConverterExp = "0=企业客户,1=微信,2=支付宝")
    private Integer type;

    /**
     * 帐号状态（0正常 1停用）
     */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private Integer status;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
