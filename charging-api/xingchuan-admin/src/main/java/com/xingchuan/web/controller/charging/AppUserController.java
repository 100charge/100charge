package com.xingchuan.web.controller.charging;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingchuan.charging.domain.excel.AppUserExportExcel;
import com.xingchuan.charging.domain.req.*;
import com.xingchuan.charging.domain.resp.AppEnterpriseUserListResponse;
import com.xingchuan.charging.domain.resp.AppUserInfoResponse;
import com.xingchuan.charging.domain.resp.AppUserListResponse;
import com.xingchuan.charging.domain.resp.UserCarPageResponse;
import com.xingchuan.charging.service.IAppUserCarService;
import com.xingchuan.charging.service.IAppUserService;
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
    private IAppUserCarService appUserCarService;


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
     * 查询用户车辆列表
     */
    @GetMapping("/userCarList")
    @ApiOperation("查询用户车辆列表-分页")
    public TableDataInfo userCarList(UserCarPageRequest request) {
        Page<UserCarPageResponse> page = appUserCarService.userCarList(request);
        return getDataTable(page);
    }

    /**
     * 用户车辆车辆列表
     */
    @GetMapping("/listAppUserCar")
    @ApiOperation("小程序管理-用户车辆车辆列表")
    public AjaxResult listAppUserCar() {
        return success(appUserCarService.listAppUserCar());
    }

    /**
     * 添加车辆
     */
    @PostMapping("/addCar")
    @ApiOperation("小程序管理-添加车辆")
    @Log(title = "小程序-添加车辆", businessType = BusinessType.INSERT)
    public AjaxResult addCar(@Validated @RequestBody AppUserCarAddRequest request) {
        return toAjax(appUserCarService.addAppUserCar(request));
    }

    /**
     * 修改车牌号
     */
    @PostMapping("/editPlateNo")
    @ApiOperation(value = "小程序管理-修改车牌号")
    @Log(title = "小程序-修改车牌号", businessType = BusinessType.UPDATE)
    public AjaxResult editPlateNo(@Validated @RequestBody AppUserCarUpdateRequest request) {
        return toAjax(appUserCarService.editPlateNo(request));
    }

    /**
     * 删除车辆
     */
    @GetMapping("/deleteById")
    @ApiOperation(value = "小程序管理-删除车辆")
    @Log(title = "小程序-删除车牌号", businessType = BusinessType.DELETE)
    public AjaxResult deleteById(@RequestParam(value = "carId", defaultValue = "-1") Long carId) {
        return toAjax(appUserCarService.deleteById(carId));
    }

}
