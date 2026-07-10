package com.smartlogistics.matchingservice.dto;

import java.util.List;

public record CustomPage<T>(
        List<T> content,
        int number,
        int size,
        long totalElements,
        int totalPages
) {}
