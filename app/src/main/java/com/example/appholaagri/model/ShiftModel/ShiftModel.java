package com.example.appholaagri.model.ShiftModel;

import java.util.List;

public class ShiftModel {
    private List<Shift> data;
    public List<Shift> getData() {
        return data;
    }
    public void setData(List<Shift> data) {
        this.data = data;
    }
    public static class Shift {
        private int workShiftId;

        private String workShiftCode;

        private String workShiftName;

        private String dayOfWeek;

        private String dateString;

        private String startTime;

        private String endTime;

        private String displayName;

        private int shiftType;

        // Getter and Setter Methods
        public int getWorkShiftId() {
            return workShiftId;
        }

        public void setWorkShiftId(int workShiftId) {
            this.workShiftId = workShiftId;
        }

        public String getWorkShiftCode() {
            return workShiftCode;
        }

        public void setWorkShiftCode(String workShiftCode) {
            this.workShiftCode = workShiftCode;
        }

        public String getWorkShiftName() {
            return workShiftName;
        }

        public void setWorkShiftName(String workShiftName) {
            this.workShiftName = workShiftName;
        }

        public String getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public String getDateString() {
            return dateString;
        }

        public void setDateString(String dateString) {
            this.dateString = dateString;
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

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public int getShiftType() {
            return shiftType;
        }

        public void setShiftType(int shiftType) {
            this.shiftType = shiftType;
        }
    }
}
