package com.xingchuan.web.controller.charging;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingchuan.charging.domain.resp.UserBalanceRecordListResponse;
import com.xingchuan.charging.service.IAppUserBalanceRecordService;
import com.xingchuan.common.core.controller.BaseController;
import com.xingchuan.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;

/**
 * 小程序用户余额管理
 */
@Api(tags = "小程序用户余额管理")
@RestController
@RequestMapping("/appUserBalance")
public class AppUserBalanceController extends BaseController {

    @Resource
    private IAppUserBalanceRecordService userBalanceRecordService;


    /**
     * 小程序用户余额变动列表
     */
    @GetMapping("/list")
    @ApiOperation("小程序用户余额变动列表")
    public TableDataInfo pageList(@ApiParam("操作类型") @RequestParam(value = "type", required = false) Integer type,
                                  @ApiParam("手机号") @RequestParam(value = "phone", required = false) String phone) {
        Page<UserBalanceRecordListResponse> page = userBalanceRecordService.pageList(type, phone);
        return getDataTable(page);
    }

    /**
     * 小程序用户余额充值记录列表
     */
    @GetMapping("/rechargeRecordList")
    @ApiOperation("小程序用户余额充值记录列表")
    public TableDataInfo rechargeRecordList(@NotBlank(message = "openId不能为空") @ApiParam("openId") @RequestParam(value = "openId") String openId) {
        Page<UserBalanceRecordListResponse> page = userBalanceRecordService.rechargeRecordList(openId);
        return getDataTable(page);
    }
}
