package com.jl.test.mode.kafka.java.ip;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: shudj
 * Time: 2018/12/5 12:15
 * Description:
 */
public class CacheIpLookup implements IIPScanner, Serializable {

    private Set<String> fraudIPList = new HashSet<>();

    public  CacheIpLookup() {
        fraudIPList.add("212");
        fraudIPList.add("234");
        fraudIPList.add("23");
        fraudIPList.add("22");
        fraudIPList.add("12");
        fraudIPList.add("62");
        fraudIPList.add("122");
        fraudIPList.add("92");
        fraudIPList.add("24");
        fraudIPList.add("202");
        fraudIPList.add("25");
        fraudIPList.add("224");
        fraudIPList.add("225");
    }

    @Override
    public boolean isFraudIP(String ipAddresses) {
        return fraudIPList.contains(ipAddresses);
    }
}