package com.potevio.analog.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "保存到Excel文件中的数据Bean")
public class ExcelData {
    @ApiModelProperty(value = "主键ID用于删除", notes = "主键ID用于删除", required = true)
    private String id;
    @ApiModelProperty(value = "每次上报数据包个数", notes = "每次上报数据包个数", required = true)
    private String numOfPktsPerTime;
    @ApiModelProperty(value = "每次上报间隔", notes = "每次上报间隔", required = true)
    private String intervalOfPerTime;
    @ApiModelProperty(value = "每包数据的间隔时间", notes = "每包数据的间隔时间", required = true)
    private String intervalOfPerPkt;
    @ApiModelProperty(value = "端口号", notes = "端口号", required = true)
    private String portNum;
    @ApiModelProperty(value = "测试类型，0：招测，1：上报", notes = "测试类型，0：招测，1：上报", required = true)
    private String testType;
    @ApiModelProperty(value = "数据长度", notes = "数据长度", required = true)
    private String dataLength;
    @ApiModelProperty(value = "配置的服务端IP,应对多网卡情况", notes = "配置的服务端IP,应对多网卡情况", required = true)
    private String ip;
    @ApiModelProperty(value = "*召测长度*", required = true)
    private String callLength;


    public ExcelData() {
    }

    public String getCallLength() {
        return callLength;
    }

    public void setCallLength(String callLength) {
        this.callLength = callLength;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ExcelData(String numOfPktsPerTime, String intervalOfPerTime, String intervalOfPerPkt, String portNum, String testType, String dataLength, String ip, String callLength) {
        this.numOfPktsPerTime = numOfPktsPerTime;
        this.intervalOfPerTime = intervalOfPerTime;
        this.intervalOfPerPkt = intervalOfPerPkt;
        this.portNum = portNum;
        this.testType = testType;
        this.dataLength = dataLength;
        this.ip = ip;
        this.callLength = callLength;
    }

    public String getNumOfPktsPerTime() {
        return numOfPktsPerTime;
    }

    public void setNumOfPktsPerTime(String numOfPktsPerTime) {
        this.numOfPktsPerTime = numOfPktsPerTime;
    }

    public String getIntervalOfPerTime() {
        return intervalOfPerTime;
    }

    public void setIntervalOfPerTime(String intervalOfPerTime) {
        this.intervalOfPerTime = intervalOfPerTime;
    }

    public String getIntervalOfPerPkt() {
        return intervalOfPerPkt;
    }

    public void setIntervalOfPerPkt(String intervalOfPerPkt) {
        this.intervalOfPerPkt = intervalOfPerPkt;
    }

    public String getPortNum() {
        return portNum;
    }

    public void setPortNum(String portNum) {
        this.portNum = portNum;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getDataLength() {
        return dataLength;
    }

    public void setDataLength(String dataLength) {
        this.dataLength = dataLength;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
