package com.hzk.test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class BasicTest {

    // 采样打印计数器：每1000次打印一次堆栈
    private static AtomicLong notifyCallCount = new AtomicLong(0);
    // 上次打印堆栈时间戳
    private static AtomicLong lastPrintStackTime = new AtomicLong(0);

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 100; i++) {
            Thread.currentThread().sleep(500);
            for (int j = 0; j < 2000; j++) {
                test1();
            }
        }
    }

    public static void test1(){
        printStackSampling();
//        System.out.println("BasicTest#test1");
    }




    private static void printStackSampling() {
        long count = notifyCallCount.incrementAndGet();
        Integer callSize = Integer.getInteger("ecache.printStack.call.size", 1000);
        if (count >= callSize) {// 1000次调用打印一次堆栈
            long now = System.currentTimeMillis();
            long lastPrint = lastPrintStackTime.get();
            // n毫秒内最多打印一次
            Integer printStackBetween = Integer.getInteger("ecache.printStack.between", 1000);
            if (now - lastPrint >= printStackBetween) {
                if (lastPrintStackTime.compareAndSet(lastPrint, now)) {
                    notifyCallCount.set(0);
                    Exception stackException = new Exception();
                    stackException.printStackTrace();
                    System.err.println(LocalDateTime.now());
//                    logger.info("notify call count reach 1000, print stack: count={}, trace:", count, stackException);
                }
            }
        }
    }

}
