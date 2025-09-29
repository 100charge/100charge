package com.xingchuan.framework.web.service;

import com.xingchuan.framework.manager.AsyncManager;
import com.xingchuan.framework.manager.factory.AsyncFactory;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import com.xingchuan.common.constant.CacheConstants;
import com.xingchuan.common.constant.Constants;
import com.xingchuan.common.constant.UserConstants;
import com.xingchuan.common.core.domain.entity.SysUser;
import com.xingchuan.common.core.domain.model.RegisterBody;
import com.xingchuan.common.core.redis.RedisCache;
import com.xingchuan.common.exception.user.CaptchaException;
import com.xingchuan.common.exception.user.CaptchaExpireException;
import com.xingchuan.common.utils.MessageUtils;
import com.xingchuan.common.utils.SecurityUtils;
import com.xingchuan.common.utils.StringUtils;
import com.xingchuan.system.service.ISysConfigService;
import com.xingchuan.system.service.ISysUserService;

/**
 * 注册校验方法
 *
 * @author xingchuan
 */
@Component
public class SysRegisterService {
    @Resource
    private ISysUserService userService;

    @Resource
    private ISysConfigService configService;

    @Resource
    private RedisCache redisCache;

    /**
     * 注册
     */
    public String register(RegisterBody registerBody) {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);

        // 验证码开关
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }

        if (StringUtils.isEmpty(username)) {
            msg = "用户名不能为空";
        } else if (StringUtils.isEmpty(password)) {
            msg = "用户密码不能为空";
        } else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            msg = "账户长度必须在2到20个字符之间";
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "密码长度必须在5到20个字符之间";
        } else if (!userService.checkUserNameUnique(sysUser)) {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        } else {
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag) {
                msg = "注册失败,请联系系统管理人员";
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        return msg;
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }
    }
}
