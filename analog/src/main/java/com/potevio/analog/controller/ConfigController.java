package com.potevio.analog.controller;

import com.potevio.analog.entity.Result;
import com.potevio.analog.entity.StatusCode;
import com.potevio.analog.pojo.ConfigData;
import com.potevio.analog.service.ConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/config")
@CrossOrigin
@Api(value = "配置信息Controller",tags = "ConfigController") //终端配置信息控制类
public class ConfigController {

    @Autowired
    private ConfigService configService;


    @GetMapping("/deleteall")
    @ApiOperation(value = "删除所有终端配置信息")
    public Result deleteAll() {
        configService.deleteAll();
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @GetMapping("/delete/{id}")
    @ApiOperation(value = "删除指定ID的配置数据并返回当前全部配置数据")
    @ApiImplicitParam(paramType = "delete", name = "id", value = "添加配置信息时返回的id", required = true, dataType = "String")
    public Result<List<ConfigData>> delete(@PathVariable String id) {
        List<ConfigData> configs = configService.deleteConfig(id);
        if (configs == null) {
            return new Result(false, StatusCode.ERROR, "删除失败");
        } else {
            return new Result(true, StatusCode.OK, "删除成功", configs);
        }
    }


    @PostMapping("/add")
    @ApiOperation(value = "添加一项配置后返回所有配置数据")
    @ApiImplicitParam(paramType = "add", name = "data", value = "配置信息JSON", required = true, dataType = "ConfigData")
    public Result<List<ConfigData>> add(@RequestBody ConfigData data) {
        try {
            List<ConfigData> configs = configService.addConfig(data);
            if (configs == null) {
                return new Result(false, StatusCode.ERROR, "添加失败");
            } else {
                return new Result(true, StatusCode.OK, "添加成功", configs);
            }
        } catch (Exception e) {
            return new Result(false, StatusCode.ERROR, e.getMessage());
        }
    }

    @GetMapping("/getall")
    @ApiOperation(value = "获取当前全部配置数据")
    public Result<List<ConfigData>> getall() {
        List<ConfigData> configs = configService.getAllConfig();
        return new Result(true, StatusCode.OK, "获取配置数据成功", configs);
    }


    @GetMapping("/starttest/{port}/{interval}")
    @ApiOperation(value = "发出开始测试命令")
    @ApiImplicitParam(paramType = "send", name = "port", value = "端口号", required = true, dataType = "String")
    public Result starttest(@PathVariable String port,@PathVariable String interval) {
        boolean ret = configService.starttest(port,interval);
        if (ret) {
            return new Result(true, StatusCode.OK, "开始测试成功");
        } else {
            return new Result(false, StatusCode.ERROR, "开始测试失败");
        }
    }

    @GetMapping("/stoptest")
    @ApiOperation(value = "发出结束测试命令")
    public Result stoptest() {
        boolean ret = configService.stoptest();
        if (ret) {
            return new Result(true, StatusCode.OK, "结束测试成功");
        } else {
            return new Result(false, StatusCode.ERROR, "结束测试失败");
        }
    }

    @GetMapping("/clean")
    @ApiOperation(value = "清空配置终端的数据")
    public Result clean(){
        configService.clean();
        return new Result(true, StatusCode.OK, "清空数据成功");
    }

}
