package com.xingchuan.web.controller.charging;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingchuan.charging.domain.req.*;
import com.xingchuan.charging.domain.resp.ChargingStationsPageResponse;
import com.xingchuan.charging.domain.resp.StationsPageResponse;
import com.xingchuan.charging.domain.resp.StationsResponse;
import com.xingchuan.charging.enums.ChargePileEnum;
import com.xingchuan.charging.service.IChargingStationsService;
import com.xingchuan.common.annotation.Log;
import com.xingchuan.common.annotation.RepeatSubmit;
import com.xingchuan.common.constant.MessageConstants;
import com.xingchuan.common.core.controller.BaseController;
import com.xingchuan.common.core.domain.AjaxResult;
import com.xingchuan.common.core.page.TableDataInfo;
import com.xingchuan.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 场站Controller
 *
 * @author ruoyi
 */
@RestController
@Api(tags = "admin-场站管理")
@RequestMapping("/stations")
public class ChargingStationsController extends BaseController {
    @Autowired
    private IChargingStationsService chargingStationsService;

    /**
     * 查询场站列表
     */
    @GetMapping("/list")
    @ApiOperation("场站管理-查询分页")
    public TableDataInfo list(ChargingStationsPageRequest request) {
        Page<ChargingStationsPageResponse> page = chargingStationsService.selectChargingStationsList(request);
        return getDataTable(page);
    }

    /**
     * 根据id查询场站信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("场站管理-查询详情")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        if (ObjectUtils.isEmpty(id)) {
            return error("场站信息不存在");
        }
        return success(chargingStationsService.selectChargingStationsById(id));
    }

    /**
     * 场站管理-新增
     */
    @RepeatSubmit
    @PostMapping
    @ApiOperation("场站管理-新增")
    @Log(title = "场站管理-新增", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody @Validated ChargingStationsAddRequest request) {
        return toAjax(chargingStationsService.insertChargingStations(request));
    }

    /**
     * 场站管理-修改
     */
    @RepeatSubmit
    @PutMapping
    @ApiOperation("场站管理-修改")
    @Log(title = "场站管理-修改", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@RequestBody @Validated ChargingStationsAddRequest request) {
        if (ObjectUtils.isEmpty(request.getId())) {
            return error(MessageConstants.CHARGING_STATIONS_NOT_EXIST);
        }

        return toAjax(chargingStationsService.updateChargingStations(request));
    }

    /**
     * 场站管理-删除
     */
    @RepeatSubmit
    @DeleteMapping("/{id}")
    @ApiOperation("场站管理-删除")
    @Log(title = "场站管理-修改", businessType = BusinessType.DELETE)
    public AjaxResult delete(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            return error(MessageConstants.CHARGING_STATIONS_NOT_EXIST);
        }
        chargingStationsService.deleteChargingStations(id);
        return success();
    }

    /**
     * 生成场站下所有枪的微信二维码
     */
    @RepeatSubmit
    @GetMapping("/generateQRCodeZip")
    @ApiOperation(value = "平台-充电桩生成二维码压缩包 ”场站名称:桩号_枪号“拼接")
    @Log(title = "平台-充电桩生成二维码压缩包", businessType = BusinessType.QRCODE)
    public void generateQRCodeZip(@ApiParam("场站id") @NotNull(message = MessageConstants.CHARGING_STATIONS_NOT_EXIST) @RequestParam(value = "stationId") Long stationId,
                                  HttpServletResponse servletResponse) {

        chargingStationsService.generateQRCodeZip(stationId, servletResponse);
    }

    /**
     * 场站管理-是否展示状态切换
     */
    @RepeatSubmit
    @ApiOperation("场站管理-是否展示状态切换")
    @PutMapping("/updateShowStatus")
    @Log(title = "场站管理-是否展示状态切换", businessType = BusinessType.UPDATE)
    public AjaxResult updateShowStatus(@Validated @RequestBody EnableOrDisableRequest request) {
        chargingStationsService.updateShowStatus(request.getId());
        return success();
    }

    /**
     * 查询场站下拉列表
     */
    @GetMapping(value = "/getStationList")
    @ApiOperation("查询场站下拉列表")
    public AjaxResult getStationList() {
        List<StationsResponse> response = chargingStationsService.getStationList();
        return success(response);
    }

    /**
     * 根据运营商查询场站下拉列表
     */
    @GetMapping(value = "/getListByTenantId")
    @ApiOperation("根据运营商查询场站下拉列表")
    public AjaxResult getListByTenantId() {
        List<StationsResponse> response = chargingStationsService.getListByTenantId();
        return success(response);
    }

    /**
     * 查询小程序场站列表
     */
    @GetMapping("/miniStationList")
    @ApiOperation("查询小程序场站列表")
    public TableDataInfo miniStationList(@Validated StationsPageRequest request) {
        Page<StationsPageResponse> page = chargingStationsService.selectMiniStationList(request);
        return getDataTable(page);
    }

    /**
     * 查询地图分页
     */
    @GetMapping("/listMapStation")
    @ApiOperation("场站管理-查询地图分页")
    public AjaxResult listMapStation(@Validated MapChargingStationsPageRequest request) {
        return success(chargingStationsService.listMapStation(request));
    }

    /**
     * 小程序根据id查询场站信息
     */
    @ApiOperation("小程序根据id查询场站信息")
    @GetMapping(value = "/getMiniStationDetailById")
    public AjaxResult getMiniStationDetailById(@Validated ChargingStationsDetailRequest request) {
        return success(chargingStationsService.selectMiniStationDetailById(request));
    }

    /**
     * 根据场站id查询计费费用列表
     */
    @ApiOperation("场站管理-根据场站id查询计费费用列表")
    @GetMapping(value = "/getStationRuleById")
    public AjaxResult getStationRuleById(@RequestParam(value = "stationId", defaultValue = "-1") Long stationId) {
        return success(chargingStationsService.getStationRuleById(stationId));
    }

    /**
     * 场站管理-根据场站id查询电桩信息
     */
    @ApiOperation("场站管理-根据场站id查询电桩信息")
    @GetMapping(value = "/getStationDevice")
    public AjaxResult getStationDevice(@RequestParam(value = "stationId", defaultValue = "-1") Long stationId,
                                       @RequestParam(value = "type", required = false) ChargePileEnum type) {
        return success(chargingStationsService.getStationDevice(stationId, type));
    }

    /**
     * 场站管理-扫码确定充电枪
     *
     * @param code 二维码参数
     */
    @ApiOperation("场站管理-扫码确定充电枪")
    @GetMapping(value = "/readyCharge")
    public AjaxResult readyCharge(@RequestParam(value = "code", defaultValue = "") String code) {
        return success(chargingStationsService.readyCharge(code));
    }

}
