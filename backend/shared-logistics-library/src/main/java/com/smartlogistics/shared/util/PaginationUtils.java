package com.smartlogistics.shared.util;

import com.smartlogistics.shared.dto.PageResponse;
import org.springframework.data.domain.Page;

public class PaginationUtils {
    public static <T> PageResponse<T> toPageResponse(Page<T> page) {
        if (page == null) return null;
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
