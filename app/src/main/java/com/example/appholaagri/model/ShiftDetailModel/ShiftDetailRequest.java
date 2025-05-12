package com.example.appholaagri.model.ShiftDetailModel;


import com.example.appholaagri.model.ListWorkShiftModel.ListWorkShiftResponse.Employee;
import com.example.appholaagri.model.ListWorkShiftModel.ListWorkShiftResponse.Team;
import com.example.appholaagri.model.ListWorkShiftModel.ListWorkShiftResponse.WorkShiftDetail;

import java.util.List;

public class ShiftDetailRequest {
    private Employee employee;
    private Team team;
    private List<WorkShiftDetail> workShiftListDetail;
    private Integer type;
    private Integer employeeId;

    // Constructor
    public ShiftDetailRequest(Employee employee, Team team, List<WorkShiftDetail> workShiftListDetail, Integer type, Integer employeeId) {
        this.employee = employee;
        this.team = team;
        this.workShiftListDetail = workShiftListDetail;
        this.type = type;
        this.employeeId = employeeId;
    }

    // Getters and Setters
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<WorkShiftDetail> getWorkShiftListDetail() {
        return workShiftListDetail;
    }

    public void setWorkShiftListDetail(List<WorkShiftDetail> workShiftListDetail) {
        this.workShiftListDetail = workShiftListDetail;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
}