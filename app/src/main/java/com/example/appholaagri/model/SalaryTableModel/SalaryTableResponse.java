package com.example.appholaagri.model.SalaryTableModel;

import java.util.List;

public class SalaryTableResponse {
    private List<Data> data;
    private int totalRecord;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public static class Data {
        private int id;
        private String name;
        private String enName;
        private int monthSalary;
        private int yearSalary;
        private String confirmedBy;
        private String confirmedDate;
        private String createdBy;
        private String createdDate;
        private String modifiedBy;
        private String modifiedDate;
        private Status status;
        private TypeSalary typeSalary;
        private FormConfig formConfig;
        private List<MonthlyWorkSummary> monthlyWorkSummary;
        private List<Contract> contract;

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

        public String getEnName() {
            return enName;
        }

        public void setEnName(String enName) {
            this.enName = enName;
        }

        public int getMonthSalary() {
            return monthSalary;
        }

        public void setMonthSalary(int monthSalary) {
            this.monthSalary = monthSalary;
        }

        public int getYearSalary() {
            return yearSalary;
        }

        public void setYearSalary(int yearSalary) {
            this.yearSalary = yearSalary;
        }

        public String getConfirmedBy() {
            return confirmedBy;
        }

        public void setConfirmedBy(String confirmedBy) {
            this.confirmedBy = confirmedBy;
        }

        public String getConfirmedDate() {
            return confirmedDate;
        }

        public void setConfirmedDate(String confirmedDate) {
            this.confirmedDate = confirmedDate;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public String getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public TypeSalary getTypeSalary() {
            return typeSalary;
        }

        public void setTypeSalary(TypeSalary typeSalary) {
            this.typeSalary = typeSalary;
        }

        public FormConfig getFormConfig() {
            return formConfig;
        }

        public void setFormConfig(FormConfig formConfig) {
            this.formConfig = formConfig;
        }

        public List<MonthlyWorkSummary> getMonthlyWorkSummary() {
            return monthlyWorkSummary;
        }

        public void setMonthlyWorkSummary(List<MonthlyWorkSummary> monthlyWorkSummary) {
            this.monthlyWorkSummary = monthlyWorkSummary;
        }

        public List<Contract> getContract() {
            return contract;
        }

        public void setContract(List<Contract> contract) {
            this.contract = contract;
        }
    }

    public static class Status {
        private int id;
        private String name;

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
    }

    public static class TypeSalary {
        private int id;
        private String name;

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
    }

    public static class FormConfig {
        private int id;
        private String name;

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
    }

    public static class MonthlyWorkSummary {
        private int id;
        private String name;

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
    }

    public static class Contract {
        private int id;
        private String name;

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
    }
}