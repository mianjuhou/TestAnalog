package com.potevio.analog.pojo;

import java.util.List;

public class ConfigWrapperData {

    private String portNum;

    private List<ConfigData> ruleDataArrayList;

    public ConfigWrapperData(String portNum, List<ConfigData> ruleDataArrayList) {
        this.portNum = portNum;
        this.ruleDataArrayList = ruleDataArrayList;
    }

    public String getPortNum() {
        return portNum;
    }

    public void setPortNum(String portNum) {
        this.portNum = portNum;
    }

    public List<ConfigData> getRuleDataArrayList() {
        return ruleDataArrayList;
    }

    public void setRuleDataArrayList(List<ConfigData> ruleDataArrayList) {
        this.ruleDataArrayList = ruleDataArrayList;
    }
}
