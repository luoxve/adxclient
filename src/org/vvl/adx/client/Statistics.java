package org.vvl.adx.client;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.vvl.adx.client.CommonUtils.divideByHour;

public class Statistics implements Runnable {

    @Override
    public void run() {
        Date beginDate = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        while (true) {
            StringBuilder sb = new StringBuilder();
            int requestAll = StatisticsData.request200Counts.get() + StatisticsData.request204Counts.get() + StatisticsData.request200Counts.get();
            // 日志数据
            sb.append("reqAllCounts:\t").append(requestAll)
                    .append("\treq200Counts:\t").append(StatisticsData.request200Counts.get())
                    .append("\treq204Counts:\t").append(StatisticsData.request204Counts.get())
                    .append("\treq000Counts:\t").append(StatisticsData.request000Counts.get())
                    .append("\timpCounts:\t").append(StatisticsData.impCounts.get())
                    .append("\timgCounts:\t").append(StatisticsData.imgCounts.get())
                    .append("\tclickCounts:\t").append(StatisticsData.clickCounts.get())
                    .append("\t");
            Date currentDate = Calendar.getInstance().getTime();
            sb.append("used time:\t" + (currentDate.getTime() - beginDate.getTime()) + "\t");
            sb.append("begin time:\t").append(format.format(beginDate)).append("\t");
            sb.append("curr time:\t").append(format.format(currentDate)).append("\n");
            writeLog(sb.toString().getBytes());
            try {
                TimeUnit.SECONDS.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写数据
     * @param data 二进制数据
     * @throws Exception
     */
    private static void writeLog(byte[] data){
        CommonUtils.writeLog(data, Config.STATISTICSLOGDIR, "statistics", divideByHour(), "log");
    }

    public static void main(String[] args) {
//        writeLog("abcdefg\n".getBytes());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        Date date = new Date();
//        System.out.println(format.format(date));

    }
}
