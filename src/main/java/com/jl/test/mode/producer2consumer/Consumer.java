package com.jl.test.mode.producer2consumer;

/**
 * Author: shudj
 * Time: 2018/11/28 15:03
 * Description:
 */
public class Consumer {
    private String lock;

    public Consumer(String lock) {
        this.lock = lock;
    }

    public void getValue() {
        synchronized (lock) {

            if ("".equals(MessageObject.msg)) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(MessageObject.msg);
            MessageObject.msg = "";
            lock.notifyAll();
        }
    }
}
