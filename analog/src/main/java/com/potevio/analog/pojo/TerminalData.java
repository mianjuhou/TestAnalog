package com.potevio.analog.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "初始页面所有终端列表显示对象模型")
public class TerminalData {
    @ApiModelProperty(value = "终端唯一码", required = true)
    private String imsi;
    @ApiModelProperty(value = "终端主键IP", required = true)
    private String ip;
    @ApiModelProperty(value = "端口号", required = true)
    private String port;
    @ApiModelProperty(value = "在线状态，0:离线，1：在线", required = true)
    private String onlinestate;

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

    @Override
    public String toString() {
        return "TerminalData{" +
                "imsi='" + imsi + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", onlinestate='" + onlinestate + '\'' +
                '}';
    }
}
