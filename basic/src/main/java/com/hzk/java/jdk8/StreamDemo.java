package com.hzk.java.jdk8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StreamDemo {

    public static void main(String[] args) throws Exception{

        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");
        list.add("java");
        List<String> resultList = list.stream().map(e -> e.toUpperCase()).collect(Collectors.toList());
        List<String> resultList2 = list.stream().flatMap(item -> Arrays.asList(("1" + item).toUpperCase()).stream())
                .collect(Collectors.toList());
        System.out.println(resultList2);
        System.out.println("----------------------");

        List<Integer> intList = Arrays.asList(1, 2, 3, 4);
        Optional<Integer> optional = intList.stream().reduce((x, y) -> x + y);
        System.out.println(optional.get());

    }


}
