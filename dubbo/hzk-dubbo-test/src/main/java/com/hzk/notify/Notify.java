package com.hzk.notify;

public interface Notify {
    // 调用前
    void oninvoke(String requestParam);
    // 调用后
    void onreturn(String remoteResult, String requestParam);
    // 报异常
    void onthrow(Throwable ex, String requestParam);

}
