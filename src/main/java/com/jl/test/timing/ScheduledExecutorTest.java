package com.jl.test.timing;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Author: shudj
 * Time: 2018/12/10 10:41
 * Description:
 */
public class ScheduledExecutorTest {

    private ScheduledExecutorService service;
    public long start;

    ScheduledExecutorTest() {
        this.service = Executors.newScheduledThreadPool(2);
        this.start = System.currentTimeMillis();
    }

    public void timerOne() {
        service.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("timerOne, the time:" + (System.currentTimeMillis() -start));
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 1, TimeUnit.SECONDS);
    }

    public void timerTwo() {
        service.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("timerOne, the time:" + (System.currentTimeMillis() -start));
            }
        }, 1, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        ScheduledExecutorTest test = new ScheduledExecutorTest();
        test.timerOne();
        test.timerTwo();
    }
}
