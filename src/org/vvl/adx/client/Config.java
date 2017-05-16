package org.vvl.adx.client;

import static org.vvl.adx.client.CommonUtils.getSystemEnv;

/**
 * 配置类<br>
 */
public class Config {
    // 开启线程数
    public static int THREADCOUNTS = 10;
    // 每线程执行次数（-1：永久执行）
    public static int EXECOUNTS = 10;
    // 胜出概率（-1：不执行，100：百分百执行）
    public static int WINBOUND = 30;
    // 曝光点击概率（-1：不执行，100：百分百执行，-2：随机概率）
    public static int CLKBOUND = 30;
    // 请求URL（DSP竞价URL）
    public static String DSPBIDURL = "http://60.206.107.185:8080/mi/bid";
    // JSON文件所在目录
    public static String JSONDIR = "/opt/adxclient/json/";
    // 统计日志存放目录
    public static String STATISTICSLOGDIR = "/opt/adxclient/log/";
    // 线程监控日志存放目录
    public static String THREADMONITORLOGDIR = "/opt/adxclient/log/";

    // 是否启动统计日志功能
    public static boolean ISSTATISTICSLOG = true;
    // 是否启动线程监控日志功能
    public static boolean ISMONITORLOG = true;
    // 是否启动图片加载
    public static boolean ISIMGSHOW = true;

    /**
     * 初始化配置数据
     */
    public static void init() {
        // 线程数
        Config.THREADCOUNTS = Integer.valueOf(getSystemEnv("THREADCOUNTS", "10"));
        // 每线程执行次数（-1：永久执行）
        Config.EXECOUNTS = Integer.valueOf(getSystemEnv("EXECOUNTS", "-1"));
        // 胜出概率（-1：不执行，100：百分百执行）
        Config.WINBOUND = Integer.valueOf(getSystemEnv("WINBOUND", "100"));
        // 曝光点击概率（-1：不执行，100：百分百执行，-2：随机概率）
        Config.CLKBOUND = Integer.valueOf(getSystemEnv("CLKBOUND", "100"));
        // JSON文件所在目录
        Config.JSONDIR = getSystemEnv("JSONDIR", "/opt/adxclient/json/"); // /opt/adxclient/json/ or E:\data\json2\500\
        // 统计日志存放目录
        Config.STATISTICSLOGDIR = getSystemEnv("STATISTICSLOGDIR", "/opt/adxclient/log/"); // /opt/adxclient/log/ or E:\data\log\
        // 线程监控日志存放目录
        Config.THREADMONITORLOGDIR = getSystemEnv("THREADMONITORLOGDIR", "/opt/adxclient/log/"); // /opt/adxclient/log/ or E:\data\log\
        // 是否启动统计日志功能
        Config.ISSTATISTICSLOG = Boolean.valueOf(getSystemEnv("ISSTATISTICSLOG", "true"));
        // 是否启动线程监控日志功能
        Config.ISMONITORLOG = Boolean.valueOf(getSystemEnv("ISMONITORLOG", "true"));
        // 是否启动图片加载
        Config.ISIMGSHOW = Boolean.valueOf(getSystemEnv("ISIMGSHOW", "false"));
    }
}
