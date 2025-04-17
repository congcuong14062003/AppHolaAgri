package com.example.appholaagri.model.ListWorkShiftModel;

import java.util.List;

public class ListWorkShiftRequest {
    private String keySearch;
    private Integer day;
    private Integer month;
    private int page;
    private int size;
    private List<Integer> status;
    private int type;
    private Integer year;

    public ListWorkShiftRequest(String keySearch, Integer day, Integer month, int page, int size, List<Integer> status, int type, Integer year) {
        this.keySearch = keySearch;
        this.day = day;
        this.month = month;
        this.page = page;
        this.size = size;
        this.status = status;
        this.type = type;
        this.year = year;
    }

    public String getKeySearch() {
        return keySearch;
    }

    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Integer> getStatus() {
        return status;
    }

    public void setStatus(List<Integer> status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
