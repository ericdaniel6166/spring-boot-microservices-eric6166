package com.eric6166.jpa.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> {
    List<T> content;
    AppPageable pageable;

    public PageResponse(List<T> content, Page<?> page) {
        this.content = content;
        this.pageable = new AppPageable(page);
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AppPageable {
        int pageNumber;
        int pageSize;
        int numberOfElements;
        boolean first;
        boolean last;
        boolean hasNext;
        boolean hasPrevious;
        boolean hasContent;
        long totalElements;
        int totalPages;

        public AppPageable(Page<?> page) {
            this.pageNumber = page.getNumber() + 1;
            this.pageSize = page.getSize();
            this.numberOfElements = page.getNumberOfElements();
            this.first = page.isFirst();
            this.last = page.isLast();
            this.hasNext = page.hasNext();
            this.hasPrevious = page.hasPrevious();
            this.hasContent = page.hasContent();
            this.totalElements = page.getTotalElements();
            this.totalPages = page.getTotalPages();
        }
    }

}
