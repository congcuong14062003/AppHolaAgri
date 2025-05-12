package com.example.appholaagri.model.ListWorkShiftModel;

import java.io.Serializable;
import java.util.List;

public class ListWorkShiftResponse {
    private DataWrapper data;

    public DataWrapper getData() {
        return data;
    }

    public void setData(DataWrapper data) {
        this.data = data;
    }

    public static class DataWrapper implements Serializable {
        private List<WorkShiftData> data;
        private int numOfRecords;
        private int totalRecord;

        public List<WorkShiftData> getData() {
            return data;
        }

        public void setData(List<WorkShiftData> data) {
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
    }

    public static class WorkShiftData implements Serializable {
        private Employee employee;
        private Team team;
        private List<WorkShiftDetail> workShiftListDetail;
        private Integer type;
        private Integer employeeId;

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

    public static class Employee implements Serializable {
        private Integer id;
        private String code;
        private String name;
        private Integer status;
        private Integer index;
        private String url;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Team implements Serializable {
        private Integer id;
        private String code;
        private String name;
        private Integer status;
        private Integer index;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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

    public static class WorkShiftDetail implements Serializable {
        private String dayOfWeek;
        private String atDate;
        private String displayDate;
        private List<ShiftDetail> shiftDetail;
        private Long natDate;

        public String getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public String getAtDate() {
            return atDate;
        }

        public void setAtDate(String atDate) {
            this.atDate = atDate;
        }

        public String getDisplayDate() {
            return displayDate;
        }

        public void setDisplayDate(String displayDate) {
            this.displayDate = displayDate;
        }

        public List<ShiftDetail> getShiftDetail() {
            return shiftDetail;
        }

        public void setShiftDetail(List<ShiftDetail> shiftDetail) {
            this.shiftDetail = shiftDetail;
        }

        public Long getNatDate() {
            return natDate;
        }

        public void setNatDate(Long natDate) {
            this.natDate = natDate;
        }
    }

    public static class ShiftDetail implements Serializable {
        private Integer id;
        private String code;
        private String name;
        private Integer status;
        private String startTime;
        private String endTime;
        private String startBreakTime;
        private String endBreakTime;
        private String displayName;
        private Integer nstartTime;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getStartBreakTime() {
            return startBreakTime;
        }

        public void setStartBreakTime(String startBreakTime) {
            this.startBreakTime = startBreakTime;
        }

        public String getEndBreakTime() {
            return endBreakTime;
        }

        public void setEndBreakTime(String endBreakTime) {
            this.endBreakTime = endBreakTime;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public Integer getNstartTime() {
            return nstartTime;
        }

        public void setNstartTime(Integer nstartTime) {
            this.nstartTime = nstartTime;
        }
    }
}