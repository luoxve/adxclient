package org.vvl.adx.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.vvl.adx.logger.Logger;
import org.vvl.adx.logger.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.vvl.adx.client.CommonUtils.loadJsonFileToString;

/**
 * HTTP公共类
 *
 * 请求及响应处理
 * 可请求nurl\impurl\curl
 */
public class HttpCommon {

    private static Logger logger = LoggerFactory.getLogger(HttpCommon.class);
    private static Lock lockGet = new ReentrantLock();
    private static Lock lockPost = new ReentrantLock();

    /**
     * post方式提交json代码
     * @throws Exception
     */
    public static String requestPost(String reqUrl, String jsonFileUrl) throws Exception{
        //创建默认的httpClient实例.
        CloseableHttpClient httpclient = null;
        //接收响应结果
        CloseableHttpResponse response = null;
        String resStr = null;
        try {
            //创建httppost
            httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(reqUrl);
            httpPost.addHeader(HTTP.CONTENT_TYPE,"application/json");
            // 取文件内容
            String json = loadJsonFileToString(jsonFileUrl);
            if (json == null) {
//                if (httpclient != null) httpclient.close();
                return "000";
            }
//            System.out.println("json:" + json);
            StringEntity se = new StringEntity(json, "utf-8");
            se.setContentEncoding("UTF-8");
            //发送json需要设置contentType
            se.setContentType("application/json");
            httpPost.setEntity(se);
            // 设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).setConnectionRequestTimeout(10000).build();
            httpPost.setConfig(requestConfig);
            // 加锁，防止请求地址占用
            lockPost.lock();
            response = httpclient.execute(httpPost);
            lockPost.unlock();
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
            logger.warn("POST mode exception.", e.getMessage());
        } finally {
            try {
                if (httpclient != null) httpclient.close();
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
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            httpclient = HttpClients.createDefault();
            // 创建httpget.
            HttpGet httpGet = new HttpGet(url);
//            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();//设置请求和传输超时时间
//            httpGet.setConfig(requestConfig);
            // 加锁，防止请求地址占用
            lockGet.lock();
            // 执行get请求.
            response = httpclient.execute(httpGet);
            lockGet.unlock();
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
                if (httpclient != null) httpclient.close();
                if (response != null) response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
