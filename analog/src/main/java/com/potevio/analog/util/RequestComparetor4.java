package com.potevio.analog.util;

import com.potevio.analog.pojo.AnalogData;
import com.potevio.analog.pojo.RequestOrderData;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 多字段组合排序，并且组合是有顺序的
 * 字段的绝对值代表先后顺序，正负分别代表升序和降序，0、null、空字符串代表不排序
 */
public class RequestComparetor4 implements Comparator<AnalogData> {
    public boolean needOrder = false;
    private List<Integer> orderResult = new ArrayList<>();

    public RequestComparetor4(RequestOrderData orderData) {
        Integer[] orderList = new Integer[6];
        if (orderData.getIp() != null && !orderData.getIp().isEmpty() && orderData.getIp().equals("0")) {
            orderList[0] = Integer.parseInt(orderData.getIp());
        }
        if (orderData.getOnlinestate() != null && !orderData.getOnlinestate().isEmpty() && orderData.getOnlinestate().equals("0")) {
            orderList[1] = Integer.parseInt(orderData.getOnlinestate());
        }
        if (orderData.getTestStatus() != null && !orderData.getTestStatus().isEmpty() && orderData.getTestStatus().equals("0")) {
            orderList[2] = Integer.parseInt(orderData.getTestStatus());
        }
        if (orderData.getTestTime() != null && !orderData.getTestTime().isEmpty() && orderData.getTestTime().equals("0")) {
            orderList[3] = Integer.parseInt(orderData.getTestTime());
        }
        if (orderData.getPktReceivable() != null && !orderData.getPktReceivable().isEmpty() && orderData.getPktReceivable().equals("0")) {
            orderList[4] = Integer.parseInt(orderData.getPktReceivable());
        }
        if (orderData.getPktReceived() != null && !orderData.getPktReceived().isEmpty() && orderData.getPktReceived().equals("0")) {
            orderList[5] = Integer.parseInt(orderData.getPktReceived());
        }
        //如果没有比较项或者只有第一项且为升序则不进行比较
        for (int i = 0; i < orderList.length; i++) {
            if (orderList[i] != null) {
                if (i == 0 && orderList[i] > 0) {
                    needOrder = false;
                } else {
                    needOrder = true;
                }
            }
        }
        if (!needOrder) {
            return;
        }
        //整合顺序
        for (int i = 0; i < orderList.length; i++) {
            Integer max = null;
            Integer index = null;
            for (int j = 0; j < orderList.length; j++) {
                Integer item = orderList[j];
                if (item != null) {
                    if (max == null || max < item) {
                        max = item;
                        index = j;
                    }
                }
            }
            if (max != null) {
                orderResult.add(max > 0 ? index : -index);
                orderList[index] = null;
            }
        }
    }

    public boolean isNeedOrder() {
        return needOrder;
    }

    @Override
    public int compare(AnalogData o1, AnalogData o2) {
        //顺序获取各项比较结果
        //只有前面的项结果为相等时才继续下面的比较
        for (Integer index : orderResult) {
            int compare = getCompare(index, o1, o2);
            if (compare != 0) {
                return compare;
            }
        }
        return 0;
    }

    private int getCompare(Integer index, AnalogData o1, AnalogData o2) {
        int v1 = 0;
        int v2 = 0;
        switch (Math.abs(index)) {
            case 0://IP降序
                return IpUtil.getIp2long(o1.getIp()) > IpUtil.getIp2long(o2.getIp()) ? -1 : 1;
            case 1:
                v1 = Integer.parseInt(o1.getOnlinestate());
                v2 = Integer.parseInt(o2.getOnlinestate());
                break;
            case 2:
                v1 = Integer.parseInt(o1.getTestStatus());
                v2 = Integer.parseInt(o2.getTestStatus());
                break;
            case 3:
                v1 = Integer.parseInt(o1.getTestTime());
                v2 = Integer.parseInt(o2.getTestTime());
                break;
            case 4:
                v1 = Integer.parseInt(o1.getPktReceivable());
                v2 = Integer.parseInt(o2.getPktReceivable());
                break;
            case 5:
                v1 = Integer.parseInt(o1.getPktReceived());
                v2 = Integer.parseInt(o2.getPktReceived());
                break;
        }
        if (v1 == v2) {
            return 0;
        } else if (v1 > v2) {
            return index > 0 ? 1 : -1;
        } else {
            return index > 0 ? 1 : -1;
        }
    }
}
