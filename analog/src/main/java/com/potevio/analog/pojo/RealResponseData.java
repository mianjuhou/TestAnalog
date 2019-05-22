package com.potevio.analog.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//招测|上报个数1|上报间隔5|上报周期5000|端口号8899|数据长度71|招测长度20
@Data
@ApiModel(value = "测试时返回的实时数据")
public class RealResponseData {
    @ApiModelProperty(value = "测试类型，0：招测，1：上报", notes = "测试类型，0：招测，1：上报", required = true)
    private String testType;
    @ApiModelProperty(value = "每次上报数据包个数", notes = "每次上报数据包个数", required = true)
    private String numOfPktsPerTime;
    @ApiModelProperty(value = "每次上报间隔", notes = "每次上报间隔", required = true)
    private String intervalOfPerTime;
    @ApiModelProperty(value = "每包数据的间隔时间", notes = "每包数据的间隔时间", required = true)
    private String intervalOfPerPkt;
    @ApiModelProperty(value = "端口号", notes = "端口号", required = true)
    private String portNum;
    @ApiModelProperty(value = "数据长度", notes = "数据长度", required = true)
    private String dataLength;
    @ApiModelProperty(value = "*召测长度*", required = true)
    private String callLength;
    @ApiModelProperty(value = "配置内分批处理时一批的数量", required = false)
    private String batchNum;
    @ApiModelProperty(value = "配置内分批处理时间间隔", required = false)
    private String batchDelay;

    @ApiModelProperty(value = "排序后的实时数据列表",required = true)
    private List<AnalogData> analogList = new ArrayList<>();

    public RealResponseData() {
    }

    public RealResponseData(String testType, String numOfPktsPerTime, String intervalOfPerTime, String intervalOfPerPkt, String portNum, String dataLength, String callLength, String batchNum, String batchDelay) {
        this.testType = testType;
        this.numOfPktsPerTime = numOfPktsPerTime;
        this.intervalOfPerTime = intervalOfPerTime;
        this.intervalOfPerPkt = intervalOfPerPkt;
        this.portNum = portNum;
        this.dataLength = dataLength;
        this.callLength = callLength;
        this.batchNum = batchNum;
        this.batchDelay = batchDelay;
    }
}
