package com.hzk.java.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ListTest {


    @Test
    public void shuffle() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        list.add("f");
        list.add("g");
        Collections.shuffle(list);
        System.out.println(list);
    }

    @Test
    public void toStringTest() throws Exception {
        List<ListVO> listVOS = new ArrayList<>();
        ListVO listVO1 = new ListVO("a");
        ListVO listVO2 = new ListVO("b");
        listVOS.add(listVO1);
        listVOS.add(listVO2);
        listVOS.remove(listVO1);
        listVOS.remove(listVO2);
    }

    @Test
    public void nullTest() throws Exception {
        List<ListVO> listVOS = new ArrayList<>();
        ListVO listVO1 = new ListVO("a");
        ListVO listVO2 = new ListVO("b");
        listVOS.add(listVO1);
        listVOS.add(null);
        listVOS.add(listVO2);

        boolean isRemoveFlag = listVOS.removeIf(Objects::isNull);
        for(ListVO tempVO : listVOS) {
            System.out.println(tempVO.toString());
        }
    }


    private static class ListVO{

        private String name;

        public ListVO(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "ListVO{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
