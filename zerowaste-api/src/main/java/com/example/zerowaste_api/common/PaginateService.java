package com.example.zerowaste_api.common;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


public abstract class PaginateService {

    protected abstract Sort getDefaultSort();

    /**
     * Generate sort object based on the sort query input.
     * Adding a negative (-) to indicate descending order, else ascending
     *
     * <pre>
     *     name  => order by name asc
     *     -name => order by name desc
     * </pre>
     *
     * @param sortQuery list of sort query
     * @return Sort
     */
    public Sort getSortQuery(List<String> sortQuery) {
        if (CollectionUtils.isEmpty(sortQuery)) return getDefaultSort();

        List<Sort.Order> orderList = new ArrayList<>();
        for (String sortStr : sortQuery) {
            Sort.Order order = (sortStr.startsWith("-"))
                    ? Sort.Order.desc(sortStr.substring(1))
                    : Sort.Order.asc(sortStr);
            orderList.add(order);
        }
        return Sort.by(orderList);
    }

    public Sort getSortQueryV2(List<String> sortQuery) {
        if (CollectionUtils.isEmpty(sortQuery)) return getDefaultSort();
        Sort sort = Sort.by(Collections.emptyList());
        for (String sortStr : sortQuery) {
            if (sortStr.contains(",")) {
                String[] strings = sortStr.split(",");
                for (String string : strings) {
                    sort = addSort(sort, string);
                }
            } else {
                sort = addSort(sort, sortStr);
            }
        }
        return sort;
    }

    public Sort addSort(Sort sort, String propertiesName) {

        Sort.Order order = (propertiesName.startsWith("-"))
                ? Sort.Order.desc(propertiesName.substring(1))
                : Sort.Order.asc(propertiesName);
        if (order.getProperty().matches("^\\(.*\\)$")) {
            sort = sort.and(JpaSort.unsafe(order.getDirection(), order.getProperty()));
        } else {
            sort = sort.and(Sort.by(order.getDirection(), order.getProperty()));
        }
        return sort;
    }

    /**
     * Generate pageable
     *
     * @param page     page number
     * @param pageSize page size
     * @param sort     Sort
     * @return
     */
    public Pageable getPageableQuery(int page, int pageSize, Sort sort) {
        return (pageSize <= 0)
                ? new SortedUnpaged(sort)
                : PageRequest.of(page - 1, pageSize, sort);
    }

    public String getLikeSearch(String search) {
        if (search != null) {
            return "%" + search + "%";
        } else {
            return "%%";
        }
    }

    public String getLikeSearchOrNull(String search) {
        if (search != null) {
            return "%" + search + "%";
        } else {
            return null;
        }
    }

    public Long getNumberOrNull(Long value) {
        return value != null && value > 0 ? value : null;
    }

    public String getLikeJsonSearchOrNull(String search) {
        if (search != null) {
            return "%\"" + search + "\"%";
        } else {
            return null;
        }
    }

    public <T> Page<T> convertListToPage(List<T> list, Pageable pageable) {
        if (Objects.isNull(list)) {
            throw new IllegalArgumentException("To create a Page, the list mustn't be null!");
        }

        int startOfPage = pageable.getPageNumber() * pageable.getPageSize();
        if (startOfPage > list.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        int endOfPage = Math.min(startOfPage + pageable.getPageSize(), list.size());
        return new PageImpl<>(list.subList(startOfPage, endOfPage), pageable, list.size());
    }


    public static Sort getSortOrder(String sortParam) {
        Sort sort = Sort.by("id"); // Default sort
        if (sortParam != null && !sortParam.isEmpty()) {
            try {
                String[] sortParts = sortParam.split(",");
                if (sortParts.length == 2) {
                    String property = sortParts[0];
                    String direction = sortParts[1];
                    if ("desc".equalsIgnoreCase(direction)) {
                        sort = Sort.by(Sort.Direction.DESC, property);
                    } else {
                        sort = Sort.by(Sort.Direction.ASC, property);
                    }
                } else {
                    sort = Sort.by(sortParam);
                }
            } catch (Exception e) {
                sort = Sort.by("id");
            }
        }
        return sort;
}
}