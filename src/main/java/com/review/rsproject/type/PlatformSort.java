package com.review.rsproject.type;

public enum PlatformSort {


    STAR_DESC("STAR_DESC"),
    STAR_ASC("STAR_ASC"),
    DATE_DESC("DATE_DESC"),
    DATE_ASC("DATE_ASC");

    private final String sort;

    PlatformSort(String sort) {
        this.sort = sort;
    }

    public String getSort() {
        return sort;
    }
}
