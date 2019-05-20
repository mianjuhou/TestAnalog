package com.potevio.analog.util;

import com.potevio.analog.pojo.AnalogData;
import com.potevio.analog.pojo.RequestOrderData;

import java.util.Comparator;

/**
 * 只有一个字段可以排序的，使用算法2
 */
public class RequestComparetor2 implements Comparator<AnalogData> {
    private int order = -1;
    private boolean type = true;
    public boolean needOrder = false;

    public RequestComparetor2(RequestOrderData orderData) {
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
        long v1 = IpUtil.getIp2long(o1.getIp());
        long v2 = IpUtil.getIp2long(o2.getIp());
        if (order == 0) {
        } else if (order == 1) {
            v1 = Long.parseLong(o1.getPktReceived());
            v2 = Long.parseLong(o2.getPktReceived());
        }
        if (type) {
            if (v1 >= v2) {
                return 1;
            } else {
                return -1;
            }
        } else {
            if (v1 >= v2) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
