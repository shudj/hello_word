package com.jl.test.scala.timing;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Author: shudj
 * Time: 2018/12/10 11:00
 * Description:
 */
public class ScheduledThreadPoolDemo01 {
    private static int count = 0;
    public static void main(String[] args) throws InterruptedException {
        final TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                System.out.println("task1:" + System.currentTimeMillis());
            }
        };

        final TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                System.out.println("task2:" + System.currentTimeMillis());
                count++;
            }
        };



        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(task1, 1, 1000, TimeUnit.MILLISECONDS);
        pool.scheduleAtFixedRate(task2, 0, 1000, TimeUnit.MILLISECONDS);

    }
}
