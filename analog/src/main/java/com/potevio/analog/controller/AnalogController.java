package com.potevio.analog.controller;

import com.potevio.analog.entity.Result;
import com.potevio.analog.entity.StatusCode;
import com.potevio.analog.pojo.AnalogData;
import com.potevio.analog.pojo.TerminalData;
import com.potevio.analog.service.AnalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/analog")
@CrossOrigin
public class AnalogController {

    @Autowired
    private AnalogService service;

    /**
     * 获取终端列表
     * 只获取已上线的终端的imsi ip
     */
    @RequestMapping("/terminals")
    public Result getTerminalList() {
        List<TerminalData> terminals = service.getTerminalList();
        return new Result(true, StatusCode.OK, "获取终端列表", terminals);
    }

    /**
     * 获取单个终端
     */
    @RequestMapping("/terminal/{ip}")
    public Result getTerminalDate(@PathVariable String ip) {
        TerminalData terminal = service.getTerminalData(ip);
        return new Result(true, StatusCode.OK, "获取终端", terminal);
    }

    /**
     * 获取redis中实时的测试数据
     */
    @RequestMapping("/find")
    public Result findRealData() {
        List<AnalogData> analogsList = service.findRealData();
        return new Result(true, StatusCode.OK, "获取全部数据成功", analogsList);
    }

    /**
     * 把最终测试数据导入到Excel中
     */
    @RequestMapping("/export/{filedir}")
    public Result exportToExcel(@PathVariable String filedir) {
        boolean ret = service.export(filedir);
        if (ret) {
            return new Result(true, StatusCode.OK, "数据导出成功");
        } else {
            return new Result(false, StatusCode.ERROR, "数据导出失败");
        }
    }

}
