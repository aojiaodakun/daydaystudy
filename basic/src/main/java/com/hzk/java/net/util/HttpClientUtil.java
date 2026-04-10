package com.hzk.java.net.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    private static int default_connectionTimeout = 3000;
//    private static int default_readTimeout = 5000;
    private static int default_readTimeout = 30000;
    private static String SCHEME_HTTPS = "https";

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private static HttpClient createHttpClient(int connectionTimeout, int readTimeout) {
        HttpClient client = HttpClientBuilder.create().useSystemProperties()
                .setDefaultRequestConfig(
                        RequestConfig.custom()
                                .setConnectionRequestTimeout(connectionTimeout)
                                .setConnectTimeout(connectionTimeout)
                                .setSocketTimeout(readTimeout).build()
                ).build();
        return client;
    }

    /**
     * @param client
     * @return
     * @method wrapperHttpClient
     * @description 忽略SSL证书验证
     */
    public static HttpClient wrapperHttpClient(HttpClient client) {
        if (client == null) {
            client = createHttpClient(default_connectionTimeout, default_readTimeout);
        }
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme(SCHEME_HTTPS, 443, ssf));
            ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(registry);
            return new DefaultHttpClient(mgr, client.getParams());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 说明：封装HttpClient实现post调用
     *
     * @param url    URL地址
     * @param header 头部信息
     * @param body   body体信息
     * @return URL返回内容
     * @throws IOException
     */
    public static String post(String url, Map<String, String> header, Map<String, Object> body) throws IOException {
        return post(url, header, body, default_connectionTimeout, default_readTimeout);
    }

    public static String post(String url, Map<String, String> header, Map<String, Object> body, int connectionTimeout, int readTimeout) throws IOException {
        String data = "";
        // 创建HttpClient实例
        HttpClient client = createHttpClient(connectionTimeout, readTimeout);
        // 根据URL创建HttpPost实例
        HttpPost post = new HttpPost(url);
        URI uri = post.getURI();
        if (SCHEME_HTTPS.equals(uri.getScheme())) {
            // https时包装为忽略证书的客户端
            client = wrapperHttpClient(client);
            if (client == null) {
                return data;
            }
        }
        try {
            // 傳入header
            if (header != null && header.size() != 0) {
                header.forEach((key, value) -> {
                    post.setHeader(key, value);
                });
            }
            // 传入请求体
            if (body != null && body.size() != 0) {
                List<NameValuePair> params = new ArrayList<NameValuePair>(body.size());
                body.forEach((key, value) -> {
                    params.add(new BasicNameValuePair(key, value.toString()));
                });

                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
                post.setEntity(entity);
            }

            // 发送请求，得到响应体
            HttpResponse response = client.execute(post);
            // 判断是否正常返回
            if (response.getStatusLine().getStatusCode() == 200) {
                // 解析数据
                HttpEntity resEntity = response.getEntity();
                Header respHeader = resEntity.getContentEncoding();
                //Response压缩处理
                if (respHeader != null && ("gzip".equalsIgnoreCase(respHeader.getValue()) || "x-gzip".equalsIgnoreCase(respHeader.getValue()))) {
                    GzipDecompressingEntity gzipEntity = new GzipDecompressingEntity(resEntity);
                    InputStream in = gzipEntity.getContent();
                    data = getHTMLContent(in);
                } else {
                    data = EntityUtils.toString(resEntity);
                }
            }
        } catch (IOException ex) {
            logger.error("post error: " + ex.getMessage(), ex);
            throw ex;
        } finally {
            post.releaseConnection();
        }
        return data;
    }

    public static String postjson(String url, Map<String, String> header, String json) throws IOException {
        return postjson(url, header, json, default_connectionTimeout, default_readTimeout);
    }

    /**
     * 说明：封装HttpClient实现post调用:头content-type:application/json
     *
     * @param url    URL地址
     * @param header 头部信息
     * @return URL返回内容
     * @throws IOException
     */
    public static String postjson(String url, Map<String, String> header, String json, int connectionTimeout, int readTimeout) throws IOException {
        final String CONTENT_TYPE_TEXT_JSON = "text/json;";

        String data = "";
        // 创建HttpClient实例
        HttpClient client = createHttpClient(connectionTimeout, readTimeout);

        // 根据URL创建HttpPost实例
        HttpPost post = new HttpPost(url);
        URI uri = post.getURI();
        if (SCHEME_HTTPS.equals(uri.getScheme())) {
            // https时包装为忽略证书的客户端
            client = wrapperHttpClient(client);
            if (client == null) {
                return data;
            }
        }

        try {
            // 傳入header
            if (header != null && header.size() != 0) {
                header.forEach((key, value) -> {
                    post.setHeader(key, value);
                });
            }
            // 传入请求体
            StringEntity se = new StringEntity(json, ContentType.APPLICATION_JSON);
            se.setContentType(CONTENT_TYPE_TEXT_JSON);
            post.setEntity(se);

            // 发送请求，得到响应体
            HttpResponse response = client.execute(post);
            // 判断是否正常返回
            if (response.getStatusLine().getStatusCode() == 200) {
                // 解析数据
                HttpEntity resEntity = response.getEntity();
                Header respHeader = resEntity.getContentEncoding();
                //Response压缩处理
                if (respHeader != null && ("gzip".equalsIgnoreCase(respHeader.getValue()) || "x-gzip".equalsIgnoreCase(respHeader.getValue()))) {
                    GzipDecompressingEntity gzipEntity = new GzipDecompressingEntity(resEntity);
                    InputStream in = gzipEntity.getContent();
                    data = getHTMLContent(in);
                } else {
                    data = EntityUtils.toString(resEntity);
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            post.releaseConnection();
        }
        return data;
    }

    public static String postAppJson(String url, Map<String, String> header, Map<String, Object> body) throws IOException {
        return postAppJson(url, header, body, default_connectionTimeout, default_readTimeout);
    }

    /***
     * application/json post
     * @param url
     * @param header
     * @param body
     * @param connectionTimeout
     * @param readTimeout
     * @return
     * @throws IOException
     */
    public static String postAppJson(String url, Map<String, String> header, Map<String, Object> body, int connectionTimeout, int readTimeout) throws IOException {
        final String CONTENT_TYPE_TEXT_JSON = "application/json;";

        String data = "";
        // 创建HttpClient实例
        HttpClient client = createHttpClient(connectionTimeout, readTimeout);
        // 根据URL创建HttpPost实例
        HttpPost post = new HttpPost(url);
        URI uri = post.getURI();
        if (SCHEME_HTTPS.equals(uri.getScheme())) {
            // https时包装为忽略证书的客户端
            client = wrapperHttpClient(client);
            if (client == null) {
                return data;
            }
        }

        try {
            // 傳入header
            if (header != null && header.size() != 0) {
                header.forEach((key, value) -> {
                    post.setHeader(key, value);
                });
            }
            String jsEntity = "{}";
            if (body != null) {
                ObjectMapper mapper = new ObjectMapper();
                jsEntity = mapper.writeValueAsString(body);
            }

            // 传入请求体
            StringEntity se = new StringEntity(jsEntity, ContentType.APPLICATION_JSON);
            se.setContentType(CONTENT_TYPE_TEXT_JSON);
            post.setEntity(se);

            // 发送请求，得到响应体
            HttpResponse response = client.execute(post);
            // 判断是否正常返回
            if (response.getStatusLine().getStatusCode() == 200) {
                // 解析数据
                HttpEntity resEntity = response.getEntity();
                Header respHeader = resEntity.getContentEncoding();
                //Response压缩处理
                if (respHeader != null && ("gzip".equalsIgnoreCase(respHeader.getValue()) || "x-gzip".equalsIgnoreCase(respHeader.getValue()))) {
                    GzipDecompressingEntity gzipEntity = new GzipDecompressingEntity(resEntity);
                    InputStream in = gzipEntity.getContent();
                    data = getHTMLContent(in);
                } else {
                    data = EntityUtils.toString(resEntity);
                }
            }
        } catch (IOException ex) {
            logger.error("post error: " + ex.getMessage(), ex);
            throw ex;
        } finally {
            post.releaseConnection();
        }
        return data;
    }

    public static String get(String url) throws Exception {
        return get(url, default_connectionTimeout, default_readTimeout);
    }

    /***
     * 封装HttpClient实现get调用
     * @param connectionTimeout
     * @param readTimeout
     * @param url，请求地址，需要根据参数情况进行封装
     * @return http响应内容
     * @throws Exception
     */
    public static String get(String url, int connectionTimeout, int readTimeout) throws IOException, URISyntaxException {
        BufferedReader in = null;

        String content = null;
        try {
            // 定义HttpClient
            HttpClient client = createHttpClient(connectionTimeout, readTimeout);
            // 实例化HTTP方法
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);

            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));
            StringBuilder sb = new StringBuilder();
            String line;
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line).append(NL);
            }
            content = sb.toString();
        } catch (URISyntaxException | IOException e) {
            logger.error("url: ["+ url + "] get error: " + e.getMessage(), e);
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    //ignore
                }
            }
        }
        return content;
    }

    /**
     * @param url               请求地址，不带问号
     * @param connectionTimeout
     * @param readTimeout
     * @return 相响应结果
     * @throws Exception
     */
    public static String get(String url, Map<String, String> headers, int connectionTimeout, int readTimeout) throws IOException, URISyntaxException {
        String content;
        String strBody = "";

        BufferedReader in = null;
        try {
            String destUrl = "";
            if (StringUtils.isEmpty(strBody)) {
                destUrl = url;
            } else {
                destUrl = url + "?" + strBody;
            }
            // 定义HttpClient
            HttpClient client = createHttpClient(connectionTimeout, readTimeout);
            // 实例化HTTP方法
            HttpGet request = new HttpGet();

            // 傳入header
            if (headers != null && headers.size() != 0) {
                headers.forEach((key, value) -> {
                    request.setHeader(key, value);
                });
                /*
                Iterator<String> iterator = headers.keySet().iterator();
                while(iterator.hasNext()){
                    String key = iterator.next();
                    request.setHeader(key, headers.get(key));
                }
                */
            }

            request.setURI(new URI(destUrl));
            HttpResponse response = client.execute(request);

            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            //String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line);//.append(NL);
            }
            content = sb.toString();
        } catch (IOException | URISyntaxException e) {
            logger.error("url: ["+url+"] get error: " + e.getMessage(), e);
            throw e;
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return content;
    }

    /***
     * 从流获取HTMl的输出数据
     * @param in 输入流
     * @return String
     */
    private static String getHTMLContent(InputStream in) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        try {
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            return sb.toString();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                //ignored
            }
        }
        return sb.toString();
    }

    /**
     * 封装HttpClient实现PUT请求调用
     *
     * @param url
     * @param header
     * @param json
     * @return
     */
    public static String putjson(String url, Map<String, String> header, String json) throws Exception {
        return putjson(url, header, json, default_connectionTimeout, default_readTimeout);
    }

    public static String putjson(String url, Map<String, String> header, String json, int connectionTimeout, int readTimeout) {
        final String CONTENT_TYPE_TEXT_JSON = "text/json;";
        HttpClient client = createHttpClient(connectionTimeout, readTimeout);
        HttpPut httpput = new HttpPut(url);
        String data = "";
        try {
            // 傳入header
            if (header != null && header.size() != 0) {
                header.forEach((key, value) -> {
                    httpput.setHeader(key, value);
                });
            /*
            Iterator<String> iterator = header.keySet().iterator();
            while(iterator.hasNext()){
                String key = iterator.next();
                httpput.setHeader(key, header.get(key));
            }
            */
            }
            // 传入请求体
            if (!StringUtils.isEmpty(json)) {
                StringEntity se = new StringEntity(json, ContentType.APPLICATION_JSON);
                se.setContentType(CONTENT_TYPE_TEXT_JSON);
                httpput.setEntity(se);
            }
            // 发送请求，得到响应体
            HttpResponse response = client.execute(httpput);
            // 判断是否正常返回
            if (response.getStatusLine().getStatusCode() == 200) {
                // 解析数据
                HttpEntity resEntity = response.getEntity();
                Header respHeader = resEntity.getContentEncoding();
                //Response压缩处理
                if (respHeader != null && ("gzip".equalsIgnoreCase(respHeader.getValue()) || "x-gzip".equalsIgnoreCase(respHeader.getValue()))) {
                    GzipDecompressingEntity gzipEntity = new GzipDecompressingEntity(resEntity);
                    InputStream in = gzipEntity.getContent();
                    data = getHTMLContent(in);
                } else {
                    data = EntityUtils.toString(resEntity);
                }
            }
        } catch (IOException ex) {
            return data;
        } finally {
            httpput.releaseConnection();
        }
        return data;
    }

    /**
     * @param url
     * @param headers
     * @param formDatas
     * @return
     * @deprecated
     */
    public static String postFormData(String url, Map<String, String> headers, List<FormDataEntity> formDatas) {
        throw new UnsupportedOperationException();
    }


    public static class FormDataEntity {
        /**
         * 表单字段名称
         */
        private String key;
        /**
         * 表单字段值，文本类型
         */
        private String valueString;
        /**
         * 表单字段值，文件类型
         */
        private InputStream valueInputStream;
        /**
         * 文件类型时的文件名
         **/
        private String fileName;

        /**
         * 文本类型的表单域。
         *
         * @param key
         * @param value
         */
        public FormDataEntity(String key, String value) {
            this.key = key;
            this.valueString = value;
        }

        /**
         * 文件类型的表单域。
         *
         * @param key
         * @param value
         * @param fileName
         */
        public FormDataEntity(String key, InputStream value, String fileName) {
            this.key = key;
            this.valueInputStream = value;
            this.fileName = fileName;
        }

        public String getKey() {
            return key;
        }

        public String getValueString() {
            return valueString;
        }

        public InputStream getValueInputStream() {
            return valueInputStream;
        }

        public String getFileName() {
            return fileName;
        }

    }
}
