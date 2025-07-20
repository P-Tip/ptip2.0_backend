package com.ptip.program.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class PageResponseDto<T> {
    private List<T> items;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int pageSize;
    private boolean isLast;
}
