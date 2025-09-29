package com.xingchuan.framework.web.service;

import com.xingchuan.charging.enums.AppUserStatusEnum;
import com.xingchuan.charging.mapper.AppUserMapper;
import com.xingchuan.common.core.domain.entity.AppUser;
import com.xingchuan.common.core.domain.model.LoginUser;
import com.xingchuan.common.exception.ServiceException;
import com.xingchuan.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 小程序快捷登录校验实现
 */
@Slf4j
@Service
public class AppUserQuickLoginServiceImpl implements UserDetailsService {

    @Autowired
    private AppUserMapper appUserMapper;

    @Override
    public UserDetails loadUserByUsername(String openId) throws UsernameNotFoundException {
        // 小程序登录 没有则需要新增，存在则直接查询出来
        AppUser appUser = appUserMapper.getInfoByOpenId(openId);
        if (StringUtils.isNull(openId)) {
            log.info("登录用户:{} 不存在.", appUser);
            throw new ServiceException("用户不存在");
        } else if (AppUserStatusEnum.DISABLED.getCode() == appUser.getStatus()) {
            log.info("登录用户:{} 已被停用.", appUser);
            throw new ServiceException("您的登录账号被冻结！");
        }

        //返回UserDetails用户对象
        return createLoginUser(appUser.getId(), appUser);
    }

    private UserDetails createLoginUser(Long id, AppUser appUser) {
        return new LoginUser(appUser.getId(), appUser);
    }

}