package com.v02.minback.controller;

import com.v02.minback.model.param.AccessLogGetAllParam;
import com.v02.minback.model.result.RestResult;
import com.v02.minback.service.front.LogFrontService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final LogFrontService logFrontService;

    @Operation(summary = "AccessLog 페이징", description = "AccessLog 페이징된 데이터를 조회합니다.")
    @PostMapping("/accessLog/paging")
    public RestResult getAccessLogs(@RequestBody AccessLogGetAllParam param){


        return logFrontService.getAccessLogs(param);
    }





}
