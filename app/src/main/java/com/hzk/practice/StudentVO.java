package com.hzk.practice;

public class StudentVO {

    private String name;
    private static int age=18;

    private static StudentVO studentVO = new StudentVO();

    public StudentVO(){

        System.out.println("StudentVO");
    }

    public StudentVO(String name){
        this.name = name;
    }

    public static void getInstance() {

        System.out.println("hellogetInstance");

//        return studentVO;
    }

    public void print(){
        System.out.println("name=" + name);
    }

    public static void main(String[] args) {
        StudentVO studentVO =new StudentVO();
        studentVO.methodA();
        System.out.println("hello");
    }
    public void methodA() {
        methodB();
    }

    public void methodB() {
        methodC();
    }

    public void methodC() {
        methodD();
    }

    public void methodD() {
        System.out.println("D");
    }

}
