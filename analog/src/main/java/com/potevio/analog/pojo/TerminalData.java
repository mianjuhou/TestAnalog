package com.potevio.analog.pojo;

public class TerminalData {
    private String imsi;
    private String ip;              //终端IP
    private String port;            //端口号
    private String onlinestate;     //在线状态 0掉线 1在线

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
