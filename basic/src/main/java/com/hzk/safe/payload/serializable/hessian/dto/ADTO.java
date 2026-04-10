package com.hzk.safe.payload.serializable.hessian.dto;

import java.io.Serializable;

public class ADTO implements Serializable {

    private BDTO bdto = new BDTO(this);

    public BDTO getBdto() {
        return bdto;
    }

    public void setBdto(BDTO bdto) {
        this.bdto = bdto;
    }
}
