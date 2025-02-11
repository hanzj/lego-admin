package com.lego.system.controller;

import com.lego.core.vo.JsonResponse;
import com.lego.core.web.BaseController;
import com.lego.system.dto.SysColumnSortInfo;
import com.lego.system.service.ISysColumnSortService;
import com.lego.system.service.ISysCustomFieldService;
import com.lego.system.vo.SysColumnSortModifyVO;
import com.lego.system.vo.SysColumnWidthModifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/back-end/sys-column-sort")
public class SysColumnSortController extends BaseController {

    @Autowired
    private ISysColumnSortService columnSortService;

    @Autowired
    private ISysCustomFieldService customFieldService;

    @GetMapping("/list")
    public JsonResponse<List<SysColumnSortInfo>> list(String formCode) {
        return JsonResponse.success(columnSortService.findAndInitBy(formCode, getLoginCode()));
    }

    @PostMapping("/modify-all")
    public JsonResponse<Object> modifyAll(@RequestBody List<SysColumnSortModifyVO> vos) {
        columnSortService.update(getLoginCode(), vos);
        return JsonResponse.success();
    }

    @PostMapping("/modify-width")
    public JsonResponse<Object> modifyWidth(@RequestBody SysColumnWidthModifyVO vo) {
        columnSortService.updateWidth(getLoginCode(), vo);
        return JsonResponse.success();
    }

}
