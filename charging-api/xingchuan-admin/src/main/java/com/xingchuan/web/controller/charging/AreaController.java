package com.xingchuan.web.controller.charging;

import com.xingchuan.charging.domain.resp.AreaListResponse;
import com.xingchuan.charging.service.IAreaService;
import com.xingchuan.common.core.controller.BaseController;
import com.xingchuan.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/area")
@Api(tags = "区域管理")
public class AreaController extends BaseController {

    @Resource
    private IAreaService areaService;

    /**
     * 获取开放的城市
     */
    @GetMapping(value = "/getOpenCities")
    @ApiOperation("获取开放的城市")
    public AjaxResult getOpenCities() {
        return success(areaService.getOpenCities());
    }

    /**
     * 获取省市区下拉列表
     */
    @GetMapping(value = "/getDropdownList")
    @ApiOperation("获取省市区下拉列表")
    public AjaxResult getDropdownList() {
        return success(areaService.selectAreaTreeList());
    }

    /**
     * 获取省级下拉列表
     */
    @GetMapping(value = "/provinceList")
    @ApiOperation("获取省下拉列表")
    public AjaxResult provinceList() {
        List<AreaListResponse> responseList = areaService.provinceList();
        return success(responseList);
    }

    /**
     * 根据省级id获取市级下拉列表
     */
    @GetMapping(value = "/getCityListByProvinceId")
    @ApiOperation("根据省级id获取市级下拉列表")
    public AjaxResult getCityListByProvinceId(@ApiParam("省id") @NotNull(message = "id不能为空!") @RequestParam(value = "provinceId") String provinceId) {
        List<AreaListResponse> responseList = areaService.getCityListByProvinceId(provinceId);
        return success(responseList);
    }

    /**
     * 根据市级id获取区级下拉列表
     */
    @GetMapping(value = "/getDistrictListByCityId")
    @ApiOperation("根据市级id获取区级下拉列表")
    public AjaxResult getDistrictListByCityId(@ApiParam("市id") @NotNull(message = "id不能为空!") @RequestParam(value = "cityId") String cityId) {
        List<AreaListResponse> responseList = areaService.getDistrictListByCityId(cityId);
        return success(responseList);
    }

}
