package com.example.appholaagri.model.SalaryTableDetailModel;

import java.util.List;

public class SalaryTableDetailData {
    private Long userWorkSummaryId;
    private Employee employee;
    private Department department;
    private Team team;
    private Double standardWorkEfficient;
    private Double totalWorkEfficient;
    private Double remainDayOff;
    private Double normalWorkEfficient;
    private Double extraWorkEfficient;
    private List<AttendanceSummaryRes> attendanceSummaryRes;

    public SalaryTableDetailData(Long userWorkSummaryId, Employee employee, Department department, Team team,
                                 Double standardWorkEfficient, Double totalWorkEfficient, Double remainDayOff,
                                 Double normalWorkEfficient, Double extraWorkEfficient, List<AttendanceSummaryRes> attendanceSummaryRes) {
        this.userWorkSummaryId = userWorkSummaryId;
        this.employee = employee;
        this.department = department;
        this.team = team;
        this.standardWorkEfficient = standardWorkEfficient;
        this.totalWorkEfficient = totalWorkEfficient;
        this.remainDayOff = remainDayOff;
        this.normalWorkEfficient = normalWorkEfficient;
        this.extraWorkEfficient = extraWorkEfficient;
        this.attendanceSummaryRes = attendanceSummaryRes;
    }

    // Getters and Setters
    public Long getUserWorkSummaryId() {
        return userWorkSummaryId;
    }

    public void setUserWorkSummaryId(Long userWorkSummaryId) {
        this.userWorkSummaryId = userWorkSummaryId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Double getStandardWorkEfficient() {
        return standardWorkEfficient;
    }

    public void setStandardWorkEfficient(Double standardWorkEfficient) {
        this.standardWorkEfficient = standardWorkEfficient;
    }

    public Double getTotalWorkEfficient() {
        return totalWorkEfficient;
    }

    public void setTotalWorkEfficient(Double totalWorkEfficient) {
        this.totalWorkEfficient = totalWorkEfficient;
    }

    public Double getRemainDayOff() {
        return remainDayOff;
    }

    public void setRemainDayOff(Double remainDayOff) {
        this.remainDayOff = remainDayOff;
    }

    public Double getNormalWorkEfficient() {
        return normalWorkEfficient;
    }

    public void setNormalWorkEfficient(Double normalWorkEfficient) {
        this.normalWorkEfficient = normalWorkEfficient;
    }

    public Double getExtraWorkEfficient() {
        return extraWorkEfficient;
    }

    public void setExtraWorkEfficient(Double extraWorkEfficient) {
        this.extraWorkEfficient = extraWorkEfficient;
    }

    public List<AttendanceSummaryRes> getAttendanceSummaryRes() {
        return attendanceSummaryRes;
    }

    public void setAttendanceSummaryRes(List<AttendanceSummaryRes> attendanceSummaryRes) {
        this.attendanceSummaryRes = attendanceSummaryRes;
    }
}

class Employee {
    // Define fields and methods for Employee class as needed
}

class Department {
    private Long id;
    private String code;
    private String name;
    private Integer status;
    private Integer index;

    public Department(Long id, String code, String name, Integer status, Integer index) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.status = status;
        this.index = index;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}

class Team {
    private Long id;
    private String code;
    private String name;
    private Integer status;
    private Integer index;

    public Team(Long id, String code, String name, Integer status, Integer index) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.status = status;
        this.index = index;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}




