package com.jl.test.mode.producer2consumer;

import sun.plugin2.message.Message;

/**
 * Author: shudj
 * Time: 2018/11/28 14:52
 * Description: 生产者：负责生产消息
 */
public class Producer {
    private String lock;

    public Producer(String lock) {
        this.lock = lock;
    }

    public void setValue() {
        try {
            synchronized (lock) {

                /*
                 * 如果MessageObject.msg值不为空，说明消费者还没有消费完，
                 * 生产者就先不用生产，先等消费完再生产
                 **/
                if (!"".equals(MessageObject.msg)) {
                    lock.wait();
                }
                MessageObject.msg = "msg-" + System.currentTimeMillis();
                lock.notifyAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
