package com.example.appholaagri.model.SalaryTableModel;

import java.util.List;

public class SalaryTableRequest {
    private int isApp;
    private String keySearch;
    private int page;
    private int size;
    private List<Integer> status;

    public SalaryTableRequest() {
    }
    public SalaryTableRequest(int isApp, String keySearch, int page, int size, List<Integer> status) {
        // Giá trị mặc định
        this.isApp = isApp;
        this.keySearch = keySearch;
        this.page = page;
        this.size = size;
        this.status = status;
    }

    // Getters và Setters
    public int getIsApp() {
        return isApp;
    }

    public void setIsApp(int isApp) {
        this.isApp = isApp;
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
}
