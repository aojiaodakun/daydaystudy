package com.hzk.safe.payload.serializable.hessian.test;

import com.hzk.safe.payload.serializable.hessian.HessianSerializationUtil;
import com.hzk.safe.payload.serializable.common.dto.MserviceRequestDTO;

import java.util.HashMap;
import java.util.Map;

public class HessianUtilTest {

    public static void main(String[] args) {
        MserviceRequestDTO requestDTO = new MserviceRequestDTO();
        requestDTO.setAge(18);
        requestDTO.setMoney(999l);
        requestDTO.setName("hzk");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("a", "1");
        paramMap.put("b", "2");
        // TODO 黑名单
        com.sun.rowset.JdbcRowSetImpl jdbcRowSet = new com.sun.rowset.JdbcRowSetImpl();
        paramMap.put("c", jdbcRowSet);
        requestDTO.setParamMap(paramMap);
        // serialize
        byte[] bytes = HessianSerializationUtil.serialize(requestDTO);
        MserviceRequestDTO newMserviceRequestDTO = (MserviceRequestDTO) HessianSerializationUtil.deserialize(bytes);
        System.out.println(newMserviceRequestDTO);
        // gzipDeserialize
        byte[] bytes1 = HessianSerializationUtil.gzipSerialize(requestDTO);
        MserviceRequestDTO newMserviceRequestDTO1 = (MserviceRequestDTO) HessianSerializationUtil.gzipDeserialize(bytes1);
        System.out.println(newMserviceRequestDTO1);
        // serializeMessage
        byte[] bytes2 = HessianSerializationUtil.serializeMessage(requestDTO);
        MserviceRequestDTO newMserviceRequestDTO2 = (MserviceRequestDTO) HessianSerializationUtil.deserializeMessage(bytes2);
        System.out.println(newMserviceRequestDTO2);
    }

}
