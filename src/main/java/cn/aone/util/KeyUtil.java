package cn.aone.util;

import java.util.Random;

/**
 */
public class KeyUtil {

    /**
     * 生成唯一的主键(6位数)
     * 格式: 时间+随机数
     * @return
     */
    public static synchronized String genUniqueKey() {//加synchronized防止多线程情况下并发使主键重复
    	
        Random random = new Random();
        Integer number = random.nextInt(900000) + 100000;

        return System.currentTimeMillis() + String.valueOf(number);
    }
}
