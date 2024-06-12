package com.eric6166.jpa.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
public final class PageUtils {

    public static final String COLUMN_ID = "id";

    private PageUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Pageable buildPageable(Integer pageNumber, Integer size, String sortColumn, String sortDirection) {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.fromString(sortDirection), COLUMN_ID));
        if (!StringUtils.equalsIgnoreCase(sortColumn, COLUMN_ID)) {
            orders.add(Sort.Order.asc(COLUMN_ID));
        }
        var sort = Sort.by(orders);
        return PageRequest.of(pageNumber - 1, size, sort);
    }

    public static Pageable buildPageable(Integer pageNumber, Integer size, List<Sort.Order> orders) {
        var orderById = orders.stream().filter(order -> StringUtils.equalsIgnoreCase(order.getProperty(), COLUMN_ID)).findFirst();
        if (orderById.isEmpty()) {
            orders.add(Sort.Order.asc(COLUMN_ID));
        }
        var sort = Sort.by(orders);
        return PageRequest.of(pageNumber - 1, size, sort);
    }

    public static Pageable buildSimplePageable(Integer pageNumber, Integer size, String sortColumn, String sortDirection) {
        var sort = Sort.by(new Sort.Order(Sort.Direction.fromString(sortDirection), sortColumn));
        return PageRequest.of(pageNumber - 1, size, sort);
    }

    public static Pageable buildSimplePageable(Integer pageNumber, Integer size, List<Sort.Order> orders) {
        var sort = Sort.by(orders);
        return PageRequest.of(pageNumber - 1, size, sort);
    }


    public static Pageable buildSimplePageable(Integer pageNumber, Integer size) {
        return PageRequest.of(pageNumber - 1, size);
    }


//    public static <T> CursorPageResponse<T> buildBlankCursorPageResponse(Page<?> page) {
//        return new CursorPageResponse<>(new ArrayList<>(), null, null, page);
//    }
//
//    public static String getDecodedCursor(String cursorValue) {
//        if (cursorValue == null || cursorValue.isEmpty()) {
//            throw new IllegalArgumentException("Cursor value is not valid!");
//        }
//        var decodedBytes = Base64.getDecoder().decode(cursorValue);
//        var decodedValue = new String(decodedBytes);
//
//        return StringUtils.substringBetween(decodedValue, "###");
//    }
//
//    public static String getEncodedCursor(String field) {
//        Objects.requireNonNull(field);
//
//        var structuredValue = "###" + field + "### - " + LocalDateTime.now();
//        return Base64.getEncoder().encodeToString(structuredValue.getBytes());
//    }
}
