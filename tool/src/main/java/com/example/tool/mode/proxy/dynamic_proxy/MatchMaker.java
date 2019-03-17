package com.example.tool.mode.proxy.dynamic_proxy;

import com.example.tool.mode.proxy.static_proxy.Person;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 原理：
 * 在底层拿到被代理对象引用，然后获取接口，
 * JDK从新生代生成一个类，同时这个类也是实现这个接口，
 * 把被代理对象的引用也拿到，然后编译这个类获取字节码
 */
public class MatchMaker implements InvocationHandler {

    private Person target;

    public MatchMaker(Person target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("I will help xiaoni find boyFriend");
        return method.invoke(this.target, args);
    }
}
