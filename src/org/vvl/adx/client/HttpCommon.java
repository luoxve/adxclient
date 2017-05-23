package org.vvl.adx.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.vvl.adx.logger.Logger;
import org.vvl.adx.logger.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * HTTP公共类
 *
 * 请求及响应处理
 * 可请求nurl\impurl\curl
 */
public class HttpCommon {

    private static Logger logger = LoggerFactory.getLogger(HttpCommon.class);
    private static final Lock lockGet = new ReentrantLock();
    private static final Lock lockPost = new ReentrantLock();

    // 创建默认的httpClient实例.
    private static CloseableHttpClient httpClient;
    // 设置请求和传输超时时间
    private static RequestConfig requestConfig;

    /**
     * post方式提交json代码
     * @throws Exception
     */
    public static String requestPost(String reqUrl, String json){
        if (json == null) return "000";
        //接收响应结果
        CloseableHttpResponse response = null;
        String resStr = null;
        try {
            if (httpClient == null ) {
                // 创建HttpClient
                httpClient = getHttpClient();;
            }
            // 创建HttpPost
            HttpPost httpPost = new HttpPost(reqUrl);
            httpPost.addHeader(HTTP.CONTENT_TYPE,"application/json");
//            System.out.println("json:" + json);
            StringEntity se = new StringEntity(json, "utf-8");
            se.setContentEncoding("UTF-8");
            //发送json需要设置contentType
            se.setContentType("application/json");
            httpPost.setEntity(se);
            // 设置请求和传输超时时间
            if (requestConfig == null) {
                requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).setConnectionRequestTimeout(5000).build();
            }
            httpPost.setConfig(requestConfig);
            // 加锁，防止请求地址占用
//            lockPost.lock();
//            try {
                response = httpClient.execute(httpPost);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                logger.warn("httpClient execute exception.", e.getMessage());
//            } finally {
//                lockPost.unlock();
//            }
            // 处理响应码
            int respCode = response.getStatusLine().getStatusCode();
            if (respCode == 204 || respCode == 400) {
//                if (httpclient != null) httpclient.close();
//                if (response != null) response.close();
                return "204";
            }
            //解析返结果
            HttpEntity entity = response.getEntity();
            if(entity != null){
                resStr = EntityUtils.toString(entity, "UTF-8");
//                System.out.println("###:" + resStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
//                if (httpclient != null) httpclient.close();
                if (response != null) response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resStr;
    }

    /**
     * 发送get请求
     * @param url nurl\impurl\curl
     * @throws Exception
     */
    public static void requestGet(String url){
        CloseableHttpResponse response = null;
        try {
            if (httpClient == null) {
                // 创建HttpClient
                httpClient = getHttpClient();
            }
            // 创建httpget.
            HttpGet httpGet = new HttpGet(url);
            // 设置请求和传输超时时间
            if (requestConfig == null) {
                requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).setConnectionRequestTimeout(5000).build();
            }
            httpGet.setConfig(requestConfig);
            // 加锁，防止请求地址占用
//            lockGet.lock();
            // 执行get请求.
            response = httpClient.execute(httpGet);
//            try {
//                // 执行get请求.
//                response = httpclient.execute(httpGet);
//            } catch (Exception e) {
//                logger.warn("httpClient execute exception.", e.getMessage());
//            } finally {
//                lockGet.unlock();
//            }
            // 获取响应实体
            HttpEntity entity = response.getEntity();

            // 打印响应状态
//            System.out.println(response.getStatusLine().getStatusCode());
            if (entity != null) {
                // 打印响应内容
//                System.out.println("Response content: " + EntityUtils.toString(entity));
            }
        } catch (Exception e) {
            logger.warn("GET mode exception.", e.getMessage());
        } finally {
            try {
//                if (httpclient != null) httpclient.close();
                if (response != null) response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭HttpClient
     */
    public static void close() {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.info("httpClient close exception.");
            }
        }
    }

    public static synchronized CloseableHttpClient getHttpClient() {

        if (httpClient == null) {
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            // 连接池最大连接数
            cm.setMaxTotal(1000);
            // 单条链路最大连接数（一个ip+一个端口 是一个链路）
            cm.setDefaultMaxPerRoute(500);
            // 指定某条链路的最大连接数

            ConnectionKeepAliveStrategy kaStrategy = new DefaultConnectionKeepAliveStrategy() {
                @Override
                public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                    long keepAlive = super.getKeepAliveDuration(response, context);
                    if (keepAlive == -1) {
                        keepAlive = 60000;
                    }
                    return keepAlive;
                }

            };

            httpClient = HttpClients.custom().setConnectionManager(cm).setKeepAliveStrategy(kaStrategy).build();
        }

        return httpClient;
    }

    public static void main(String[] args) {
        new HttpCommon().requestGet("http://60.206.107.185/xiaomi/v1/w");
        new HttpCommon().requestGet("http://60.206.107.185/xiaomi/v1/i");
        new HttpCommon().requestGet("http://60.206.107.185/xiaomi/v1/c");
        //new HttpCommon().adShow("http://p3.ifengimg.com/a/2017_14/43f07fbeb718f7f_size33_w652_h352.jpg");
        //System.out.println(new HttpCommon().currentTime());
//        try {
//            new HttpCommon().adShow("http://p3.ifengimg.com/a/2017_14/43f07fbeb718f7f_size33_w652_h352.jpg");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
