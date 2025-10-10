package com.example.zerowaste_api.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.data.domain.Page;

import java.util.List;

@JsonPropertyOrder({"pageSize", "pageNumber", "numberOfElements", "totalElements", "totalPages",
        "first", "last", "hasNext", "hasPrevious", "hasContent", "content"})
public class PageWrapperVO<T> {
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;
    private final List<?> content;
    private final boolean first;
    private final boolean last;
    private final boolean hasPrevious;
    private final boolean hasNext;
    private final boolean hasContent;

    private T extraContent;

    public PageWrapperVO(@SuppressWarnings("rawtypes") Page pageDomain, List<?> content) {
        super();
        this.pageSize = pageDomain.getSize();
        this.pageNumber = pageDomain.getNumber() + 1;
        this.totalElements = pageDomain.getTotalElements();
        this.totalPages = pageDomain.getTotalPages();
        this.content = content;
        this.first = pageDomain.isFirst();
        this.last = pageDomain.isLast();
        this.hasPrevious = pageDomain.hasPrevious();
        this.hasNext = pageDomain.hasNext();
        this.hasContent = pageDomain.hasContent();
    }

    public PageWrapperVO(@SuppressWarnings("rawtypes") Page pageDomain, List<?> content, T extraContent) {
        super();
        this.pageSize = pageDomain.getSize();
        this.pageNumber = pageDomain.getNumber() + 1;
        this.totalElements = pageDomain.getTotalElements();
        this.totalPages = pageDomain.getTotalPages();
        this.content = content;
        this.first = pageDomain.isFirst();
        this.last = pageDomain.isLast();
        this.hasPrevious = pageDomain.hasPrevious();
        this.hasNext = pageDomain.hasNext();
        this.hasContent = pageDomain.hasContent();
        this.extraContent = extraContent;
    }
    // Add a JsonCreator annotated constructor for Jackson deserialization.
    @JsonCreator
    public PageWrapperVO(
            @JsonProperty("pageSize") int pageSize,
            @JsonProperty("pageNumber") int pageNumber,
            @JsonProperty("totalElements") long totalElements,
            @JsonProperty("totalPages") int totalPages,
            @JsonProperty("content") List<?> content,
            @JsonProperty("first") boolean first,
            @JsonProperty("last") boolean last,
            @JsonProperty("hasPrevious") boolean hasPrevious,
            @JsonProperty("hasNext") boolean hasNext,
            @JsonProperty("hasContent") boolean hasContent,
            @JsonProperty("extraContent") T extraContent
    ) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.content = content;
        this.first = first;
        this.last = last;
        this.hasPrevious = hasPrevious;
        this.hasNext = hasNext;
        this.hasContent = hasContent;
        this.extraContent = extraContent;
    }

    @JsonGetter("pageSize")
    public int getSize() {
        return pageSize;
    }

    @JsonGetter("pageNumber")
    public int getNumber() {
        return pageNumber;
    }

    public int getNumberOfElements() {
        return content.size();
    }

    public long getTotalElements() {
        return totalElements;
    }


    public int getTotalPages() {
        return totalPages;
    }

    public List<?> getContent() {
        return content;
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isLast() {
        return last;
    }

    @JsonGetter("hasNext")
    public boolean hasNext() {
        return hasNext;
    }

    @JsonGetter("hasPrevious")
    public boolean hasPrevious() {
        return hasPrevious;
    }

    @JsonGetter("hasContent")
    public boolean hasContent() {
        return hasContent;
    }

    public T getExtraContent() {
        return extraContent;
}

}
