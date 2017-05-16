package org.vvl.adx.client;

/**
 * 图片加载<br>
 * 从队列中拿到图片地址，有就加载此图片，没有就等待
 */
public class Impress implements Runnable {

    // 图库
    private Gallery gallery;

    public Impress(Gallery gallery) {
        this.gallery = gallery;
    }
    @Override
    public void run() {
        while (true) {
            try {
                String imgUrl = gallery.take();
                ImgShow.adShow(imgUrl);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
