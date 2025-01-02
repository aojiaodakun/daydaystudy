package com.hzk.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class ExampleParamDTO implements Serializable {

    @NotNull // 不允许为空
    @Size(min = 1, max = 20) // 长度或大小范围
    private String name;

    @Min(18) // 最小值
    @Max(100) // 最大值
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
