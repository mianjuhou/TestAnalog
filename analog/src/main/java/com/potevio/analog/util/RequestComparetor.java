package com.potevio.analog.util;

import com.potevio.analog.pojo.AnalogData;
import com.potevio.analog.pojo.RequestOrderData;

import java.util.Comparator;

/**
 * 只有一个字段可以排序的，如果此字段相同其他字段乱序
 */
public class RequestComparetor implements Comparator<AnalogData> {
    private int order = -1;
    private boolean type = true;
    public boolean needOrder = true;

    public RequestComparetor(RequestOrderData orderData) {
        if (orderData.getIp() != null && !orderData.getIp().isEmpty() && !orderData.getIp().equals("0")) {
            order = 0;
            type = orderData.getIp().equals("1") ? true : false;
            needOrder = !type;
        } else if (orderData.getOnlinestate() != null && !orderData.getOnlinestate().isEmpty() && !orderData.getOnlinestate().equals("0")) {
            order = 1;
            type = orderData.getOnlinestate().equals("1") ? true : false;
        } else if (orderData.getTestStatus() != null && !orderData.getTestStatus().isEmpty() && !orderData.getTestStatus().equals("0")) {
            order = 2;
            type = orderData.getTestStatus().equals("1") ? true : false;
        } else if (orderData.getTestTime() != null && !orderData.getTestTime().isEmpty() && !orderData.getTestTime().equals("0")) {
            order = 3;
            type = orderData.getTestTime().equals("1") ? true : false;
        } else if (orderData.getPktReceivable() != null && !orderData.getPktReceivable().isEmpty() && !orderData.getPktReceivable().equals("0")) {
            order = 4;
            type = orderData.getPktReceivable().equals("1") ? true : false;
        } else if (orderData.getPktReceived() != null && !orderData.getPktReceived().isEmpty() && !orderData.getPktReceived().equals("0")) {
            order = 5;
            type = orderData.getPktReceived().equals("1") ? true : false;
        }
    }

    public boolean isNeedOrder() {
        return needOrder;
    }

    @Override
    public int compare(AnalogData o1, AnalogData o2) {
        int compare = getCompare(o1, o2);
        return type ? compare : -compare;
    }

    public int getCompare(AnalogData o1, AnalogData o2) {
        switch (order) {
            case 0:
                return IpUtil.getIp2long(o1.getIp()) > IpUtil.getIp2long(o2.getIp()) ? 1 : -1;
            case 1: {
                long v1 = Long.parseLong(o1.getOnlinestate());
                long v2 = Long.parseLong(o2.getOnlinestate());
                if (v1 == v2) {
                    return 0;
                } else if (v1 > v2) {
                    return 1;
                } else {
                    return -1;
                }
            }
            case 2: {
                long v1 = Long.parseLong(o1.getTestStatus());
                long v2 = Long.parseLong(o2.getTestStatus());
                if (v1 == v2) {
                    return 0;
                } else if (v1 > v2) {
                    return 1;
                } else {
                    return -1;
                }
            }
            case 3: {
                long v1 = Long.parseLong(o1.getTestTime());
                long v2 = Long.parseLong(o2.getTestTime());
                if (v1 == v2) {
                    return 0;
                } else if (v1 > v2) {
                    return 1;
                } else {
                    return -1;
                }
            }
            case 4: {
                long v1 = Long.parseLong(o1.getPktReceivable());
                long v2 = Long.parseLong(o2.getPktReceivable());
                if (v1 == v2) {
                    return 0;
                } else if (v1 > v2) {
                    return 1;
                } else {
                    return -1;
                }
            }
            case 5: {
                long v1 = Long.parseLong(o1.getPktReceived());
                long v2 = Long.parseLong(o2.getPktReceived());
                if (v1 == v2) {
                    return 0;
                } else if (v1 > v2) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
        return 0;
    }
}
