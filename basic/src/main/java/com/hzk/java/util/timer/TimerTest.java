package com.hzk.java.util.timer;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {

    @Test
    public void timerTest1() throws Exception {
        Timer timer = new Timer();
        timer.schedule(task, 0, 2000);
        Thread.sleep(1000 * 10);
        timer.cancel();
        System.out.println("定时任務已取消");
        timer.schedule(task, 0, 2000);// 抛异常

    }

    static TimerTask task = new TimerTask() {
        @Override
        public void run() {
            System.out.println(LocalDateTime.now() + "执行一次");
        }
    };

}

