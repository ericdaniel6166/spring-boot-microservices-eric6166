package com.eric6166.jpa.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class PageResponse<T> {
    private List<T> content;
    private AppPageable pageable;

    public PageResponse(List<T> content, Page<?> page) {
        this.content = content;
        this.pageable = new AppPageable(page);
    }

    @Getter
    @Setter
    public static class AppPageable {
        private int pageNumber;
        private int pageSize;
        private int numberOfElements;
        private boolean first;
        private boolean last;
        private boolean hasNext;
        private boolean hasPrevious;
        private boolean hasContent;
        private long totalElements;
        private int totalPages;

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
