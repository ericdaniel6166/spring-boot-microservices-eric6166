package com.eric6166.jpa.utils;

import org.springframework.data.domain.Sort;

public class PageConst {
    public static final String ID = "id";
    public static final Sort.Order DEFAULT_SORT_ORDER = new Sort.Order(Sort.Direction.ASC, ID);

    private PageConst() {
        throw new IllegalStateException("Utility class");
    }
}
