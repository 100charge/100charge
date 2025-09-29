package com.xingchuan.framework.web.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.alibaba.fastjson2.JSONObject;
import com.xingchuan.charging.enums.AppUserEnum;
import com.xingchuan.charging.service.IAppUserService;
import com.xingchuan.common.constant.CacheConstants;
import com.xingchuan.common.constant.Constants;
import com.xingchuan.common.constant.MessageConstants;
import com.xingchuan.common.constant.UserConstants;
import com.xingchuan.common.core.domain.entity.AppUser;
import com.xingchuan.common.core.domain.entity.SysUser;
import com.xingchuan.common.core.domain.model.LoginUser;
import com.xingchuan.common.core.redis.RedisCache;
import com.xingchuan.common.exception.ServiceException;
import com.xingchuan.common.exception.user.*;
import com.xingchuan.common.utils.*;
import com.xingchuan.common.utils.ip.IpUtils;
import com.xingchuan.framework.manager.AsyncManager;
import com.xingchuan.framework.manager.factory.AsyncFactory;
import com.xingchuan.framework.miniLoginConfig.MiniAppAuthenticationToken;
import com.xingchuan.framework.security.context.AuthenticationContextHolder;
import com.xingchuan.system.service.ISysConfigService;
import com.xingchuan.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 登录校验方法
 *
 * @author xingchuan
 */
@Slf4j
@Component
public class SysLoginService {

    @Resource
    private RedisCache redisCache;
    @Resource
    private WxMaService wxMaService;
    @Resource
    private TokenService tokenService;
    @Resource
    private ISysUserService userService;
    @Resource
    private IAppUserService appUserService;
    @Resource
    private ISysConfigService configService;
    @Resource
    private AuthenticationManager authenticationManager;


    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        // 验证码校验
        validateCaptcha(username, code, uuid);
        // 登录前置校验
        loginPreCheck(username, password);
        // 用户验证
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
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
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            redisCache.deleteObject(verifyKey);
            if (captcha == null) {
                AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                throw new CaptchaExpireException();
            }
            if (!code.equalsIgnoreCase(captcha)) {
                AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                throw new CaptchaException();
            }
        }
    }

    /**
     * 登录前置校验
     *
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            AsyncManager.me().execute(AsyncFactory.recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
            throw new BlackListException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }

    /**
     * 微信小程序快速登录
     *
     * @param code 微信小程序code
     * @return 登录用户信息
     */
    public LoginUser wxQuickLogin(String code) {
        try {
            String accessToken = wxMaService.getAccessToken();
            log.info("获取到的accessToken:{}", accessToken);
            WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(code);
            log.info("sessionInfo:{}", JSONObject.toJSONString(sessionInfo));
            String openId = sessionInfo.getOpenid();

            if (!appUserService.existsOpenId(openId)) {
                // 用户不存在，直接新增用户 并且 余额表插入默认数据
                AppUser appUser = new AppUser();
                appUser.setOpenId(openId);
                appUser.setType(AppUserEnum.WECHAT.getCode());
                appUserService.saveAndBalance(appUser);
                // 更新通联授权协议号
                String authorizationCode = RandomUtils.getAppUserAuthorizationCode(appUser.getId());
                appUser.setAuthorizationCode(authorizationCode);
                appUserService.updateById(appUser);
            }
            // 用户验证
            Authentication authentication;
            try {
                // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
                authentication = authenticationManager.authenticate(new MiniAppAuthenticationToken(openId));
            } catch (Exception e) {
                if (e instanceof BadCredentialsException) {
                    throw new UserPasswordNotMatchException();
                } else {
                    throw new ServiceException(e.getMessage());
                }
            } finally {
                AuthenticationContextHolder.clearContext();
            }
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            loginUser.setToken(tokenService.createMiniToken(loginUser));
            return loginUser;
        } catch (Exception exception) {
            log.error("微信登录失败:{}", exception.getLocalizedMessage());
            throw new ServiceException(exception.getLocalizedMessage());
        }
    }

    /**
     * 绑定微信、支付宝手机号
     *
     * @param openId 用户openId
     * @param code   微信查询手机号编号
     */
    public void bindMobile(String openId, String code) {
        try {
            AppUser appUser = appUserService.getInfoByOpenId(openId);
            if (ObjectUtils.isEmpty(appUser)) {
                throw new ServiceException(MessageConstants.USER_DOES_NOT_EXIST);
            }
            if (StringUtils.isNotEmpty(appUser.getPhoneNumber())) {
                return;
            }
            WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getPhoneNoInfo(code);
            log.info("手机号信息:{}", JSONObject.toJSONString(phoneNoInfo));
            String phoneNumber = phoneNoInfo.getPhoneNumber();
            appUser.setPhoneNumber(phoneNumber);

            if (ObjectUtils.isEmpty(appUser.getNickName())) {
                appUser.setNickName(phoneNumber);
            }
            appUserService.updateById(appUser);
            //更新用户手机号
            updateSecurityContext(phoneNumber);
        } catch (WxErrorException exception) {
            log.error("绑定手机号失败:{}", exception.getError().getErrorMsg());
            throw new ServiceException("绑定手机号失败:" + exception.getLocalizedMessage());
        }
    }

    /**
     * 更新用户手机号
     */
    private void updateSecurityContext(String phone) {
        try {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            String token = null;
            if (loginUser != null) {
                token = loginUser.getToken();
            }
            String redisKey = CacheConstants.LOGIN_TOKEN_KEY + token;

            LoginUser redisUser = redisCache.getCacheObject(redisKey);
            redisUser.getAppUser().setPhoneNumber(phone);
            tokenService.setLoginUser(redisUser);
            SecurityUtils.updateSecurityContext(redisUser);
        } catch (Exception e) {
            log.error("更新用户手机号失败:", e);
        }
    }

}
