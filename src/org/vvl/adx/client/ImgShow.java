package org.vvl.adx.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.vvl.adx.client.CommonUtils.currentTime;

/**
 * 模拟广告（图片）加载展示
 */
public class ImgShow {
    /**
     * 广告展示
     * @param imgurl adm.imgurls
     */
    public static void adShow(String imgurl) throws Exception {
        // new一个URL对象
        URL url = new URL(imgurl);
        // 打开链接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置请求方式为GET
        conn.setRequestMethod("GET");
        // 超时响应时间为2秒
        conn.setConnectTimeout(2 * 1000);
        // 通过输入流获取图片数据
        InputStream inStream = conn.getInputStream();
        // 得到图片的二进制数据
        byte[] data = readInputStream(inStream);
        // 写成图片在固定目录下
//        writeImg(data);
    }

    /**
     * 生成图片
     * @param data 二进制图片数据
     * @throws Exception
     */
    private static void writeImg(byte[] data) {
        try {
            // 取时间
            String time = currentTime();
            // 目录
            String dir = "E:\\data\\img\\";
            // 文件名
            String fileName = "image" + time;
            // 扩展名
            String ext = ".jpg";
            // new文件用于保存图片
            File imgFile = new File(dir + fileName + ext);
            // 创建输出流
            FileOutputStream outStream = new FileOutputStream(imgFile);
            // 写入数据
            outStream.write(data);
            // 关闭输出流
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读输入流内容到输出流后转成byte数组
     * @param inStream
     * @return
     */
    public static byte[] readInputStream(InputStream inStream) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            // 创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            // 每次读取的字符串长度，如果为-1代表全部读取完毕
            int len = 0;
            // 从输入流中读取数据放入缓冲区中
            while ((len = inStream.read(buffer)) != -1) {
                // 从缓冲区中把数据写入到输出流
                outStream.write(buffer, 0, len);
            }
            // 把输出流内数据写入内存
            byte[] content = outStream.toByteArray();
            // 关闭输入流
            inStream.close();
            // 关闭输出流
            outStream.close();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
