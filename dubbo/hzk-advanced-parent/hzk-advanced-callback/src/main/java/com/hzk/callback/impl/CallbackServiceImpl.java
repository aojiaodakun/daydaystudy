package com.hzk.callback.impl;

import com.hzk.callback.ICallbackListener;
import com.hzk.callback.ICallbackService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CallbackServiceImpl implements ICallbackService {

    private final Map<String, ICallbackListener> LISTENER_MAP = new ConcurrentHashMap<>();

    public CallbackServiceImpl(){
        Thread t = new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        for(Map.Entry<String, ICallbackListener> entry : LISTENER_MAP.entrySet()){
                            try {
                                // 发送变更通知
                                entry.getValue().changed(getChanged(entry.getKey()));
                            } catch (Throwable t) {
                                LISTENER_MAP.remove(entry.getKey());
                            }
                        }
                        Thread.sleep(5000); // 定时触发变更通知
                    } catch (Throwable t) { // 防御容错
                        t.printStackTrace();
                    }
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void addListener(String key, ICallbackListener listener) {
        LISTENER_MAP.putIfAbsent(key, listener);
    }

    private String getChanged(String key) {
        return "Changed: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

}
