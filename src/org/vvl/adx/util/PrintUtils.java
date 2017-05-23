package org.vvl.adx.util;

import org.vvl.adx.logger.Logger;
import org.vvl.adx.logger.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 集合打印工具类
 * <p>方法命名规则<br>
 * <i>print + 需打印集合的类型 + 集合内元素类型或键值类型</i><br>
 * 如打印集合：Map<String,HashSet<String>> map<br>
 * 则方法命名为：printMapStrSetStr
 * </p>
 */
public class PrintUtils {
    private static Logger logger = LoggerFactory.getLogger(PrintUtils.class);

    public static void printMapStrStr(String mapName,Map<String,String> map){
        logger.debug("  Map " + mapName + " size: " + map.size());
        for(String key:map.keySet()){
            String value = map.get(key);
            logger.debug("    key: " + key + " value:" + value);
        }
        logger.debug("----------------------");
    }

    public static void printMapIntStr(String mapName,Map<Integer,String> map){
        logger.debug("  Map " + mapName + " size: " + map.size());
        for(Integer key:map.keySet()){
            String value = map.get(key);
            logger.debug("    key: " + key + " value:" + value);
        }
        logger.debug("----------------------");
    }

    public static void printMapStrInt(String name,Map<String,Integer>map){
        logger.debug("Map " + name + "size: " + map.size());
        for(String key:map.keySet()){
            logger.debug(" key: " + key + " count:" + map.get(key));
        }
    }

    public static void printMapStrSetStr(String mapName,Map<String,HashSet<String>> map){
        logger.debug(" Map " + mapName + " size: " + map.size());
        for(String key:map.keySet()){
            HashSet<String> lists = map.get(key);
            String str = " key: " + key + " listSize:" + lists.size() + " [";
            int count = 0;
            for (String list : lists) {
                str = str + list + " ";
                count++;
                if(count%8==0)str = str + "\n";
            }
            logger.debug(str + "]");
        }
        logger.debug("--------------------------");
    }

    public static void printListStr(String name,List<String> lists){
        String listStr = "";
        for(String str:lists){
            listStr = listStr + " " + str;
        }
        logger.debug("  " + name + " size:" + lists.size() + " details: " + listStr);
    }
}
