import org.vvl.adx.client.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * adx仿真测试
 */
public class Main {

    private void test() {
        // 图库
        Gallery gallery = new Gallery();
        // 线程监控日志队列
        Pool pool = new Pool();
        // 初始化环境变量（此处不开启默认使用Config类中的配置）
        Config.init();
        // 初始化JSON文件
        JsonFiles.init();

        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < Config.THREADCOUNTS; i ++) {
            // 模拟ADX
            exec.submit(new ExchangeSimulation(gallery, pool));
        }
        for (int i = 0; i < Config.THREADCOUNTS / 2; i++) {
            if (Config.ISIMGSHOW)
                // 模拟图片加载展示（曝光）
                exec.submit(new Impress(gallery));
            if (Config.ISMONITORLOG)
                // 输出线程监控信息
                exec.submit(new Monitor(pool));
        }
        if (Config.ISSTATLOG)
            // 输出统计信息
            exec.submit(new Statistics());
        exec.shutdown();
    }

    public static void main(String[] args) {
        new Main().test();
    }
}
