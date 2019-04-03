package com.potevio.analog.pojo;

public class AnalogData {
    private String imsi;            //imsi
    private String ip;              //终端IP
    private String port;            //端口号
    private String onlinestate;     //在线状态
    private String testStatus;      //测试状态
    private String testTime;        //测试次数
    private String pktReceivable;   //应收包数
    private String pktReceived;     //实收包数
    private String startTime;       //开始时间
    private String endTime;         //结束时间
    private String avgDelay;        //平均时延
    private String maxDelay;        //最大时延
    private String lastDelay;       //最近一次的时延

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
