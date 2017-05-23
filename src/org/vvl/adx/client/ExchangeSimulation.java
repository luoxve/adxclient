package org.vvl.adx.client;

import org.vvl.adx.logger.Logger;
import org.vvl.adx.logger.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.vvl.adx.client.HttpCommon.*;
import static org.vvl.adx.client.JsonFiles.fileContents;
import static org.vvl.adx.client.JsonFiles.fileNames;

/**
 * 平台模拟
 */
public class ExchangeSimulation implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(ExchangeSimulation.class);
    private Gallery gallery;
    private Pool pool;

    public ExchangeSimulation(Gallery gallery, Pool pool) {
        this.gallery = gallery;
        this.pool = pool;
    }

    // 请求URL
    String reqBidUrl = Config.DSPBIDURL;
    // 胜出URL
    String winUrl = Config.WINURL;
    // 图片URL
    String imgUrl = Config.IMGURL;
    // 曝光展示URL
    String impUrl = Config.IMPURL;
    // 曝光点击URL
    String clickUrl = Config.CLICKURL;
    // JSON文件URL
    String jsonFileUrl = Config.JSONDIR;
    // 加密价格
    String price = "cHJpY2VlbmNvZGluZ3doZTzKyOjDQx_zjiVOmw";

    // 随机下标index
    int randomIndex;

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "\tbegin.");
        Global.startDate = new Date();
        int i = Config.EXECOUNTS;
        boolean flag = false;
        // 当i==-1时设置i永久为1，循环一直执行
        if (i == -1) {
            flag = true;
            i = 1;
        }
        int count = 0;
        while (i > 0) {
            if (!flag) {
                i--;
            }
            count++;
            try {
                // 随机取JSON文件数据
                Map<String, String> dataMap = randomJsonFileContent();
                if (dataMap == null) continue;
                // 当前文件名称
                String fileName = dataMap.get("name");
                // 当前文件内容(json)
                String jsonContent = dataMap.get("json");
//                String fileName = sequenceJsonFileName();
                // 1.请求DSP
                String respStr = requestPost(reqBidUrl, jsonContent);
                if (respStr == null) continue;
                // 没有响应不成功则继续下一次文请求
                if (respStr == "000") {
                    if (Config.ISMONITORLOG) { // 是否启动线程监控
                        String monitorLog = Thread.currentThread().getName() + "\t" + count + "\t" + fileName + "\t000\tthis file is not exists.\n";
                        pool.put(monitorLog);
                    }
                    continue;
                }
                if (respStr == "204") {
                    if (Config.ISMONITORLOG) { // 是否启动线程监控
                        String monitorLog = Thread.currentThread().getName() + "\t" + count + "\t" + fileName + "\t204\tno contents.\n";
                        pool.put(monitorLog);
                    }
                    continue;
                }
                if (Config.ISMONITORLOG) { // 是否启动线程监控
                    String monitorLog = Thread.currentThread().getName() + "\t" + count + "\t" + fileName + "\t200.\n";
                    pool.put(monitorLog);
                }
                // 统计响应200的请求
                if (Config.ISSTATISTICSLOG) StatisticsData.request200Counts.incrementAndGet();
                // 2.解析所需要的数据
                ResonpseJsonAnalyzer respJson = new ResonpseJsonAnalyzer(respStr);
                // 初始化数据
                respJson.analyze();
                // 胜出请求
                winUrl = respJson.getNurl();
                // 图片URL
                imgUrl = respJson.getImgurl();
                // 曝光展示URL
                impUrl = respJson.getImpurl();
                // 曝光点击URL
                clickUrl = respJson.getCurl();
                // 模拟胜出
                if (random(Config.WINBOUND)) {
                    winUrl = replaceWinPrice(winUrl);
                    // 3.胜出请求nurl
                    requestGet(winUrl);
                    if (imgUrl != null && Config.ISIMGSHOW) { // && 是否启动图片加载功能
                        // 4.曝光图片加载
//                        adShow(imgUrl);
                        if (Config.ISIMGSHOW) gallery.put(imgUrl);
                        // 图片加载统计
                        if (Config.ISSTATISTICSLOG) StatisticsData.imgCounts.incrementAndGet();
                    }
                    if (impUrl != null) {
                        // 5.曝光请求
                        impUrl = replaceWinPrice(impUrl);
                        requestGet(impUrl);
                        // 曝光请求统计
                        if (Config.ISSTATISTICSLOG)  StatisticsData.impCounts.incrementAndGet();
                    }
                    if (clickUrl != null && random(Config.CLKBOUND)) {
                        // 替换曝光价格（胜出价格）
                        clickUrl = replaceWinPrice(clickUrl);
                        // 6.曝光点击
                        requestGet(clickUrl);
                        // 点击统计
                        if (Config.ISSTATISTICSLOG) StatisticsData.clickCounts.incrementAndGet();
                    }
                }
            } catch (Exception e) {
                logger.warn(Thread.currentThread().getName() + " EXCEPTION.", e.getMessage());
            }
        }
        Global.endDate = new Date();
        System.out.println("used time:" + (Global.endDate.getTime() - Global.startDate.getTime()));
        // 关闭HttpClient
        close();
    }

    private String replaceWinPrice(String nurl) {
        return nurl.replace("{WIN_PRICE}", price);
    }

    /**
     * 按指定概率返回true
     * @param bound [0, 100)
     * @return
     */
    private boolean random(int bound) {
        if (bound == 100) return true;
        if (bound == -1) return false;
        if (bound == -2) {
            bound = randomIndex % 10 + 1;
        }
        int number = new Random().nextInt(100);
        if (number < bound) {
            return true;
        }
        return false;
    }

    /**
     * 随机抽取一个JSON文件内容
     * @return
     */
    private Map<String, String> randomJsonFileContent() {
        Map<String, String> ret = new HashMap<>();
        if (fileContents.size() == 0 || fileNames.size() == 0) return null;
        randomIndex = new Random().nextInt(fileContents.size());
        ret.put("name", fileNames.get(randomIndex));
        ret.put("json", fileContents.get(randomIndex));
        return ret;
    }
}
