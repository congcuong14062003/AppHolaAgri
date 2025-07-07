package com.example.appholaagri.model.WorkSummaryDetailModel;

import java.util.List;

public class AttendanceSummaryRes {
        private String dayOfWeek;
        private String attendanceDate;
        private String displayDate;
        private Double totalCoEfficient;
        private List<ShiftSummaryRes> shiftSummaryRes;
        private Integer nattendanceDate;

        public AttendanceSummaryRes(String dayOfWeek, String attendanceDate, String displayDate, Double totalCoEfficient,
                                    List<ShiftSummaryRes> shiftSummaryRes, Integer nattendanceDate) {
            this.dayOfWeek = dayOfWeek;
            this.attendanceDate = attendanceDate;
            this.displayDate = displayDate;
            this.totalCoEfficient = totalCoEfficient;
            this.shiftSummaryRes = shiftSummaryRes;
            this.nattendanceDate = nattendanceDate;
        }

        // Getters and Setters
        public String getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public String getAttendanceDate() {
            return attendanceDate;
        }

        public void setAttendanceDate(String attendanceDate) {
            this.attendanceDate = attendanceDate;
        }

        public String getDisplayDate() {
            return displayDate;
        }

        public void setDisplayDate(String displayDate) {
            this.displayDate = displayDate;
        }

        public Double getTotalCoEfficient() {
            return totalCoEfficient;
        }

        public void setTotalCoEfficient(Double totalCoEfficient) {
            this.totalCoEfficient = totalCoEfficient;
        }

        public List<ShiftSummaryRes> getShiftSummaryRes() {
            return shiftSummaryRes;
        }

        public void setShiftSummaryRes(List<ShiftSummaryRes> shiftSummaryRes) {
            this.shiftSummaryRes = shiftSummaryRes;
        }

        public Integer getNattendanceDate() {
            return nattendanceDate;
        }

        public void setNattendanceDate(Integer nattendanceDate) {
            this.nattendanceDate = nattendanceDate;
        }
}
