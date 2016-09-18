package com.flightroutes.flight.monitor.util;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lin.zhao
 * Email: linlemo@gmail.com
 * Date: 16/9/1
 * Time: 19:06
 */
public class HttpUtil {

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
            e.printStackTrace();
        }
        // 关闭连接,释放资源
        httpClient.close();
    }
}
