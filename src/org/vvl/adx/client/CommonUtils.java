package org.vvl.adx.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 公共工具类
 */
public class CommonUtils {
    /**
     * 得到当前时间
     * @return 格式 年月日时分秒毫秒-20170408125753033
     */
    public static String currentTime() {
        Calendar cal = Calendar.getInstance();
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(cal.get(Calendar.MINUTE));
        String second = String.valueOf(cal.get(Calendar.SECOND));
        String ms = String.valueOf(cal.get(Calendar.MILLISECOND));
        month = format(month, 2);
        day = format(day, 2);
        hour = format(hour, 2);
        minute = format(minute, 2);
        second = format(second, 2);
        ms = format(ms, 3);
        return year + month + day + hour + minute + second + ms;
    }

    /**
     * 若不满足指定位数则在前面补0
     * @param str
     * @param len
     * @return
     */
    private static String format(String str, int len) {
        if (str == null) return null;
        if (len < str.length()) return str;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        for (int i = 0; i < len - str.length(); i++) {
            sb.insert(0, "0");
        }
        return sb.toString();
    }

    /**
     * 把指定文件的内容读到String中
     * @param url JSON文件地址
     * @return
     * @throws Exception
     */
    public static String loadJsonFileToString(String url) throws Exception {
        // 检查文件是否存在
        File file = new File(url);
        if (!file.exists()) return null;
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new FileReader(url));
        String lines = null;
        while ((lines = br.readLine()) != null) {
            sb.append(lines);
        }
        br.close();
        return sb.toString();
    }

    /**
     * 从SHELL中查找配置数据
     * @param name
     * @param defaultValue
     * @return
     */
    public static String getSystemEnv(String name,String defaultValue){
        String value = System.getenv(name);
        if (value == null) {
            value = System.getProperty(name);
        }
        if(value==null){
            return defaultValue;
        }else {
            return value;
        }
    }

    /**
     * 按天划分区域块，返回当天年月日，如20170427
     * @return
     */
    public static String divideByDay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(new Date());
    }

    /**
     * 按小时划分区域块，每6个小时算一块区域，一天共分为4块<br>
     * 0-5,6-11,12-17,18-23
     * @return
     */
    public static String divideByHour () {
        String tempHour;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 18) {
            tempHour = "04";
        } else if (hour >= 12) {
            tempHour = "03";
        } else if (hour >= 6) {
            tempHour = "02";
        } else {
            tempHour = "01";
        }
        return divideByDay() + tempHour;
    }

    /**
     * 写日志
     * @param data 二进制数据
     * @param directory 目录
     * @param firstName 主名称
     * @param secondName 副名称
     * @param extension 扩展名
     */
    public static void writeLog(byte[] data, String directory, String firstName, String secondName, String extension){
        try {
//            String dir = Config.STATISTICSLOGDIR;
//            // 文件名
//            String fileName = "statistics";
//            // 每6个小时生成一个Log文件
//            fileName = fileName + divideByHour();
//            // 扩展名
//            String ext = ".log";
            StringBuilder url = new StringBuilder();
            // 生成文件全名
            url.append(directory); // 目录
            url.append(firstName); // 主名
            if (secondName != null) {
                url.append(secondName); // 副名
            }
            url.append(".").append(extension); // 扩展名
            // new文件用于保存日志
            File logFile = new File(url.toString());
            if (!logFile.exists()) logFile.createNewFile();
            // 创建输出流
            FileOutputStream outStream = new FileOutputStream(logFile, true);
            // 写入数据
            outStream.write(data);
            // 关闭输出流
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
