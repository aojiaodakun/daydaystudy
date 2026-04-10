package com.hzk.java.thread.service.impl;

import com.hzk.java.thread.service.Calculator;

public class ForLoopCalculator implements Calculator {

    @Override
    public long sumUp(long[] numbers) {
        long total = 0;
        for (long i : numbers) {
            total += i;
        }
        return total;
    }

}
