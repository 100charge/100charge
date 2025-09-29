package com.xingchuan.web.controller.system;

import com.xingchuan.charging.domain.model.LoginMiniBody;
import com.xingchuan.common.annotation.Log;
import com.xingchuan.common.constant.Constants;
import com.xingchuan.common.core.domain.AjaxResult;
import com.xingchuan.common.core.domain.entity.SysMenu;
import com.xingchuan.common.core.domain.entity.SysUser;
import com.xingchuan.common.core.domain.model.LoginBody;
import com.xingchuan.common.core.domain.model.LoginUser;
import com.xingchuan.common.enums.BusinessType;
import com.xingchuan.common.utils.SecurityUtils;
import com.xingchuan.framework.web.service.SysLoginService;
import com.xingchuan.framework.web.service.SysPermissionService;
import com.xingchuan.system.service.ISysMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 登录验证
 *
 * @author xingchuan
 */
@RestController
public class SysLoginController {
    @Resource
    private SysLoginService loginService;

    @Resource
    private ISysMenuService menuService;

    @Resource
    private SysPermissionService permissionService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    @Log(title = "系统用户登录", businessType = BusinessType.INSERT)
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 微信快捷登录
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @ApiOperation("微信快捷登录")
    @PostMapping("/wxQuickLogin")
    @Log(title = "微信登录", businessType = BusinessType.INSERT)
    public AjaxResult wxQuickLogin(@RequestBody LoginMiniBody loginBody) {
        // 生成令牌
        LoginUser loginUser = loginService.wxQuickLogin(loginBody.getCode());
        return AjaxResult.success(loginUser);
    }

    /**
     * 绑定微信手机号
     *
     * @param openId 用户openId
     * @param code   微信查询手机号编号
     * @return 结果
     */
    @ApiOperation("绑定微信手机号")
    @GetMapping("/appletBindMobile")
    @Log(title = "绑定微信手机号", businessType = BusinessType.INSERT)
    public AjaxResult appletBindMobile(@RequestParam("openId") String openId, @RequestParam("code") String code) {

        // 绑定
        loginService.bindMobile(openId, code);
        return AjaxResult.success(Constants.LOGIN_SUCCESS);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }

}
