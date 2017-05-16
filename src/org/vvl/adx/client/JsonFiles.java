package org.vvl.adx.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class JsonFiles {
    // JSON文件
    public static List<String> fileNames = new ArrayList<>();
    private static AtomicInteger count = new AtomicInteger(0);

    /**
     * 初始化所有JSON文件的名称
     */
    public static void init() {
        String dir = Config.JSONDIR;
        File jsonDir = new File(dir);
        if (!jsonDir.exists()) {
            System.out.println("JSON dir not exist!");
            return;
        }
        if (!jsonDir.isDirectory()) {
            System.out.println("this is not directory!");
            return;
        }
        File[] files = jsonDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (!file.isDirectory()) {
                fileNames.add(file.getName());
            }
        }
    }

    /**
     * 顺序读取目录下的所有JSON文件
     * @return
     */
    public static String sequenceJsonFileName() {
        // 初始化所有JSON文件名称
//        init();
        if (count.get() < fileNames.size()) {
            int index = count.getAndAdd(1);
            return fileNames.get(index);
        }
        return "END";
    }

    public static void main(String[] args) {
        init();
    }
}
