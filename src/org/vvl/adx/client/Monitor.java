package org.vvl.adx.client;

import static org.vvl.adx.client.CommonUtils.divideByHour;

/**
 * 监控各线程执行状态
 */
public class Monitor implements Runnable {

    private Pool pool;

    public Monitor(Pool pool) {
        this.pool = pool;
    }

    @Override
    public void run() {
        while (true) {
            String msg = pool.take();
            writeLog(msg.getBytes());
        }
    }

    /**
     * 写数据
     * @param data 二进制数据
     * @throws Exception
     */
    private static void writeLog(byte[] data){
        CommonUtils.writeLog(data, Config.THREADMONITORLOGDIR, "monitor", divideByHour(), "log");
    }
}
