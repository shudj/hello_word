package com.example.tool.mode.proxy.static_proxy;

/**
 * 代理对象，在创建代理接口时，让其传一个代理目标，
 * 实现被代理者同样的接口，因为我们使用过代理者来调用被代理者的方法，
 * 所以就得需要去实现与被代理者相同的接口
 */
public class MatchMaker implements Person {

    private Person target;

    public MatchMaker(Person target) {
        this.target = target;
    }

    @Override
    public void getLove() {
        System.out.println("I will help you find boyFriend");
        this.target.getLove();
    }
}
