package com.hzk.notify;

public class NotifyImpl implements Notify{


    @Override
    public void oninvoke(String requestParam) {
        System.out.println("oninvoke!!!!!! requestParam:" + requestParam);
    }

    @Override
    public void onreturn(String remoteResult, String requestParam) {
        System.out.println("onreturn!!!!! remoteResult:" + remoteResult + ",requestParam:" + requestParam);
    }

    @Override
    public void onthrow(Throwable ex, String requestParam) {
        if (ex != null) {
            ex.printStackTrace();
        }
        System.out.println("onthrow!!!!! requestParam:" + requestParam);
    }
}
