package com.hzk.safe.payload.serializable.hessian.dto;

import java.io.Serializable;

public class BDTO implements Serializable {

    private ADTO aDTO;

    public BDTO(ADTO aDTO) {
        this.aDTO = aDTO;
    }

    public ADTO getaDTO() {
        return aDTO;
    }

    public void setaDTO(ADTO aDTO) {
        this.aDTO = aDTO;
    }
}
