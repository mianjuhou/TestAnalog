package com.potevio.analog.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
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
    @ApiModelProperty(value = "所属基站ID",required = true)
    private String eNodeBId;
    
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
