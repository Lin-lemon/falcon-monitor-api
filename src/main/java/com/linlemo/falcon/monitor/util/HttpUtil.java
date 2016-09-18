package com.linlemo.falcon.monitor.util;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lin.zhao
 * Email: linlemo@gmail.com
 * Date: 16/9/1
 * Time: 19:06
 */
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static void post(String content, String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        StringEntity se = new StringEntity(content, "UTF-8");
        se.setContentEncoding("UTF-8");
        se.setContentType("application/json");

        httpPost.setEntity(se);
        try {
            httpClient.execute(httpPost);
        } catch (IOException e) {
            logger.error("http post ioexception", e);
        }
        // 关闭连接,释放资源
        httpClient.close();
    }
}
