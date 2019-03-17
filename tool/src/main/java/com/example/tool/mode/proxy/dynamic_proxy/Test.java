package com.example.tool.mode.proxy.dynamic_proxy;

import java.lang.reflect.Proxy;

public class Test {

    public static void main(String[] args) {
        MatchMaker matchMaker = new MatchMaker(new XiaoNi());
        Person p = (Person) Proxy.newProxyInstance(XiaoNi.class.getClassLoader(), XiaoNi.class.getInterfaces(), matchMaker);
        p.getLove();
    }
}
