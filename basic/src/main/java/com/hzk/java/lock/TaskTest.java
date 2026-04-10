package com.hzk.java.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TaskTest {

    private static volatile Map<String,TaskProcessData> TASKPROCESS_MAP = new HashMap<>();

    private static volatile ReentrantReadWriteLock LOCKER = new ReentrantReadWriteLock();

    static int TOTAL = 1000;


    public static void main(String[] args) {

        putProcess(TASKPROCESS_MAP,"任务1",TOTAL,0);


        new Thread(()->{
            for(int i = 0;i<10;i++){
                try {
                    Thread.currentThread().sleep(1000);
                    putProcess(TASKPROCESS_MAP,"任务1",TOTAL,100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        },"A").start();

        new Thread(()->{
            for(;;){
                try {
                    Thread.currentThread().sleep(1000);
                    TaskProcessData data = getProcess(TASKPROCESS_MAP,"任务1");
                    System.out.println(Thread.currentThread().getName() + "：" + data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"B").start();

//        new Thread(()->{
//            for(;;){
//                try {
//                    Thread.currentThread().sleep(1000);
//                    TaskProcessData data = getProcess(TASKPROCESS_MAP,"任务1");
//                    System.out.println(Thread.currentThread().getName() + "：" + data);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        },"C").start();

    }

    // put
    private static void putProcess(Map<String,TaskProcessData> map,String key,int total,int hadSend){
        try {
            LOCKER.writeLock().lock();
            TaskProcessData data = map.get(key);
            if(data == null){
                data = new TaskProcessData();
                data.total = total;
                data.toSend = total;
                map.put(key,data);
            }else{
                int had = data.hadSend + hadSend;
                int to = data.total - had;
                data.hadSend = had;
                data.toSend = to;
                if(data.toSend == 0){
                    removeProcess(map,key);
                }else{
                    map.put(key,data);
                }
            }
        }finally {
            LOCKER.writeLock().unlock();
        }
    }

    // get
    private static TaskProcessData getProcess(Map<String,TaskProcessData> map,String key){
        try {
            LOCKER.readLock().lock();
            return map.get(key);
        }finally {
            LOCKER.readLock().unlock();
        }
    }

    // remove
    private static void removeProcess(Map<String,TaskProcessData> map,String key){
        try {
            LOCKER.writeLock().lock();
            map.remove(key);
        }finally {
            LOCKER.writeLock().unlock();
        }
    }


}

class TaskProcessData{
    volatile int total;
    volatile int hadSend;
    volatile int toSend;

    @Override
    public String toString() {
        return "TaskProcessData{" +
                "total=" + total +
                ", hadSend=" + hadSend +
                ", toSend=" + toSend +
                '}';
    }
}
