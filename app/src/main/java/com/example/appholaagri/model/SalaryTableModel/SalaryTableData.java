package com.example.appholaagri.model.SalaryTableModel;

import java.util.List;

public class SalaryTableData {
    private List<Item> data; // Sửa kiểu từ DataSalary thành Item
    private int numOfRecords;
    private int totalRecord;

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
        this.data = data;
    }

    public int getNumOfRecords() {
        return numOfRecords;
    }

    public void setNumOfRecords(int numOfRecords) {
        this.numOfRecords = numOfRecords;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public static class Item {
        private WorkSummaryMonthly workSummaryMonthly;
        private List<Company> company;
        private List<Division> division;
        private int month;
        private int year;
        private String displayDate;
        private Status status;
        private Time time;
        private int workSummaryMonthlyId;

        public WorkSummaryMonthly getWorkSummaryMonthly() {
            return workSummaryMonthly;
        }

        public void setWorkSummaryMonthly(WorkSummaryMonthly workSummaryMonthly) {
            this.workSummaryMonthly = workSummaryMonthly;
        }

        public List<Company> getCompany() {
            return company;
        }

        public void setCompany(List<Company> company) {
            this.company = company;
        }

        public List<Division> getDivision() {
            return division;
        }

        public void setDivision(List<Division> division) {
            this.division = division;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public String getDisplayDate() {
            return displayDate;
        }

        public void setDisplayDate(String displayDate) {
            this.displayDate = displayDate;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public Time getTime() {
            return time;
        }

        public void setTime(Time time) {
            this.time = time;
        }

        public int getWorkSummaryMonthlyId() {
            return workSummaryMonthlyId;
        }

        public void setWorkSummaryMonthlyId(int workSummaryMonthlyId) {
            this.workSummaryMonthlyId = workSummaryMonthlyId;
        }
    }

    public static class WorkSummaryMonthly {
        private int id;
        private String code;
        private String name;
        private int status;
        private int index;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
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

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    public static class Company {
        private int id;
        private String code;
        private String name;
        private int status;
        private int index;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
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

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    public static class Division {
        private int id;
        private String code;
        private String name;
        private int status;
        private int index;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
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

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    public static class Status {
        private int id;
        private String code;
        private String name;
        private int status;
        private int index;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
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

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    public static class Time {
        private int createdBy;
        private String createdByName;
        private String createdDate;
        private int modifiedBy;
        private String modifiedByName;
        private String modifiedDate;

        public int getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(int createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedByName() {
            return createdByName;
        }

        public void setCreatedByName(String createdByName) {
            this.createdByName = createdByName;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public int getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(int modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public String getModifiedByName() {
            return modifiedByName;
        }

        public void setModifiedByName(String modifiedByName) {
            this.modifiedByName = modifiedByName;
        }

        public String getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }
    }
}
