package com.example.appholaagri.model.SalaryTableDetailModel;

public class SalaryTableDetailRequest {
    private String keySearch;
    private int page;
    private int size;
    private WorkSummaryMonthly workSummaryMonthly;
    public SalaryTableDetailRequest(){

    }
    // Constructor
    public SalaryTableDetailRequest(String keySearch, int page, int size, WorkSummaryMonthly workSummaryMonthly) {
        this.keySearch = keySearch;
        this.page = page;
        this.size = size;
        this.workSummaryMonthly = workSummaryMonthly;
    }

    // Getters and Setters
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

    public WorkSummaryMonthly getWorkSummaryMonthly() {
        return workSummaryMonthly;
    }

    public void setWorkSummaryMonthly(WorkSummaryMonthly workSummaryMonthly) {
        this.workSummaryMonthly = workSummaryMonthly;
    }

    // Inner class to represent WorkSummaryMonthly
    public static class WorkSummaryMonthly {
        private String code;
        private int id;
        private String name;
        private int status;

        // Constructor
        public WorkSummaryMonthly(String code, int id, String name, int status) {
            this.code = code;
            this.id = id;
            this.name = name;
            this.status = status;
        }

        // Getters and Setters
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}

