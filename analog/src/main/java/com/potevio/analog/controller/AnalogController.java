package com.potevio.analog.controller;

import com.potevio.analog.entity.PageResult;
import com.potevio.analog.entity.Result;
import com.potevio.analog.entity.StatusCode;
import com.potevio.analog.pojo.AnalogData;
import com.potevio.analog.pojo.RealResponseData;
import com.potevio.analog.pojo.RequestOrderData;
import com.potevio.analog.pojo.TerminalData;
import com.potevio.analog.service.AnalogService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/analog")
@CrossOrigin
@Api(value = "终端Controller", tags = "AnalogController") //获取终端信息的控制器
public class AnalogController {

    @Autowired
    private AnalogService service;

    @GetMapping(value = "/scanterminals")
    @ApiOperation(value = "获取所有终端的信息列表")
    @ApiIgnore
    public Result<List<TerminalData>> getTerminalList() {
        List<TerminalData> terminals = service.getTerminalScanList();
        return new Result(true, StatusCode.OK, "获取终端列表", terminals);
    }

    @GetMapping("/terminalpage/{page}/{size}")
    @ApiOperation(value = "分页获取终端的信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "分页索引", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "size", value = "页大小", required = true, dataType = "int")
    })
    public Result<PageResult<TerminalData>> getTerminalPage(@PathVariable("page") int page, @PathVariable("size") int size) {
        PageResult<TerminalData> pageData = service.getTerminalPageData3(page, size);
        return new Result(true, StatusCode.OK, "获取终端列表", pageData);
    }

    @GetMapping("/stations")
    @ApiOperation(value = "获取基站列表")
    public Result<List<String>> getStationDatas() {
        List<String> stations = service.getStationDatas();
        return new Result(true, StatusCode.OK, "获取基站列表", stations);
    }

    @GetMapping("/stations/{id}")
    @ApiOperation(value = "获取基站下的终端列表")
    @ApiImplicitParam(paramType = "id", name = "id", value = "基站ID", required = true, dataType = "String")
    public Result<List<TerminalData>> getTerminalDatas(@PathVariable String id) {
        List<TerminalData> terminals = service.getTerminalDatas(id);
        return new Result(true, StatusCode.OK, "获取基站列表", terminals);
    }

    @GetMapping("/terminal/{ip}")
    @ApiOperation(value = "获取单个终端完整信息")
    @ApiImplicitParam(paramType = "query", name = "ip", value = "终端主键IP", required = true, dataType = "String")
    @ApiIgnore
    public Result<TerminalData> getTerminalData(@PathVariable String ip) {
        TerminalData terminal = service.getTerminalData(ip);
        return new Result(true, StatusCode.OK, "获取终端", terminal);
    }

    @GetMapping("/find")
    @ApiOperation(value = "获取已配置终端的实时信息列表")
    public Result<List<AnalogData>> findRealData() {
        List<AnalogData> analogsList = service.findRealData();
        return new Result(true, StatusCode.OK, "获取全部数据成功", analogsList);
    }

    @PostMapping("/findorder")
    @ApiOperation(value = "获取已配置终端的实时信息列表,根据条件排序")
    public Result<List<RealResponseData>> findOrderRealData(RequestOrderData orderData) {
        List<RealResponseData> orderRealData = service.findOrderRealData(orderData);
        return new Result(true, StatusCode.OK, "获取全部数据成功", orderRealData);
    }

    @GetMapping("/export/{filedir}")
    @ApiOperation(value = "生成终端测试结果的Excel文件")
    @ApiImplicitParam(paramType = "create", name = "filedir", value = "指定生成文件所在目录", required = true, dataType = "string")
    @ApiIgnore
    public Result exportToExcel(@PathVariable String filedir) {
        boolean ret = service.export(filedir);
        if (ret) {
            return new Result(true, StatusCode.OK, "数据导出成功");
        } else {
            return new Result(false, StatusCode.ERROR, "数据导出失败");
        }
    }

    @GetMapping("/clearall")
    @ApiOperation(value = "清空缓存，Redis,TCP端各种缓存")
    public Result clearAll() {
        service.clearAll();
        return new Result(false, StatusCode.OK, "发送清空数据成功");
    }

}
