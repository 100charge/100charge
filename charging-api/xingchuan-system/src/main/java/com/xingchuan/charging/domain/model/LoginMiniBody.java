package com.xingchuan.charging.domain.model;

import lombok.Data;

/**
 * 微信-用户登录对象
 */
@Data
public class LoginMiniBody {
    /**
     * 加密算法的初始向量
     */
    String iv;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 调用接口获取登录凭证（code）。通过凭证进而换取用户登录态信息，
     * 包括用户在当前小程序的唯一标识（openid）、
     * 微信开放平台账号下的唯一标识（union_id，若当前小程序已绑定到微信开放平台账号）
     * 及本次登录的会话密钥（session_key）等。用户数据的加解密通讯需要依赖会话密钥完成。
     */
    private String code;
    /**
     * 消息密文
     */
    private String encryptedData;

}
