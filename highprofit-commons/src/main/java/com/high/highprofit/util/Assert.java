package com.high.highprofit.util;

import com.high.highprofit.exception.ServiceException;

/**
 * 断言工具类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
public class Assert {
    private Assert() {}

    /**
     * 断言字符串是否为空
     * @param str 字符串
     * @param message 错误信息
     */
    public static void isEmpty(String str, String message) {
        if (str == null || "".equals(str)) {
            throw new ServiceException(message);
        }
    }

    /**
     * 断言是否符合条件
     * @param flag 条件表达式结果
     * @param message 错误信息
     */
    public static void isFlag(Boolean flag, String message) {
        if (flag) {
            throw new ServiceException(message);
        }
    }
}
