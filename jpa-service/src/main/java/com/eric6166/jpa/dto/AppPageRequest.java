package com.eric6166.jpa.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppPageRequest {

    Integer pageNumber;

    Integer pageSize;

    Set<String> sortColumn;

    String sortDirection;


}