package com.hzk.test;

public class ExceptionTest {


    public static void main(String[] args) {
        methodA();
    }

    static void methodA(){
        try {
            methodB();
        } catch (RuntimeException e) {
            throw new AException(e.getMessage(), e);
        }
    }

    static void methodB(){
        try {
            int a=1;
            String s1 = "a";
            boolean b1 = false;

            methodC();
        } catch (RuntimeException e) {
            throw new BException(e.getMessage(), e);
        }
    }

    static void methodC(){
        try {
            methodD();
        } catch (RuntimeException e) {
            throw new CException(e.getMessage(), e);
        }
    }

    static void methodD(){
        try {
            int i = 10 / 0;
        } catch (Exception e) {
            throw new DException(e.getMessage(), e);
        }
    }


}
class AException extends RuntimeException{

    public AException(String message, Throwable cause) {
        super(message, cause);
    }

}

class BException extends RuntimeException{
    public BException(String message, Throwable cause) {
        super(message, cause);
    }
}

class CException extends RuntimeException{
    public CException(String message, Throwable cause) {
        super(message, cause);
    }
}

class DException extends RuntimeException{
    public DException(String message, Throwable cause) {
        super(message, cause);
    }
}