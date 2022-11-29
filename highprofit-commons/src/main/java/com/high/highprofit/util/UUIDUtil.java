package com.high.highprofit.util;

import java.util.UUID;

/**
 * UUID工具类
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
public class UUIDUtil {
    private UUIDUtil() {}

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
