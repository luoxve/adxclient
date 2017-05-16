package org.vvl.adx.client;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 图库
 */
public class Gallery {

    // 图片队列
    private BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    /**
     * 将图片放入图库
     * @param imgUrl
     */
    public void put(String imgUrl) {
        try {
            queue.put(imgUrl);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从图库中拿出图片
     * @return
     */
    public String take() {
        String imgUrl = null;
        try {
            imgUrl = queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return imgUrl;
    }
}
