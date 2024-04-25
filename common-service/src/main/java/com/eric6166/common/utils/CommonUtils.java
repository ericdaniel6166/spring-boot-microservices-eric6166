package com.eric6166.common.utils;

import com.eric6166.base.utils.BaseConst;

public final class CommonUtils {
    public static String get() {
        return BaseConst.BASE;
    }

    private CommonUtils() {
        throw new IllegalStateException("Utility class");
    }
}
