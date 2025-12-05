package com.xingchuan.web.controller.charging;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xingchuan.charging.domain.req.*;
import com.xingchuan.charging.domain.resp.*;    
import com.xingchuan.charging.service.IReportFormService;
import com.xingchuan.common.core.controller.BaseController;
import com.xingchuan.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@Api(tags = "报表管理")
@RequestMapping("/reportForm")
public class ReportFormController extends BaseController {

    @Resource
    private IReportFormService reportFormService;

    /***
     * 账户充值明细
     */
    @GetMapping("/accountRechargeDetailList")
    @ApiOperation("账户充值明细")
    public TableDataInfo accountRechargeDetailList(AccountRechargeDetailRequest request){
        Page<AccountRechargeDetailResponse> page = reportFormService.accountRechargeDetailList(request);
        return getDataTable(page);
    }

}
