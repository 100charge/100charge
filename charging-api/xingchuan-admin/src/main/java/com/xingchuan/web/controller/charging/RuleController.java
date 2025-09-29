package com.xingchuan.web.controller.charging;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingchuan.charging.domain.req.*;
import com.xingchuan.charging.domain.resp.RulesDetailQueryResponse;
import com.xingchuan.charging.domain.resp.RulesPageListResponse;
import com.xingchuan.charging.service.IRulesService;
import com.xingchuan.common.annotation.Log;
import com.xingchuan.common.core.controller.BaseController;
import com.xingchuan.common.core.domain.AjaxResult;
import com.xingchuan.common.core.page.TableDataInfo;
import com.xingchuan.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 计费策略Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/rule")
@Api(tags = "计费策略")
public class RuleController extends BaseController {

    @Resource
    private IRulesService rulesService;


    /**
     * 查询计费规则列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "分页查询计费规则列表")
    public TableDataInfo list(@ApiParam("计费规则名称") @RequestParam(value = "name", required = false) String name) {
        Page<RulesPageListResponse> responsePage = rulesService.selectPageList(name);
        return getDataTable(responsePage);
    }

    /**
     * 查询计费规则时段列表
     */
    @GetMapping("/listRuleTimeByRuleId")
    @ApiOperation(value = "查询计费规则时段列表")
    public AjaxResult listRuleTimeByRuleId(@RequestParam(value = "id", defaultValue = "-1") Long id) {
        return success(rulesService.listRuleTimeByRuleId(id));
    }

    /**
     * 获取计费规则详情
     */
    @GetMapping(value = "/getRuleDetail")
    @ApiOperation(value = "获取计费规则详情")
    public AjaxResult getRuleDetail(@ApiParam("计费规则id") @NotNull(message = "id不能为空!") @RequestParam("id") Long id) {
        RulesDetailQueryResponse response = rulesService.getRuleDetail(id);
        return success(response);
    }

    /**
     * 获取计费规则时段详情
     */
    @GetMapping(value = "/getRuleTimeDetail")
    @ApiOperation(value = "获取计费规则时段详情")
    public AjaxResult getRuleTimeDetail(@ApiParam("计费规则时段id") @NotNull(message = "id不能为空!") @RequestParam("id") Long id) {
        return success(rulesService.getRuleTimeDetail(id));
    }

    /**
     * 新增计费策略
     */
    @PostMapping("/addRule")
    @ApiOperation(value = "新增计费策略")
    @Log(title = "新增计费策略", businessType = BusinessType.INSERT)
    public AjaxResult addRule(@RequestBody @Validated RulesAddRequest request) {
        rulesService.addRule(request);
        return success();
    }

    /**
     * 新增计费策略时段
     */
    @PostMapping("/addRuleTime")
    @ApiOperation(value = "新增计费策略时段")
    @Log(title = "新增计费策略时段", businessType = BusinessType.INSERT)
    public AjaxResult addRuleTime(@RequestBody @Validated RuleTimeAddRequest request) {
        rulesService.addRuleTime(request);
        return success();
    }

    /**
     * 修改计费策略
     */
    @PostMapping("/updateRule")
    @ApiOperation(value = "修改计费策略")
    @Log(title = "修改计费策略", businessType = BusinessType.UPDATE)
    public AjaxResult updateRule(@RequestBody @Validated RulesUpdateRequest request) {
        rulesService.updateRule(request);
        return success();
    }

    /**
     * 修改计费策略时段
     */
    @PostMapping("/updateRuleTime")
    @ApiOperation(value = "修改计费策略时段")
    @Log(title = "修改计费策略时段", businessType = BusinessType.UPDATE)
    public AjaxResult updateRuleTime(@RequestBody @Validated RuleTimeUpdateRequest request) {
        rulesService.updateRuleTime(request);
        return success();
    }

    /**
     * 删除计费策略
     */
    @DeleteMapping("/delRule/{id}")
    @ApiOperation(value = "删除计费策略")
    @Log(title = "删除计费策略", businessType = BusinessType.DELETE)
    public AjaxResult delRule(@ApiParam("计费规则id") @NotNull(message = "id不能为空!") @PathVariable("id") Long id) {
        rulesService.delRule(id);
        return success();
    }

    /**
     * 删除计费策略时段
     */
    @DeleteMapping("/delRuleTime/{id}")
    @ApiOperation(value = "删除计费策略时段")
    @Log(title = "删除计费策略时段", businessType = BusinessType.DELETE)
    public AjaxResult delRuleTime(@ApiParam("计费规则时段id") @NotNull(message = "id不能为空!") @PathVariable("id") Long id) {
        rulesService.delRuleTime(id);
        return success();
    }

    /**
     * 计费规则下拉列表
     */
    @GetMapping("/dropDownList")
    @ApiOperation(value = "计费规则下拉列表")
    public AjaxResult dropDownList(@ApiParam("所属运营商") @RequestParam(value = "tenantId", required = false) Long tenantId,
                                   @ApiParam("计费规则名称") @RequestParam(value = "name", required = false) String name) {
        List<RuleDropDownListResponse> responseList = rulesService.dropDownList(tenantId, name);
        return success(responseList);
    }
}
