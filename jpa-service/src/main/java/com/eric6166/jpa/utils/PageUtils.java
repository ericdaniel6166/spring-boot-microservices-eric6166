package com.eric6166.jpa.utils;

import com.eric6166.base.utils.Const;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Slf4j
public final class PageUtils {

    private PageUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Pageable buildPageable(Integer pageNumber, Integer size, String sortColumn, String sortDirection) {
        Sort sort;
        if (StringUtils.equals(sortColumn, Const.ID)) {
            sort = Sort.by(new Sort.Order(Sort.Direction.fromString(sortDirection), Const.ID));
        } else {
            sort = Sort.by(new Sort.Order(Sort.Direction.fromString(sortDirection), sortColumn),
                    Sort.Order.asc(Const.ID));
        }
        return PageRequest.of(pageNumber - 1, size, sort);
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
