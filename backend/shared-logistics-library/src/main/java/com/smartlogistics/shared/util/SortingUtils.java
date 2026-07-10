package com.smartlogistics.shared.util;

import org.springframework.data.domain.Sort;
import java.util.ArrayList;
import java.util.List;

public class SortingUtils {
    public static Sort createSort(String[] sort) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sort != null) {
            for (String sortOrder : sort) {
                if (sortOrder.contains(",")) {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.ASC, sortOrder));
                }
            }
        }
        return Sort.by(orders);
    }

    private static Sort.Direction getSortDirection(String direction) {
        if (direction == null) return Sort.Direction.ASC;
        if (direction.equalsIgnoreCase("desc")) return Sort.Direction.DESC;
        return Sort.Direction.ASC;
    }
}
