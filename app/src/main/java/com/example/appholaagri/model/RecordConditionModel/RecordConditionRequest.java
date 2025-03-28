package com.example.appholaagri.model.RecordConditionModel;

import java.util.List;

public class RecordConditionRequest {
    private List<Integer> cultivationArea;
    private String endDate;
    private String keySearch;
    private int page;
    private List<Integer> plantation;
    private int size;
    private String startDate;
    private List<Integer> status;
    private List<Integer> userId;

    public RecordConditionRequest(List<Integer> cultivationArea, String endDate, String keySearch, int page,
                                  List<Integer> plantation, int size, String startDate, List<Integer> status, List<Integer> userId) {
        this.cultivationArea = cultivationArea;
        this.endDate = endDate;
        this.keySearch = keySearch;
        this.page = page;
        this.plantation = plantation;
        this.size = size;
        this.startDate = startDate;
        this.status = status;
        this.userId = userId;
    }

    public List<Integer> getCultivationArea() {
        return cultivationArea;
    }

    public void setCultivationArea(List<Integer> cultivationArea) {
        this.cultivationArea = cultivationArea;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getKeySearch() {
        return keySearch;
    }

    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Integer> getPlantation() {
        return plantation;
    }

    public void setPlantation(List<Integer> plantation) {
        this.plantation = plantation;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List<Integer> getStatus() {
        return status;
    }

    public void setStatus(List<Integer> status) {
        this.status = status;
    }

    public List<Integer> getUserId() {
        return userId;
    }

    public void setUserId(List<Integer> userId) {
        this.userId = userId;
    }
}
