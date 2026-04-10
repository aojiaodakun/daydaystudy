package com.hzk.java.jdk8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * 1、消费型 Consumer
 * 2、供给型 Supplier
 * 3、断言型 Predicate
 * 4、函数型 Function
 */
public class FunctionInterfaceDemo {

    public static void main(String[] args) {
        consumerTest();
        supplierTest();
        redicateTest();
        functionTest();

        biConsumerTest();

        System.out.println("-----");
        method1();

    }

    public static void consumerTest(){
        Consumer<String> consumer = (x)->{
            System.out.println(x);
        };
        String temp = "abc";
        consumer.accept(temp);
    }
    public static void supplierTest(){
        Supplier supplier = ()->new Object();
        Object object = supplier.get();
        System.out.println(object);
    }
    public static void redicateTest(){
        Predicate<String> predicate = (x)->x.contains("c");
        String temp = "abcd";
        System.out.println(predicate.test(temp));
    }

    public static void functionTest(){
        Function<String,Integer> function = (s) -> s.length();
        String temp = "abcd";
        System.out.println(function.apply(temp));
    }

    public static void biConsumerTest(){
        BiConsumer<String,Integer> biConsumer = (s,i)->{
            System.out.println(s);
            System.out.println(i);
        };
        biConsumer.accept("s1",2);
    }



    public static void method1(){
        // 传参
        String[] arrays = new String[]{"1","2"};
        String collect1 = Arrays.asList(arrays).stream().collect(Collectors.joining(","));
        System.out.println(collect1);

        // 我这边接收
        List<String> list = Arrays.asList(collect1);
        System.out.println(list);
    }

    @Test
    public void test1(){
        List<Person> list = new ArrayList<>();
        Person person1 = new Person("18", 18);
        Person person2 = new Person("19", 19);
        Person person3 = new Person("20", 20);
        Person person4 = new Person("21", 21);
        list.add(person1);
        list.add(person2);
        list.add(person3);
        list.add(person4);


    }


}
class Person{
    private String name;
    private int age;
    public boolean m1(){
        return false;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}


