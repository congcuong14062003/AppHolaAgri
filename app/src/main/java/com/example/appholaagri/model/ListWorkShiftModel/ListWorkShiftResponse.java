package com.example.appholaagri.model.ListWorkShiftModel;

import java.util.List;

public class ListWorkShiftResponse {
    private WorkShiftData data;
    public WorkShiftData getData() {
        return data;
    }
    public void setData(WorkShiftData data) {
        this.data = data;
    }
    public static class WorkShiftData {
            private Employee employee;
            private Team team;
            private List<WorkShiftDetail> workShiftListDetail;

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
        }
        public static class Employee {
            private int id;
            private String code;
            private String name;
            private int status;
            private int index;
            private String url;

            // Getters and Setters...

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

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
        public static class Team {
            private Integer id;
            private String code;
            private String name;
            private Integer status;
            private Integer index;

            // Getters and Setters...

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

        public static class WorkShiftDetail {
            private String dayOfWeek;
            private String atDate;
            private String displayDate;
            private List<ShiftDetail> shiftDetail;
            private long natDate;

            // Getters and Setters...

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

            public long getNatDate() {
                return natDate;
            }

            public void setNatDate(long natDate) {
                this.natDate = natDate;
            }
        }
        public static class ShiftDetail {
            private int id;
            private String code;
            private String name;
            private int status;
            private String startTime;
            private String endTime;
            private String startBreakTime;
            private String endBreakTime;
            private String displayName;
            private int nstartTime;

            // Getters and Setters...

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

            public int getNstartTime() {
                return nstartTime;
            }

            public void setNstartTime(int nstartTime) {
                this.nstartTime = nstartTime;
            }
        }
}
