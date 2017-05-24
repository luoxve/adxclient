package org.vvl.adx.client;

import static org.vvl.adx.client.CommonUtils.getSystemEnv;

/**
 * 配置类<br>
 */
public class Config {
    // 开启线程数
    public static int THREADCOUNTS;
    // 每线程执行次数（-1：永久执行）
    public static int EXECOUNTS;
    // 胜出概率（-1：不执行，100：百分百执行）
    public static int WINBOUND;
    // 曝光点击概率（-1：不执行，100：百分百执行，-2：随机概率）
    public static int CLKBOUND;
    // JSON文件所在目录
    public static String JSONDIR;
    // 统计日志存放目录
    public static String STATISTICSLOGDIR;
    // 线程监控日志存放目录
    public static String THREADMONITORLOGDIR;

    // 请求URL（DSP竞价URL）
    public static String DSPBIDURL;
    // 胜出URL
    public static String WINURL;
    // 图片URL
    public static String IMGURL;
    // 曝光展示URL
    public static String IMPURL;
    // 曝光点击URL
    public static String CLICKURL;

    // 是否启动统计日志功能
    public static boolean ISSTATLOG;
    // 是否启动统计204日志功能（若ISSTATLOG为false则些配置失效）
    public static boolean ISSTAT204LOG;
    // 是否启动统计000日志功能（若ISSTATLOG为false则些配置失效）
    public static boolean ISSTAT000LOG;
    // 是否启动线程监控日志功能
    public static boolean ISMONITORLOG;
    // 是否启动图片加载
    public static boolean ISIMGSHOW;

    // CONNECT_TIMEOUT
    public static int CONNECT_TIMEOUT;
    // SOCKET_TIMEOUT
    public static int SOCKET_TIMEOUT;
    // CONNECTION_REQUEST_TIMEOUT
    public static int CONNECTION_REQUEST_TIMEOUT;

    /**
     * 初始化配置数据
     */
    public static void init() {
        // 线程数
        Config.THREADCOUNTS = Integer.valueOf(getSystemEnv("THREADCOUNTS", "50"));
        // 每线程执行次数（-1：永久执行）
        Config.EXECOUNTS = Integer.valueOf(getSystemEnv("EXECOUNTS", "-1"));
        // 胜出概率（-1：不执行，100：百分百执行）
        Config.WINBOUND = Integer.valueOf(getSystemEnv("WINBOUND", "10"));
        // 曝光点击概率（-1：不执行，100：百分百执行，-2：随机概率[1,10]%）
        Config.CLKBOUND = Integer.valueOf(getSystemEnv("CLKBOUND", "-2"));
        // JSON文件所在目录
        Config.JSONDIR = getSystemEnv("JSONDIR", "E:/opt/adxclient/json/"); // /opt/adxclient/json/ or E:/opt/adxclient/json/
        // 统计日志存放目录
        Config.STATISTICSLOGDIR = getSystemEnv("STATISTICSLOGDIR", "E:/opt/adxclient/log/"); // /opt/adxclient/log/ or E:/opt/adxclient/log/
        // 线程监控日志存放目录
        Config.THREADMONITORLOGDIR = getSystemEnv("THREADMONITORLOGDIR", "E:/opt/adxclient/log/"); // /opt/adxclient/log/ or E:/opt/adxclient/log/
        // 是否启动统计日志功能
        Config.ISSTATLOG = Boolean.valueOf(getSystemEnv("ISSTATLOG", "false"));
        // 是否启动线程监控日志功能
        Config.ISMONITORLOG = Boolean.valueOf(getSystemEnv("ISMONITORLOG", "false"));
        // 是否启动图片加载
        Config.ISIMGSHOW = Boolean.valueOf(getSystemEnv("ISIMGSHOW", "false"));
        // 是否启动统计204日志功能（若ISSTATLOG为false则些配置失效）
        Config.ISSTAT204LOG = Boolean.valueOf(getSystemEnv("ISSTAT204LOG", "false"));
        // 是否启动统计000日志功能（若ISSTATLOG为false则些配置失效）
        Config.ISSTAT000LOG = Boolean.valueOf(getSystemEnv("ISSTAT000LOG", "false"));

        // 请求URL（DSP竞价URL）
        Config.DSPBIDURL = getSystemEnv("DSPBIDURL", "http://dsp.tsapk.com/mi/bid"); // http://60.206.107.185:8080/mi/bid or http://dsp.tsapk.com/mi/bid
        // 胜出URL
        Config.WINURL = getSystemEnv("WINURL", "http://60.206.107.185/xiaomi/v1/w");
        // 图片URL
        Config.IMGURL = getSystemEnv("IMGURL", "http://p3.ifengimg.com/a/2017_14/43f07fbeb718f7f_size33_w652_h352.jpg");
        // 曝光展示URL
        Config.IMPURL = getSystemEnv("IMPURL", "http://60.206.107.185/xiaomi/v1/i");
        // 曝光点击URL
        Config.CLICKURL = getSystemEnv("CLICKURL", "http://60.206.107.185/xiaomi/v1/c");

        // CONNECT_TIMEOUT
        Config.CONNECT_TIMEOUT = Integer.valueOf(getSystemEnv("CONNECT_TIMEOUT", "10000"));
        // SOCKET_TIMEOUT
        Config.SOCKET_TIMEOUT = Integer.valueOf(getSystemEnv("SOCKET_TIMEOUT", "10000"));
        // CONNECTION_REQUEST_TIMEOUT
        Config.CONNECTION_REQUEST_TIMEOUT = Integer.valueOf(getSystemEnv("CONNECTION_REQUEST_TIMEOUT", "2000"));
    }
}
