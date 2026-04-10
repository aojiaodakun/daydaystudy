package com.hzk.safe.payload.serializable.common.config;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

/**
 * com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl
 * TemplatesImpl 本身是“XSLT 编译结果缓存类”，
 * 因为能加载任意字节码，在“多态反序列化 + 无白名单”的情况下，会被利用成 RCE Gadget。
 */
public class TemplatesImplTest {


    public static void main(String[] args) throws Exception {
        // 1. 一个最简单的 XSLT
        String xslt =
                "<?xml version=\"1.0\"?>\n" +
                        "<xsl:stylesheet version=\"1.0\"\n" +
                        "    xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" +
                        "  <xsl:template match=\"/\">\n" +
                        "    <hello>world</hello>\n" +
                        "  </xsl:template>\n" +
                        "</xsl:stylesheet>";
        // 2. 编译 XSLT → Templates（底层实现就是 TemplatesImpl）
        // 内部生成了com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl
        TransformerFactory factory = TransformerFactory.newInstance();
        Templates templates = factory.newTemplates(
                new StreamSource(new java.io.StringReader(xslt))
        );
        // 3. 使用模板
        Transformer transformer = templates.newTransformer();
        transformer.transform(
                new StreamSource(new java.io.StringReader("<root/>")),
                new StreamResult(System.out)
        );
    }
}

