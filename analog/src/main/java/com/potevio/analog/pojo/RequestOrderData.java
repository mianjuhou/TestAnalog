package com.potevio.analog.pojo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 如果都不配置则使用默认方式即IP升序
 */
public class RequestOrderData {

    @ApiModelProperty(value = "排序字段，1 升序 2 降序 其他为不启用", required = false)
    private String ip;
    @ApiModelProperty(value = "在线状态，1 升序 2 降序 其他为不启用", required = false)
    private String onlinestate;
    @ApiModelProperty(value = "测试状态，1 升序 2 降序 其他为不启用", required = false)
    private String testStatus;
    @ApiModelProperty(value = "测试次数, 1 升序 2 降序 其他为不启用", required = false)
    private String testTime;
    @ApiModelProperty(value = "应收包数, 1 升序 2 降序 其他为不启用", required = false)
    private String pktReceivable;
    @ApiModelProperty(value = "实收包数，1 升序 2 降序 其他为不启用", required = false)
    private String pktReceived;

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPktReceived() {
        return pktReceived;
    }

    public void setPktReceived(String pktReceived) {
        this.pktReceived = pktReceived;
    }
}
