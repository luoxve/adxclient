package org.vvl.adx.client;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 统计
 */
public class StatisticsData {
    // 200请求次数
    public static AtomicInteger request200Counts = new AtomicInteger(0);
    // 点击次数
    public static AtomicInteger clickCounts = new AtomicInteger(0);
    // 曝光次数
    public static AtomicInteger impCounts = new AtomicInteger(0);
    // 图片加载次数
    public static AtomicInteger imgCounts = new AtomicInteger(0);

    public static void printCounts() {
        System.out.println("clickCounts:\t" + clickCounts.get() + "\timpCounts:\t" + impCounts.get() + "\timgCounts:\t" + imgCounts.get());
    }
}
