package com.potevio.analog.util;

import java.util.Comparator;

public class IpComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        if (IpUtil.ipToLong(o1) > IpUtil.ipToLong(o2)) {
            return 1;
        } else {
            return -1;
        }
    }
}
