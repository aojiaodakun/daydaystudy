package com.hzk.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DoubleSummaryStatisticsTest {


    @Test
    public void test1() throws Exception{
        List<DoubleVO> list = new ArrayList<>();
        list.add(new DoubleVO(1, 9.8));
        list.add(new DoubleVO(2, 9.9));
        list.add(new DoubleVO(3, 10.0));
        list.add(new DoubleVO(4, 10.1));
        list.add(new DoubleVO(5, 10.2));
        list.add(new DoubleVO(5, 10.2));

        Map<Long, DoubleSummaryStatistics> map= list.stream()
                .collect(Collectors.groupingBy(DoubleVO::getName,
                        Collectors.summarizingDouble( DoubleVO::getValue )));

        System.out.println(map);


    }


    private static class DoubleVO {
        long name;
        Double value;

        public DoubleVO(long name, Double value) {
            this.name = name;
            this.value = value;
        }

        public long getName() {
            return name;
        }

        public void setName(long name) {
            this.name = name;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }
    }

}
