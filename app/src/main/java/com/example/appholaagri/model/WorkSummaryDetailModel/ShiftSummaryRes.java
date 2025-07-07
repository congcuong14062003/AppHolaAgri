package com.example.appholaagri.model.WorkSummaryDetailModel;

public class ShiftSummaryRes {
        private Long shiftId;
        private String shiftName;
        private String shiftCode;
        private String startTime;
        private String endTime;
        private String checkInTime;
        private String checkOutTime;
        private String note;

        public ShiftSummaryRes(Long shiftId, String shiftName, String shiftCode, String startTime, String endTime,
                               String checkInTime, String checkOutTime, String note) {
            this.shiftId = shiftId;
            this.shiftName = shiftName;
            this.shiftCode = shiftCode;
            this.startTime = startTime;
            this.endTime = endTime;
            this.checkInTime = checkInTime;
            this.checkOutTime = checkOutTime;
            this.note = note;
        }

        // Getters and Setters
        public Long getShiftId() {
            return shiftId;
        }

        public void setShiftId(Long shiftId) {
            this.shiftId = shiftId;
        }

        public String getShiftName() {
            return shiftName;
        }

        public void setShiftName(String shiftName) {
            this.shiftName = shiftName;
        }

        public String getShiftCode() {
            return shiftCode;
        }

        public void setShiftCode(String shiftCode) {
            this.shiftCode = shiftCode;
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

        public String getCheckInTime() {
            return checkInTime;
        }

        public void setCheckInTime(String checkInTime) {
            this.checkInTime = checkInTime;
        }

        public String getCheckOutTime() {
            return checkOutTime;
        }

        public void setCheckOutTime(String checkOutTime) {
            this.checkOutTime = checkOutTime;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }
}
