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
import com.xingchuan.common.utils.PhoneNumberUtils;
import com.xingchuan.common.utils.SecurityUtils;
import com.xingchuan.framework.web.service.SysLoginService;
import com.xingchuan.framework.web.service.SysPermissionService;
import com.xingchuan.system.service.ISysMenuService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
     * 发送短信验证码
     *
     * @param phoneNumber 手机号
     * @return AjaxResult
     */
    @ApiOperation(value = "发送短信验证码", notes = "发送短信验证码到指定手机号")
    @GetMapping("/sms/send")
    public AjaxResult sendSms(@ApiParam(value = "手机号", required = true) @RequestParam("phoneNumber") String phoneNumber) {
        // 参数校验
        if (!PhoneNumberUtils.isValidPhoneNumber(phoneNumber)) {
            return AjaxResult.error("手机号格式异常");
        }
        loginService.sendSms(phoneNumber);
        return AjaxResult.success("验证码发送成功");
    }

        /**
     * 短信验证码登录接口
     *
     * @param phoneNumber 手机号
     * @param code        验证码
     * @return AjaxResult
     */
    @ApiOperation(value = "短信验证码登录", notes = "使用手机号和短信验证码登录")
    @GetMapping("/loginWithSms")
    public AjaxResult loginWithSms(@ApiParam(value = "手机号", required = true) @RequestParam("phoneNumber") String phoneNumber,
                                   @ApiParam(value = "验证码", required = true) @RequestParam("code") String code) {
        if (!PhoneNumberUtils.isValidPhoneNumber(phoneNumber)) {
            return AjaxResult.error("手机号格式异常");
        }
        if (code == null || code.isEmpty()) {
            return AjaxResult.error("验证码不能为空");
        }
        AjaxResult ajax = AjaxResult.success();
        String token = loginService.loginWithSms(phoneNumber, code);

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
