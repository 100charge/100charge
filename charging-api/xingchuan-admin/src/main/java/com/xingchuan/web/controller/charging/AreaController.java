package com.xingchuan.web.controller.charging;

import com.xingchuan.charging.service.IAreaService;
import com.xingchuan.common.core.controller.BaseController;
import com.xingchuan.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


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

}
