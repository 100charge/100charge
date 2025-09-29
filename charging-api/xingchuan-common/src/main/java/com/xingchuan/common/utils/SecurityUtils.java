package com.xingchuan.common.utils;

import com.xingchuan.common.constant.AjaxHttpStatus;
import com.xingchuan.common.constant.Constants;
import com.xingchuan.common.core.domain.entity.AppUser;
import com.xingchuan.common.core.domain.entity.SysRole;
import com.xingchuan.common.core.domain.model.LoginUser;
import com.xingchuan.common.exception.ServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.PatternMatchUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 安全服务工具类
 */
public class SecurityUtils {

    /**
     * 用户ID
     **/
    public static Long getUserId() {
        try {
            return Objects.requireNonNull(getLoginUser()).getUserId();
        } catch (Exception e) {
            throw new ServiceException("获取用户ID异常", AjaxHttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取部门ID
     **/
    public static Long getDeptId() {
        try {
            return Objects.requireNonNull(getLoginUser()).getDeptId();
        } catch (Exception e) {
            throw new ServiceException("获取部门ID异常", AjaxHttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户账户
     **/
    public static String getUsername() {
        try {
            return Objects.requireNonNull(getLoginUser()).getUsername();
        } catch (Exception e) {
            throw new ServiceException("获取用户账户异常", AjaxHttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取小程序用户信息
     */
    public static AppUser getAppUser() {
        try {
            LoginUser loginUser = getLoginUser();
            return Objects.requireNonNull(loginUser.getAppUser());
        } catch (Exception e) {
            throw new ServiceException("获取用户信息异常", AjaxHttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取小程序用户openid
     */
    public static String getUserOpenId() {
        try {
            LoginUser loginUser = getLoginUser();
            return Objects.requireNonNull(loginUser.getAppUser().getOpenId());
        } catch (Exception e) {
            throw new ServiceException("获取用户信息异常", AjaxHttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        try {
            Authentication authentication = getAuthentication();
            if (null != authentication) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof LoginUser) {
                    return (LoginUser) principal;
                }
            }
            return null;
        } catch (Exception e) {
            throw new ServiceException("获取用户信息异常", AjaxHttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 更新SecurityContextHolder 并更新 SecurityContext
     *
     * @param userDetails
     */
    public static void updateSecurityContext(UserDetails userDetails) {
        // 更新 SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {

        SecurityContext context = SecurityContextHolder.getContext();
        if (null == context) {
            return null;
        }
        return context.getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public static boolean hasPermission(String permission) {
        return hasPermission(Objects.requireNonNull(getLoginUser()).getPermissions(), permission);
    }

    /**
     * 判断是否包含权限
     *
     * @param authorities 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    public static boolean hasPermission(Collection<String> authorities, String permission) {
        return authorities.stream().filter(StringUtils::hasText)
                .anyMatch(x -> Constants.ALL_PERMISSION.equals(x) || PatternMatchUtils.simpleMatch(x, permission));
    }

    /**
     * 验证用户是否拥有某个角色
     *
     * @param role 角色标识
     * @return 用户是否具备某角色
     */
    public static boolean hasRole(String role) {
        List<SysRole> roleList = Objects.requireNonNull(getLoginUser()).getUser().getRoles();
        Collection<String> roles = roleList.stream().map(SysRole::getRoleKey).collect(Collectors.toSet());
        return hasRole(roles, role);
    }

    /**
     * 判断是否包含角色
     *
     * @param roles 角色列表
     * @param role  角色
     * @return 用户是否具备某角色权限
     */
    public static boolean hasRole(Collection<String> roles, String role) {
        return roles.stream().filter(StringUtils::hasText)
                .anyMatch(x -> Constants.SUPER_ADMIN.equals(x) || PatternMatchUtils.simpleMatch(x, role));
    }
}
