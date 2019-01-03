package cn.aone.util;

import cn.aone.enums.CodeEnum;
/**
 * 根据code获取枚举的工具类
 * @author 开发
 *
 */

public class EnumUtil {

    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each: enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }
}
