package com.hzk.safe.payload.serializable.jackson;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class Wrapper {

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.CLASS,
            include = JsonTypeInfo.As.PROPERTY,
            property = "@class"
    )
//    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    public Object data;

}
