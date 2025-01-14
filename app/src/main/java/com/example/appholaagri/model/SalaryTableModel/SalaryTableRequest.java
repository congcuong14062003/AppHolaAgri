package com.example.appholaagri.model.SalaryTableModel;

import java.util.List;

public class SalaryTableRequest {
    private List<Integer> companyId;
    private List<Integer> divisionId;
    private String keySearch;
    private int month;
    private int page;
    private int size;
    private List<Integer> status;
    private int year;

    // Constructor mặc định
    public SalaryTableRequest() {
    }

    // Constructor đầy đủ
    public SalaryTableRequest(List<Integer> companyId, List<Integer> divisionId, String keySearch, int month,
                       int page, int size, List<Integer> status, int year) {
        this.companyId = companyId;
        this.divisionId = divisionId;
        this.keySearch = keySearch;
        this.month = month;
        this.page = page;
        this.size = size;
        this.status = status;
        this.year = year;
    }

    // Getter và Setter
    public List<Integer> getCompanyId() {
        return companyId;
    }

    public void setCompanyId(List<Integer> companyId) {
        this.companyId = companyId;
    }

    public List<Integer> getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(List<Integer> divisionId) {
        this.divisionId = divisionId;
    }

    public String getKeySearch() {
        return keySearch;
    }

    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "FilterModel{" +
                "companyId=" + companyId +
                ", divisionId=" + divisionId +
                ", keySearch='" + keySearch + '\'' +
                ", month=" + month +
                ", page=" + page +
                ", size=" + size +
                ", status=" + status +
                ", year=" + year +
                '}';
    }
}
