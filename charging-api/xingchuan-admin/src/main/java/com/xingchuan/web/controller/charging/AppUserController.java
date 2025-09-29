package com.xingchuan.web.controller.charging;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingchuan.charging.domain.excel.AppUserExportExcel;
import com.xingchuan.charging.domain.req.AppUserDisableOrEnableRequest;
import com.xingchuan.charging.domain.req.AppUserQueryRequest;
import com.xingchuan.charging.domain.req.CompanyUserQueryRequest;
import com.xingchuan.charging.domain.resp.AppEnterpriseUserListResponse;
import com.xingchuan.charging.domain.resp.AppUserInfoResponse;
import com.xingchuan.charging.domain.resp.AppUserListResponse;
import com.xingchuan.charging.domain.resp.UserWithdrawalListResponse;
import com.xingchuan.charging.service.IAppUserService;
import com.xingchuan.charging.service.IUserWithdrawalRequestService;
import com.xingchuan.common.annotation.Log;
import com.xingchuan.common.annotation.RepeatSubmit;
import com.xingchuan.common.core.controller.BaseController;
import com.xingchuan.common.core.domain.AjaxResult;
import com.xingchuan.common.core.domain.entity.AppUser;
import com.xingchuan.common.core.page.TableDataInfo;
import com.xingchuan.common.enums.BusinessType;
import com.xingchuan.common.utils.SecurityUtils;
import com.xingchuan.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * app用户Controller
 *
 * @author ruoyi
 */
@Api(tags = "小程序用户管理")
@RestController
@RequestMapping("/appUser")
public class AppUserController extends BaseController {

    @Autowired
    private IAppUserService appUserService;
    @Autowired
    private IUserWithdrawalRequestService userWithdrawalRequestService;


    /**
     * 查询企业用户列表
     */
    @GetMapping("/companyUserList")
    @ApiOperation("查询企业用户列表-分页")
    public TableDataInfo companyUserList(CompanyUserQueryRequest request) {
        Page<AppEnterpriseUserListResponse> page = appUserService.platformCompanyUserList(request);
        return getDataTable(page);
    }

    /**
     * 查询小程序-用户列表
     */
    @GetMapping("/list")
    @ApiOperation("小程序用户管理-分页")
    public TableDataInfo list(AppUserQueryRequest request) {
        Page<AppUserListResponse> page = appUserService.selectAppUserList(request);
        return getDataTable(page);
    }

    /**
     * 导出小程序用户列表
     */
    @RepeatSubmit
    @ApiOperation("小程序用户管理-导出")
    @PostMapping("/miniProgramUserExport")
    @Log(title = "小程序用户管理-导出", businessType = BusinessType.EXPORT)
    public void miniProgramUserExport(HttpServletResponse response, AppUserQueryRequest request) {
        List<AppUserExportExcel> exportList = appUserService.miniProgramUserExport(request);
        ExcelUtil<AppUserExportExcel> excelUtil = new ExcelUtil<>(AppUserExportExcel.class);
        excelUtil.exportExcel(response, exportList, "小程序用户列表");
    }

    /**
     * 小程序用户-启用禁用、冻结
     */
    @RepeatSubmit
    @ApiOperation("小程序用户-启用禁用、冻结")
    @Log(title = "小程序用户-启用禁用、冻结", businessType = BusinessType.UPDATE)
    @PutMapping("/appUserDisableOrEnable")
    public AjaxResult appUserDisableOrEnable(@RequestBody @Validated AppUserDisableOrEnableRequest request) {
        appUserService.appUserDisableOrEnable(request);
        return success();
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/getUserInfo")
    @ApiOperation("小程序管理-获取用户信息")
    public AjaxResult getUserInfo() {
        AppUser appUser = SecurityUtils.getAppUser();
        String userOpenId = appUser.getOpenId();

        AppUserInfoResponse userInfo = appUserService.getUserInfo(userOpenId);
        return success(userInfo);
    }

    /**
     * 获取用户余额
     */
    @GetMapping("/getUserBalance")
    @ApiOperation("小程序管理-获取用户余额")
    public AjaxResult getUserBalance(@RequestParam @JsonFormat(pattern = "yyyy-MM") Date queryDate) {
        return success(appUserService.getUserBalance(queryDate, SecurityUtils.getUserOpenId()));
    }

    /**
     * 分页查询用户提现申请记录
     */
    @GetMapping("/getWithdrawalApplicationPage")
    @ApiOperation(value = "分页查询用户提现申请记录")
    public TableDataInfo queryByPage() {
        String userOpenId = SecurityUtils.getUserOpenId();
        Page<UserWithdrawalListResponse> response = userWithdrawalRequestService.queryByPage(userOpenId);
        return getDataTable(response);
    }

}
