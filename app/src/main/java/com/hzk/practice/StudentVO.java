package com.hzk.practice;

public class StudentVO {

    private static StudentVO studentVO = new StudentVO();

    public StudentVO(){

        System.out.println("StudentVO");
    }

    public static void getInstance() {

        System.out.println("hellogetInstance");

//        return studentVO;
    }

    public void print(){

        System.out.println("print");
    }

}
