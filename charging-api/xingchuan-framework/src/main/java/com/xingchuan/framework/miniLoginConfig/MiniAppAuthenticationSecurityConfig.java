package com.xingchuan.framework.miniLoginConfig;


import com.xingchuan.framework.web.service.AppUserQuickLoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

/**
 * 小程序身份验证安全配置
 */
@Component
public class MiniAppAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private AppUserQuickLoginServiceImpl userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        MiniAppAuthenticationProvider smsCodeAuthenticationProvider = new MiniAppAuthenticationProvider();
        smsCodeAuthenticationProvider.setUserDetailsService(userDetailsService);

        http.authenticationProvider(smsCodeAuthenticationProvider);
    }
}
