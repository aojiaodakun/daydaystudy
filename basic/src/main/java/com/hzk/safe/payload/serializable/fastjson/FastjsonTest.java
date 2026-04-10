package com.hzk.safe.payload.serializable.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.hzk.safe.payload.serializable.common.dto.MessageDTO;
import com.hzk.safe.payload.serializable.common.dto.MserviceRequestDTO;
import com.hzk.safe.payload.serializable.common.dto.RequestContextDTO;
import org.junit.Test;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用全局的ParseConfig，开启SafeMode，关闭autotype
 */
public class FastjsonTest {

    @Test
    public void safeFastjsonUtilTest() throws Exception {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMsg("hzk");
        RequestContextDTO requestContextDTO = new RequestContextDTO();
        requestContextDTO.setTenantId("tenantId");
        requestContextDTO.setAccountId("accountId");
        messageDTO.setRequestContext(requestContextDTO);

        String jsonString = SafeFastjsonUtil.toJSONString(messageDTO);
        MessageDTO messageDTO1 = SafeFastjsonUtil.parseObject(jsonString, MessageDTO.class);
        System.out.println(messageDTO1);
    }

    @Test
    public void templatesImplTest() throws Exception {
        // com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl
        MserviceRequestDTO requestDTO = new MserviceRequestDTO();
        requestDTO.setAge(18);
        requestDTO.setMoney(999l);
        requestDTO.setName("hzk");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("a", "1");
        paramMap.put("b", "2");
        // TODO 黑名单
        String xslt =
                "<?xml version=\"1.0\"?>\n" +
                        "<xsl:stylesheet version=\"1.0\"\n" +
                        "    xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" +
                        "  <xsl:template match=\"/\">\n" +
                        "    <hello>world</hello>\n" +
                        "  </xsl:template>\n" +
                        "</xsl:stylesheet>";
        TransformerFactory factory = TransformerFactory.newInstance();
        Templates templates = factory.newTemplates(
                new StreamSource(new java.io.StringReader(xslt))
        );
        paramMap.put("c", templates);
        requestDTO.setParamMap(paramMap);

        String jsonString = FastjsonUtil.toJSONString(requestDTO);
        MserviceRequestDTO mserviceRequestDTO = FastjsonUtil.parseObject(jsonString, MserviceRequestDTO.class);
        System.out.println(mserviceRequestDTO);
    }

    @Test
    public void globalAutotypeTest() throws Exception {
        String json = "{\n" +
                "  \"@type\": \"com.hzk.safe.payload.serializable.fastjson.AutoTypeUser\"\n" +
                "}\n";
        // 全局配置，关闭autotype
        ParserConfig.getGlobalInstance().setAutoTypeSupport(false);
        try {
            // Exception in thread "main" com.alibaba.fastjson.JSONException: autoType is not support. com.hzk.safe.payload.serializable.fastjson.AutoTypeUser
            Object obj = JSON.parseObject(json, Object.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void autotypeTest() throws Exception {
        String json = "{\n" +
                "  \"@type\": \"com.hzk.safe.payload.serializable.fastjson.AutoTypeUser\"\n" +
                "}\n";
        ParserConfig parserConfig = new ParserConfig();
        // 启用autoType
        parserConfig.setAutoTypeSupport(true);
        try {
            // Exception in thread "main" com.alibaba.fastjson.JSONException: autoType is not support. com.hzk.safe.payload.serializable.fastjson.AutoTypeUser
            Object obj = JSON.parseObject(json, Object.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 正常，实例化AutoTypeUser对象
        Object obj1 = JSON.parseObject(json, Object.class, parserConfig);
        System.out.println(obj1);


    }

    /**
     * 彻底关闭 fastjson 的“动态类型能力”
     * 不管你是否开启 AutoType、是否 addAccept、JSON 里有没有 @type，
     * 一律当普通字段处理，绝不实例化任意类。
     */
    @Test
    public void safemodeTest() throws Exception {
        String json = "{\n" +
                "  \"@type\": \"com.hzk.safe.payload.serializable.fastjson.AutoTypeUser\"\n" +
                "}\n";
        ParserConfig parserConfig = new ParserConfig();
        // 启用safemode
        parserConfig.setSafeMode(true);
        try {
            // safeMode not support autoType : com.hzk.safe.payload.serializable.fastjson.AutoTypeUser
            Object obj = JSON.parseObject(json, Object.class, parserConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
