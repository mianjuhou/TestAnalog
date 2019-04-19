package com.potevio.analog.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "发送开始命令时包裹配置信息和总端口的对象模型")
public class ConfigWrapperData {

    @ApiModelProperty(value = "总端口号", required = true)
    private String portNum;
    @ApiModelProperty(value = "总召测间隔", required = true)
    private String interval;
    @ApiModelProperty(value = "配置信息列表", required = true)
    private List<ConfigData> ruleDataArrayList;

    public ConfigWrapperData() {
    }

    public ConfigWrapperData(String portNum, List<ConfigData> ruleDataArrayList) {
        this.portNum = portNum;
        this.ruleDataArrayList = ruleDataArrayList;
    }

    public ConfigWrapperData(String portNum, String interval, List<ConfigData> ruleDataArrayList) {
        this.portNum = portNum;
        this.interval = interval;
        this.ruleDataArrayList = ruleDataArrayList;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getPortNum() {
        return portNum;
    }

    public void setPortNum(String portNum) {
        this.portNum = portNum;
    }

    public List<ConfigData> getRuleDataArrayList() {
        return ruleDataArrayList;
    }

    public void setRuleDataArrayList(List<ConfigData> ruleDataArrayList) {
        this.ruleDataArrayList = ruleDataArrayList;
    }
}
