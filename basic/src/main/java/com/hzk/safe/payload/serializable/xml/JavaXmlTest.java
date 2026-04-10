package com.hzk.safe.payload.serializable.xml;

import com.google.common.io.ByteStreams;
import com.hzk.safe.payload.serializable.xml.dto.UsageConfig;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

public class JavaXmlTest {

    public static void main(String[] args) throws Exception{
        JAXBContext jaxbContext = JAXBContext.newInstance(UsageConfig.class);
        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);

        try (InputStream in = UsageConfig.class.getResourceAsStream("/mq/mqqueueconfig.xml")) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteStreams.copy(in, out);
            String xmlString = out.toString("utf-8");
            StringReader reader = new StringReader(xmlString);
            XMLStreamReader xsr = xif.createXMLStreamReader(reader);
            Unmarshaller un = jaxbContext.createUnmarshaller();
            UsageConfig usageConfig = (UsageConfig) un.unmarshal(xsr);
            System.out.println(usageConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
