package com.example.appholaagri.model.UserData;

public class ContractInfo {
    private Contract contract;
    private String workStartDate;
    private String officialWorkingDay;
    private String workEndDate;
    private Status status;
    private String urlFile;

    // Getters và setters
    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public String getWorkStartDate() {
        return workStartDate;
    }

    public void setWorkStartDate(String workStartDate) {
        this.workStartDate = workStartDate;
    }

    public String getOfficialWorkingDay() {
        return officialWorkingDay;
    }

    public void setOfficialWorkingDay(String officialWorkingDay) {
        this.officialWorkingDay = officialWorkingDay;
    }

    public String getWorkEndDate() {
        return workEndDate;
    }

    public void setWorkEndDate(String workEndDate) {
        this.workEndDate = workEndDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }
}
