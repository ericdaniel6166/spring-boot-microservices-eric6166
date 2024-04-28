package com.eric6166.base.utils;

import java.time.ZoneId;

public final class BaseConst {
    public static final String DEFAULT_TIME_ZONE_ID_STRING = "UTC"; //change
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of(DEFAULT_TIME_ZONE_ID_STRING); //change

    private BaseConst() {
        throw new IllegalStateException("Utility class");
    }
}
