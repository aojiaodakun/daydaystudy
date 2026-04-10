package com.hzk.java.proxy.jdk;

class UserServiceImpl implements IUserService {


    @Override
    public String add(String string) {
        System.out.println("新增用户:" + string);
        return string;
    }

    public void delete() {
        System.out.println("删除用户");
    }
}
