package com.xingchuan.web.controller.charging;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingchuan.charging.domain.excel.ChargingPileExcelImport;
import com.xingchuan.charging.domain.req.ChargingPileAddRequest;
import com.xingchuan.charging.domain.req.ChargingPilePageListRequest;
import com.xingchuan.charging.domain.req.ChargingPileUpdateRequest;
import com.xingchuan.charging.domain.resp.ChargingPileDetailResponse;
import com.xingchuan.charging.domain.resp.ChargingPilePageListResponse;
import com.xingchuan.charging.service.IChargingPileService;
import com.xingchuan.common.annotation.Log;
import com.xingchuan.common.annotation.RepeatSubmit;
import com.xingchuan.common.constant.MessageConstants;
import com.xingchuan.common.core.controller.BaseController;
import com.xingchuan.common.core.domain.AjaxResult;
import com.xingchuan.common.core.page.TableDataInfo;
import com.xingchuan.common.enums.BusinessType;
import com.xingchuan.common.utils.StringUtils;
import com.xingchuan.common.utils.WxQRCodeUtil;
import com.xingchuan.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 充电桩管理Controller
 *
 * @author ruoyi
 */
@Slf4j
@RestController
@RequestMapping("/chargingPile")
@Api(tags = "充电桩管理")
public class ChargingPileController extends BaseController {

    @Resource
    private IChargingPileService chargingPileService;


    /**
     * 查询充电桩列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "分页查询充电桩列表")
    public TableDataInfo list(ChargingPilePageListRequest request) {
        Page<ChargingPilePageListResponse> responsePage = chargingPileService.selectPageList(request);
        return getDataTable(responsePage);
    }

    /**
     * 获取充电桩详情
     */
    @GetMapping(value = "/getChargingPileDetail")
    @ApiOperation(value = "获取充电桩详情")
    public AjaxResult getChargingPileDetail(@ApiParam("桩id") @NotNull(message = "id不能为空!") @RequestParam("id") Long id) {
        ChargingPileDetailResponse response = chargingPileService.getChargingPileDetail(id);
        return success(response);
    }

    /**
     * 新增充电桩
     */
    @RepeatSubmit
    @PostMapping("/addChargingPile")
    @ApiOperation(value = "新增充电桩")
    @Log(title = "新增充电桩", businessType = BusinessType.INSERT)
    public AjaxResult addChargingPile(@RequestBody @Validated ChargingPileAddRequest request) {
        chargingPileService.addChargingPile(request);
        return success();
    }

    /**
     * 修改充电桩
     */
    @RepeatSubmit
    @PostMapping("/updateChargingPile")
    @ApiOperation(value = "修改充电桩")
    @Log(title = "修改充电桩", businessType = BusinessType.UPDATE)
    public AjaxResult updateChargingPile(@RequestBody @Validated ChargingPileUpdateRequest request) {
        chargingPileService.updateChargingPile(request);
        return success();
    }

    /**
     * 删除充电桩
     */
    @RepeatSubmit
    @ApiOperation(value = "删除充电桩")
    @DeleteMapping("/delChargingPile/{id}")
    @Log(title = "删除充电桩", businessType = BusinessType.DELETE)
    public AjaxResult delChargingPile(@ApiParam("充电桩id") @NotNull(message = "id不能为空!") @PathVariable Long id) {
        chargingPileService.delChargingPile(id);
        return success();
    }

    /**
     * 新增充电枪
     */
    @RepeatSubmit
    @PostMapping("/addChargingGun/{id}")
    @ApiOperation(value = "新增充电枪")
    @Log(title = "新增充电枪", businessType = BusinessType.INSERT)
    public AjaxResult addChargingGun(@ApiParam("充电桩id") @NotNull(message = "id不能为空!") @PathVariable("id") Long id) {
        chargingPileService.addChargingGun(id);
        return success();
    }

    /**
     * 删除充电枪
     */
    @RepeatSubmit
    @DeleteMapping("/delChargingGun/{id}")
    @ApiOperation(value = "删除充电枪")
    @Log(title = "删除充电枪", businessType = BusinessType.DELETE)
    public AjaxResult delChargingGun(@ApiParam("充电枪id") @NotNull(message = "id不能为空!") @PathVariable Long id) {
        chargingPileService.delChargingGun(id);
        return success();
    }

    /**
     * 充电桩-修改启用、停用
     */
    @RepeatSubmit
    @ApiOperation("充电桩-修改启用、停用")
    @GetMapping("/updatePileStatus/{id}")
    @Log(title = "充电桩-修改启用、停用", businessType = BusinessType.UPDATE)
    public AjaxResult updateShowStatus(@NotNull(message = MessageConstants.CHARGING_PILE_NOT_EXIST) @PathVariable Long id) {
        chargingPileService.updateShowStatus(id);
        return success();
    }

    @RepeatSubmit
    @PostMapping("/importData")
    @PreAuthorize("@ss.hasPermission('device:pile:import')")
    @Log(title = "充电桩管理-excel导入", businessType = BusinessType.IMPORT)
    @ApiOperation(value = "充电桩管理-excel导入")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<ChargingPileExcelImport> util = new ExcelUtil<>(ChargingPileExcelImport.class);
        List<ChargingPileExcelImport> excelImportList = util.importExcel(file.getInputStream());
        String message = chargingPileService.importChargingPile(excelImportList);
        if (StringUtils.isBlank(message)) {
            return success();
        } else {
            return error(message);
        }
    }

    /**
     * 生成微信二维码
     */
    @RepeatSubmit
    @GetMapping("/generateQRCodeZip")
    @ApiOperation(value = "平台-充电桩生成二维码压缩包 ”桩号_枪号“拼接")
    @Log(title = "平台-充电桩生成二维码压缩包", businessType = BusinessType.QRCODE)
    public void generateQRCodeZip(@RequestParam(value = "pileNo", defaultValue = "-1") String pileNo,
                                  HttpServletResponse servletResponse) {

        chargingPileService.generateQRCodeZip(pileNo, servletResponse);
    }

    /**
     * 生成微信二维码
     */
    @RepeatSubmit
    @GetMapping("/generateQRCode")
    @ApiOperation(value = "平台-充电桩生成二维码 ”桩号_枪号“拼接")
    @Log(title = "平台-充电桩生成二维码", businessType = BusinessType.QRCODE)
    public void generateQRCode(@ApiParam("桩编码") @NotNull(message = "桩编码不能为空!") @RequestParam("pileNo") String pileNo,
                               @ApiParam("枪编码") @NotNull(message = "枪编码不能为空!") @RequestParam("gunNo") String gunNo,
                               HttpServletResponse servletResponse) {
        try {
            String content = String.format("\"https://wxapi.xxx.com?code=%sG%s\"", pileNo, gunNo);
            if (StringUtils.isBlank(content)) {
                return;
            }
            content = content.trim();
            //核心代码-生成二维码
            WxQRCodeUtil.createCodeToOutputStream(content, servletResponse.getOutputStream());
        } catch (Exception e) {
            log.error("平台-生成二维码失败", e);
            throw new RuntimeException("平台-二维码生成失败，请查看日志");
        }
    }

}
