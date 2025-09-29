package com.xingchuan.framework.miniLoginConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xingchuan.common.constant.Constants;
import com.xingchuan.common.core.domain.AjaxResult;
import com.xingchuan.common.core.domain.model.LoginUser;
import com.xingchuan.framework.web.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 小程序身份验证成功处理程序
 */
@Component
public class MiniAppAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    TokenService tokenService;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("登录成功");

        AjaxResult ajax = AjaxResult.success();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String token = tokenService.createToken(loginUser);
        ajax.put(Constants.TOKEN, token);

        response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().write(objectMapper.writeValueAsString(tokenService.createToken(loginUser)));
        response.getWriter().write(objectMapper.writeValueAsString(ajax));
    }
}
