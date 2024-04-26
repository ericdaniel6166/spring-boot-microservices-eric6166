package com.eric6166.jpa.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppPageRequest {

    Integer pageNumber;

    Integer pageSize;

    String sortColumn;

    String sortDirection;


}