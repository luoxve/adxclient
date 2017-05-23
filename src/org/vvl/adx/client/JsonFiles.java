package org.vvl.adx.client;

import org.vvl.adx.util.PrintUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class JsonFiles {
    // JSON文件名称
    public static Map<Integer, String> fileNames = new HashMap<>();
    // JSON文件内容（key:位置下标，value:文件内容）
    public static Map<Integer, String> fileContents = new HashMap<>();
    private static AtomicInteger count = new AtomicInteger(0);

    /**
     * 初始化所有JSON文件的名称
     * （现只支持同一个目录下的文件）
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
                String json = CommonUtils.loadJsonFileToString(file);
                fileContents.put(i, json);
                fileNames.put(i, file.getName());
            }
        }
    }

    /**
     * 顺序读取目录下的所有JSON文件
     * @return
     */
    public static String sequenceJsonFileContent() {
        // 初始化所有JSON文件名称
        if (count.get() < fileContents.size()) {
            int index = count.getAndAdd(1);
            return fileContents.get(index);
        }
        return "END";
    }

    public static void main(String[] args) {
        Config.init();
        init();
        PrintUtils.printMapIntStr("fileContents", fileContents);
    }
}
