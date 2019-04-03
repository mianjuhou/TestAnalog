package com.potevio.analog.pojo;

import java.util.ArrayList;
import java.util.List;

public class ConfigData {
    private String numOfPktsPerTime;                //每次上报数据包个数
    private String intervalOfPerTime;               //每次上报间隔
    private String intervalOfPerPkt;                //每包数据的间隔时间
    private String portNum;                         //端口号
    private String testType;                        //测试类型
    private String dataLength;                      //数据长度
    private String expireTime;                      //超时时间
    private String ip;                              //配置的服务端IP,应对多网卡情况
    private List<String> ueList = new ArrayList<>();//终端ip列表

    private String id;          //配置数据列表的唯一标识IP
    private String startip;     //开始ip
    private String endip;       //结束ip
    private String isfilter;    //如果遇到ip覆盖情况是否过滤掉

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
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
