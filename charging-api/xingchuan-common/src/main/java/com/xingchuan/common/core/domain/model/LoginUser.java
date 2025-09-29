package com.xingchuan.common.core.domain.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.xingchuan.common.core.domain.entity.AppUser;
import com.xingchuan.common.core.domain.entity.SysUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * 登录用户身份权限
 */
@Data
public class LoginUser implements UserDetails {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    /**
     * 部门权限id集合
     */
    private Set<Long> deptPermissions;

    /**
     * 用户信息
     */
    private SysUser user;

    /**
     * 小程序用户
     */
    private AppUser appUser;

    /**
     * 是否管理员
     */
    private boolean admin;

    /**
     * 是否平台管理员
     */
    private boolean platformAdmin;

    public LoginUser(SysUser user, Set<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    public LoginUser(Long userId, Long deptId, SysUser user, Set<String> permissions) {
        this.userId = userId;
        this.deptId = deptId;
        this.user = user;
        this.permissions = permissions;
    }

    public LoginUser(Long userId, Long deptId, SysUser user, Set<String> permissions, Set<Long> deptIdPermissions) {
        this.userId = userId;
        this.deptId = deptId;
        this.user = user;
        this.permissions = permissions;
        this.deptPermissions = deptIdPermissions;
    }

    public LoginUser(Long userId, AppUser appUser, Set<Long> deptIds) {
        this.userId = userId;
        this.appUser = appUser;
        this.deptPermissions = deptIds;
    }

    public LoginUser(Long id, AppUser appUser) {
        this.userId = id;
        this.appUser = appUser;
    }

    @JSONField(serialize = false)
    @Override
    public String getPassword() {
        if (user != null) {
            return user.getPassword();
        }
        return appUser.getPassword();
    }

    @Override
    public String getUsername() {
        if (user != null) {
            return user.getUserName();
        }
        return appUser.getUserName();
    }


    public boolean isAdmin() {
        return user != null && user.isAdmin();
    }


    /**
     * 账户是否未过期,过期无法验证
     */
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 指定用户是否解锁,锁定的用户无法进行身份验证
     *
     * @return
     */
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     *
     * @return
     */
    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用 ,禁用的用户不能身份验证
     *
     * @return
     */
    @JSONField(serialize = false)
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
