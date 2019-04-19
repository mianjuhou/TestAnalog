package com.potevio.analog.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "终端数据对象模型")
public class AnalogData {
    @ApiModelProperty(value = "终端唯一码", required = true)
    private String imsi;
    @ApiModelProperty(value = "终端主键IP", required = true)
    private String ip;
    @ApiModelProperty(value = "端口号", required = true)
    private String port;
    @ApiModelProperty(value = "在线状态，0:离线，1：在线", required = true)
    private String onlinestate;
    @ApiModelProperty(value = "测试状态，0：开始，1：结束", required = true)
    private String testStatus;
    @ApiModelProperty(value = "测试次数", required = true)
    private String testTime;
    @ApiModelProperty(value = "应收包数", required = true)
    private String pktReceivable;
    @ApiModelProperty(value = "实收包数", required = true)
    private String pktReceived;
    @ApiModelProperty(value = "开始时间", required = false)
    private String startTime;
    @ApiModelProperty(value = "结束时间", required = false)
    private String endTime;
    @ApiModelProperty(value = "平均时延", required = false)
    private String avgDelay;
    @ApiModelProperty(value = "最大时延", required = false)
    private String maxDelay;
    @ApiModelProperty(value = "最近一次的时延", required = false)
    private String lastDelay;

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getOnlinestate() {
        return onlinestate;
    }

    public void setOnlinestate(String onlinestate) {
        this.onlinestate = onlinestate;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public String getPktReceivable() {
        return pktReceivable;
    }

    public void setPktReceivable(String pktReceivable) {
        this.pktReceivable = pktReceivable;
    }

    public String getPktReceived() {
        return pktReceived;
    }

    public void setPktReceived(String pktReceived) {
        this.pktReceived = pktReceived;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAvgDelay() {
        return avgDelay;
    }

    public void setAvgDelay(String avgDelay) {
        this.avgDelay = avgDelay;
    }

    public String getMaxDelay() {
        return maxDelay;
    }

    public void setMaxDelay(String maxDelay) {
        this.maxDelay = maxDelay;
    }

    public String getLastDelay() {
        return lastDelay;
    }

    public void setLastDelay(String lastDelay) {
        this.lastDelay = lastDelay;
    }
}
