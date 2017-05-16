package org.vvl.adx.client;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Global {
    // 图片队列
    public static BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    // 开始时间
    public static Date startDate = Calendar.getInstance().getTime();
    // 结束时间
    public static Date endDate = Calendar.getInstance().getTime();
    public static int count = 1;
}
