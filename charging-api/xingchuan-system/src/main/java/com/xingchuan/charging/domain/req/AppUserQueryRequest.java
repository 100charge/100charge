package com.xingchuan.charging.domain.req;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.xingchuan.charging.enums.AppUserEnum;
import com.xingchuan.charging.enums.AppUserStatusEnum;
import lombok.Data;

/**
 * app用户查询参数
 *
 * @author ruoyi
 */
@Data
public class AppUserQueryRequest {

    /**
     * 登录账号
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 手机号码
     */
    private String phoneNumber;


    /**
     * 账户类型（0：企业客户；1：微信；2：支付宝）
     */
    @EnumValue
    private AppUserEnum type;

    /**
     * 帐号状态（0正常 1停用）
     */
    @EnumValue
    private AppUserStatusEnum status;

}
