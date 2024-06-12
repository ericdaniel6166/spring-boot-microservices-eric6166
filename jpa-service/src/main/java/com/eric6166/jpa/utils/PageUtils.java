package com.eric6166.jpa.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public final class PageUtils {

    public static final String COLUMN_ID = "id";

    private PageUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Pageable buildPageable(Integer pageNumber, Integer pageSize, String sortColumn, String sortDirection) {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.fromString(sortDirection), sortColumn));
        return buildPageable(pageNumber, pageSize, orders);
    }

    public static Pageable buildPageable(Integer pageNumber, Integer pageSize, List<Sort.Order> orders) {
        var orderById = orders.stream()
                .filter(order -> StringUtils.equalsIgnoreCase(order.getProperty(), COLUMN_ID))
                .findFirst();
        if (orderById.isEmpty()) {
            orders.add(Sort.Order.asc(COLUMN_ID));
        }
        return PageRequest.of(pageNumber - 1, pageSize, Sort.by(orders));
    }

    public static Pageable buildSimplePageable(Integer pageNumber, Integer pageSize, String sortColumn, String sortDirection) {
        return buildSimplePageable(pageNumber, pageSize,
                Collections.singletonList(new Sort.Order(Sort.Direction.fromString(sortDirection), sortColumn)));
    }

    public static Pageable buildSimplePageable(Integer pageNumber, Integer pageSize, List<Sort.Order> orders) {
        return PageRequest.of(pageNumber - 1, pageSize, Sort.by(orders));
    }


    public static Pageable buildSimplePageable(Integer pageNumber, Integer pageSize) {
        return PageRequest.of(pageNumber - 1, pageSize);
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
