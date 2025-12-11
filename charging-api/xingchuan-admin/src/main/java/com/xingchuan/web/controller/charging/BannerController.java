package com.xingchuan.web.controller.charging;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingchuan.charging.domain.entity.Banner;
import com.xingchuan.charging.domain.req.BannerAddRequest;
import com.xingchuan.charging.domain.req.BannerOpenRequest;
import com.xingchuan.charging.domain.req.BannerPageRequest;
import com.xingchuan.charging.domain.req.BannerUpdateRequest;
import com.xingchuan.charging.domain.resp.BannerListResponse;
import com.xingchuan.charging.domain.resp.NoticeDetailResponse;
import com.xingchuan.charging.service.IBannerService;
import com.xingchuan.common.annotation.Anonymous;
import com.xingchuan.common.annotation.Log;
import com.xingchuan.common.annotation.RepeatSubmit;
import com.xingchuan.common.core.controller.BaseController;
import com.xingchuan.common.core.domain.AjaxResult;
import com.xingchuan.common.core.page.TableDataInfo;
import com.xingchuan.common.enums.BusinessType;
import com.xingchuan.system.service.ISysNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/banner")
@Api(tags = "banner管理")
public class BannerController extends BaseController {

    @Resource
    private IBannerService bannerService;

    @Resource
    private ISysNoticeService noticeService;


    /**
     * 分页获取banner列表
     */
    @GetMapping(value = "/getBannerList")
    @Anonymous
    @ApiOperation("分页获取banner列表")
    public AjaxResult getBannerList(BannerPageRequest request) {
        Page<BannerListResponse> responsePage = bannerService.selectPageList(request.getName(), request.getOpen());
        TableDataInfo table=getDataTable(responsePage);
        return success(table.getRows());
    }

    @GetMapping(value = "/getBannerPage")
    @Anonymous
    @ApiOperation("分页获取banner列表")
    public TableDataInfo getBannerPage(BannerPageRequest request) {
        Page<BannerListResponse> responsePage = bannerService.selectPageList(request.getName(), request.getOpen());
        return getDataTable(responsePage);
    }

    /**
     * 获取banner详情
     */
    @GetMapping(value = "/getBannerDetail")
    @ApiOperation(value = "获取banner详情")
    public AjaxResult getBannerDetail(@ApiParam("banner id") @NotNull(message = "id不能为空!") @RequestParam("id") Long id) {
        Banner banner = bannerService.selectBannerById(id);
        return success(banner);
    }

    /**
     * 新增banner
     */
    @RepeatSubmit
    @PostMapping("/addBanner")
    @ApiOperation(value = "新增banner")
    @Log(title = "新增banner", businessType = BusinessType.INSERT)
    public AjaxResult addBanner(@RequestBody @Validated BannerAddRequest request) {
        bannerService.insertBanner(request);
        return success();
    }

    /**
     * 修改banner
     */
    @RepeatSubmit
    @PostMapping("/updateBanner")
    @ApiOperation(value = "修改banner")
    @Log(title = "修改banner", businessType = BusinessType.UPDATE)
    public AjaxResult updateBanner(@RequestBody @Validated BannerUpdateRequest request) {
        bannerService.updateBanner(request);
        return success();
    }

    /**
     * 修改banner
     */
    @RepeatSubmit
    @PostMapping("/openBanner")
    @ApiOperation(value = "开启关闭banner")
    @Log(title = "开启关闭banner", businessType = BusinessType.UPDATE)
    public AjaxResult openBanner(@RequestBody @Validated BannerOpenRequest request) {
        bannerService.openBanner(request);
        return success();
    }

    /**
     * 删除banner
     */
    @RepeatSubmit
    @DeleteMapping("/delBanner/{id}")
    @ApiOperation(value = "删除banner")
    @Log(title = "删除banner", businessType = BusinessType.DELETE)
    public AjaxResult delBanner(@ApiParam("banner id") @NotNull(message = "id不能为空!") @PathVariable("id") Long id) {
        bannerService.deleteBannerById(id);
        return success();
    }

    /**
     * 获取小程序banner
     */
    @GetMapping(value = "/getMiniBannerList")
    @ApiOperation("获取小程序banner")
    public AjaxResult getMiniBannerList() {
        return success(bannerService.selectBannerListMini());
    }

    /**
     * 获取文章详情
     */
    @GetMapping(value = "/getNoticeDetail")
    @ApiOperation("获取文章详情")
    public AjaxResult getNoticeDetail(@ApiParam("文章id") @NotNull(message = "id不能为空") @RequestParam("noticeId") Long id) {
        NoticeDetailResponse response = noticeService.getNoticeDetail(id);
        return success(response);
    }
}
