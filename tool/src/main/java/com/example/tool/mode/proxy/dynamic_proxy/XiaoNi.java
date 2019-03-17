package com.example.tool.mode.proxy.dynamic_proxy;

import com.example.tool.mode.proxy.static_proxy.Person;

public class XiaoNi implements Person {

    @Override
    public void getLove() {
        System.out.println("my name is xiao ni");
    }
}
