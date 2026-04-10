package com.hzk.safe.payload.serializable.xml;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;

public class XmlDecoderTest {

    public static void main(String[] args) throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<java>\n" +
                "    <object class=\"java.lang.Thread\">\n" +
                "        <void method=\"sleep\">\n" +
                "            <long>5000</long>\n" +
                "        </void>\n" +
                "    </object>\n" +
                "</java>";
        XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(xml.getBytes()));
        Object result = decoder.readObject();  // 这里会执行 sleep(5000)
        decoder.close();
    }

}
