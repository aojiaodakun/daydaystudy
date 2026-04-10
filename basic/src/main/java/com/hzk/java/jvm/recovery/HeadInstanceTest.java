package com.hzk.java.jvm.recovery;


import java.util.ArrayList;

/**
 * -Xms600m -Xmx600m -XX:SurvivorRatio=8
 * 使用VisualVM分析
 */
public class HeadInstanceTest {

    byte[] buffer = new byte[1024 * 100];

    public static void main(String[] args) {
        ArrayList<HeadInstanceTest> list = new ArrayList<>();
        while (true){
            list.add(new HeadInstanceTest());
            try{
                Thread.sleep(20);
            }catch (Exception e){

            }
        }


    }


}
