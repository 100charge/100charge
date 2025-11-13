package com.xingchuan.framework.miniLoginConfig;

import com.xingchuan.framework.web.service.AppUserQuickLoginServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;

/**
 * 小程序快捷登陆鉴权 Provider，要求实现 AuthenticationProvider 接口
 */

public class MiniAppAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private AppUserQuickLoginServiceImpl userDetailsService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MiniAppAuthenticationToken authenticationToken = (MiniAppAuthenticationToken) authentication;

        String openId = (String) authenticationToken.getPrincipal();

        UserDetails userDetails = userDetailsService.loadUserByUsername(openId);

        // 此时鉴权成功后，应当重新 new 一个拥有鉴权的 authenticationResult 返回
        MiniAppAuthenticationToken authenticationResult = new MiniAppAuthenticationToken(userDetails, userDetails.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 判断 authentication 是不是 SmsCodeAuthenticationToken 的子类或子接口
        return MiniAppAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = (AppUserQuickLoginServiceImpl) userDetailsService;
    }
}