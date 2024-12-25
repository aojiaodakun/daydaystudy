package com.hzk.zk.basic.lock;

public class TicketSeller {

    public static void main(String[] args) throws Exception {
        TicketSeller ticketSeller = new TicketSeller();
        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                try {
                    ticketSeller.sellTicketWithLock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            },"线程:" + i).start();
        }
        System.in.read();
    }

    public void sellTicketWithLock() throws Exception{
        MyLock lock = new MyLock();
        while (true) {
            lock.acquireLock();
            sell();
            lock.releaseLock();
        }
    }

    private void sell(){
        System.out.println(Thread.currentThread().getName() + "售票开始");
        int sleepMillis = 1000 * 5;
        try {
            Thread.sleep(sleepMillis);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "售票结束");
    }


}
