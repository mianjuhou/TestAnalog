package com.potevio.analog.util;

import com.potevio.analog.pojo.AnalogData;
import com.potevio.analog.pojo.RequestOrderData;

import java.util.Comparator;

/**
 * 多字段组合排序，但是组合是无序的
 */
public class RequestComparetor3 implements Comparator<AnalogData> {
    private int order = -1;
    private boolean type = true;
    public boolean needOrder = false;

    public RequestComparetor3(RequestOrderData orderData) {
        if (orderData.getIp().equals("1")) {
            order = 0;
            type = true;
        } else if (orderData.getIp().equals("2")) {
            order = 0;
            type = false;
            needOrder = true;
        }
        if (orderData.getPktReceived().equals("1")) {
            order = 1;
            type = true;
            needOrder = true;
        } else if (orderData.getPktReceived().equals("2")) {
            order = 1;
            type = false;
            needOrder = true;
        }
    }

    public boolean isNeedOrder() {
        return needOrder;
    }

    public void setNeedOrder(boolean needOrder) {
        this.needOrder = needOrder;
    }

    @Override
    public int compare(AnalogData o1, AnalogData o2) {
        boolean zd = true;
        if (order == 0) {
            if (IpUtil.getIp2long(o1.getIp()) < IpUtil.getIp2long(o2.getIp())) {
                zd = false;
            }
        } else if (order == 1) {
            if (Long.parseLong(o1.getPktReceived()) < Long.parseLong(o2.getPktReceived())) {
                zd = false;
            }
        }
        return zd && type ? 1 : -1;
    }
}
