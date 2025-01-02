package com.hzk.service.impl;

import com.hzk.dto.ParamValidationDTO;
import com.hzk.service.IDemoService14;

public class DemoServiceImpl14 implements IDemoService14 {

    @Override
    public String paramValid(ParamValidationDTO paramDTO) {
        System.out.println("入参：" + paramDTO);
        return "true";
    }

    @Override
    public String sayHello(String name) {
        return null;
    }
}
