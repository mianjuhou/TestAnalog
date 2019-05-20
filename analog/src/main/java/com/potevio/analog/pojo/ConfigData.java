package com.potevio.analog.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "配置信息对象模型")
public class ConfigData {
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
    @ApiModelProperty(value = "超时时间", notes = "超时时间", required = true)
    private String expireTime;
    @ApiModelProperty(value = "配置的服务端IP,应对多网卡情况", notes = "配置的服务端IP,应对多网卡情况", required = true)
    private String ip;
    @ApiModelProperty(value = "终端ip列表，ip段选的方式也会生成次列表", notes = "终端ip列表，ip段选的方式也会生成次列表", required = false)
    private List<String> ueList = new ArrayList<>();
    @ApiModelProperty(value = "*召测长度*", required = true)
    private String callLength;


    @ApiModelProperty(value = "配置数据列表的唯一标识id，用于删除", required = false)
    private String id;
    @ApiModelProperty(value = "段选开始ip", required = false)
    private String startip;
    @ApiModelProperty(value = "段选结束ip", required = false)
    private String endip;
    @ApiModelProperty(value = "如果遇到ip覆盖情况是否过滤掉，0：不过滤直接返回添加失败，1：直接过滤掉", required = false)
    private String isfilter;

    public RealResponseData obtainRealResponseData() {
        return new RealResponseData(testType, numOfPktsPerTime, intervalOfPerTime, intervalOfPerPkt, portNum, dataLength, callLength);
    }

    public ExcelData obtainExcelData() {
        return new ExcelData(numOfPktsPerTime, intervalOfPerTime, intervalOfPerPkt, portNum, testType, dataLength, ip, callLength);
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getCallLength() {
        return callLength;
    }

    public void setCallLength(String callLength) {
        this.callLength = callLength;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIsfilter() {
        return isfilter;
    }

    public void setIsfilter(String isfilter) {
        this.isfilter = isfilter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDataLength() {
        return dataLength;
    }

    public void setDataLength(String dataLength) {
        this.dataLength = dataLength;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public List<String> getUeList() {
        return ueList;
    }

    public void setUeList(List<String> ueList) {
        this.ueList = ueList;
    }

    //
    public String getStartip() {
        return startip;
    }

    public void setStartip(String startip) {
        this.startip = startip;
    }

    public String getEndip() {
        return endip;
    }

    public void setEndip(String endip) {
        this.endip = endip;
    }
}
