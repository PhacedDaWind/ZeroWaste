package com.example.zerowaste_api.common;


import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@MappedSuperclass
public class BaseViewOption {
    @Min(value = 1, message = "Cannot be less than {value}.")
    private int page = 1;

    @Min(value = 0, message = "Cannot be less than {value}.")
    private int pageSize = 10;

    private String searchQuery;

    protected HashMap<String, String> convertMap = new HashMap<>();

    public List<String> getConvertedSort(List<String> sort) {
        final List<String> convertedSort = new ArrayList<>();

        if (sort != null) {
            for (String list : sort) {
                CollectionUtils.addIgnoreNull(convertedSort, convertMap.get(list));
            }
        }
        return convertedSort;
}
}
