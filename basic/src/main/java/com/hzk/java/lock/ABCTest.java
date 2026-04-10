package com.hzk.java.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ABCTest {

    public static void main(String[] args) {
        ABSData absData = new ABSData();
        new Thread(()->{
            absData.printA();
        },"A").start();

        new Thread(()->{
            absData.printB();
        },"B").start();

        new Thread(()->{
            absData.printC();
        },"C").start();

    }

}
class ABSData{
    private int number = 1;
    private ReentrantLock lock = new ReentrantLock();
    private Condition conditionA = lock.newCondition();
    private Condition conditionB = lock.newCondition();
    private Condition conditionC = lock.newCondition();

    public void printA(){
        try {
            lock.lock();
            if(number != 1){
                conditionA.await();
            }
            System.out.println(Thread.currentThread().getName() + ",A");
            number = 2;
            conditionB.signal();
        }catch (Exception e){

        }finally {
            lock.unlock();
        }
    }

    public void printB(){
        try {
            lock.lock();
            if(number != 2){
                conditionB.await();
            }
            System.out.println(Thread.currentThread().getName() + ",B");
            number = 3;
            conditionC.signal();
        }catch (Exception e){

        }finally {
            lock.unlock();
        }
    }

    public void printC(){
        try {
            lock.lock();
            if(number != 3){
                conditionC.await();
            }
            System.out.println(Thread.currentThread().getName() + ",C");
            number = 1;
            conditionA.signal();
        }catch (Exception e){

        }finally {
            lock.unlock();
        }
    }

}