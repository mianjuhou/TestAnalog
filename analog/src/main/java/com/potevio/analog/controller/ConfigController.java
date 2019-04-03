package com.potevio.analog.controller;

import com.alibaba.fastjson.JSON;
import com.potevio.analog.entity.Result;
import com.potevio.analog.entity.StatusCode;
import com.potevio.analog.pojo.ConfigData;
import com.potevio.analog.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/config")
@CrossOrigin
public class ConfigController {

    @Autowired
    private ConfigService configService;

    /**
     * 删除所有配置
     */
    @RequestMapping("/deleteall")
    public Result deleteAll() {
        configService.deleteAll();
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 删除指定ID的配置数据并返回当前全部配置数据
     */
    @RequestMapping("/delete/{id}")
    public Result delete(@PathVariable String id) {
        List<ConfigData> configs = configService.deleteConfig(id);
        if (configs == null) {
            return new Result(false, StatusCode.ERROR, "删除失败");
        } else {
            return new Result(true, StatusCode.OK, "删除成功", configs);
        }
    }

    /**
     * 添加一项配置后返回所有配置数据
     */
    @RequestMapping("/add")
    public Result add(@RequestBody ConfigData data) {
//    public Result add(ConfigData data) {
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

    /**
     * 获取当前全部配置数据
     */
    @RequestMapping("/getall")
    public Result getall() {
        List<ConfigData> configs = configService.getAllConfig();
        return new Result(true, StatusCode.OK, "获取配置数据成功", configs);
    }

    /**
     * 开始测试
     */
    @RequestMapping("/starttest")
    public Result starttest() {
        boolean ret = configService.starttest();
        if (ret) {
            return new Result(true, StatusCode.OK, "开始测试成功");
        } else {
            return new Result(false, StatusCode.ERROR, "开始测试失败");
        }
    }

    /**
     * 结束测试
     */
    @RequestMapping("/stoptest")
    public Result stoptest() {
        boolean ret = configService.stoptest();
        if (ret) {
            return new Result(true, StatusCode.OK, "结束测试成功");
        } else {
            return new Result(false, StatusCode.ERROR, "结束测试失败");
        }
    }

}
