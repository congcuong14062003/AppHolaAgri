package com.example.appholaagri.model.TimekeepingStatisticsModel;

import java.util.List;

public class TimekeepingStatisticsData {

    private List<DayData> data;  // Danh sách dữ liệu ngày
    private int numOfRecords;     // Tổng số bản ghi

    // Getters and Setters
    public List<DayData> getData() {
        return data;
    }

    public void setData(List<DayData> data) {
        this.data = data;
    }

    public int getNumOfRecords() {
        return numOfRecords;
    }

    public void setNumOfRecords(int numOfRecords) {
        this.numOfRecords = numOfRecords;
    }

    // Lớp con để đại diện cho một ngày cụ thể
    public static class DayData {

        private String day;           // Ngày (ví dụ: "Thứ 3, 07/01/2025")
        private List<Shift> shiftRes; // Danh sách các ca làm việc trong ngày
        private int nday;             // Mã ngày (ví dụ: 20250107)

        // Getters and Setters
        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public List<Shift> getShiftRes() {
            return shiftRes;
        }

        public void setShiftRes(List<Shift> shiftRes) {
            this.shiftRes = shiftRes;
        }

        public int getNday() {
            return nday;
        }

        public void setNday(int nday) {
            this.nday = nday;
        }
    }

    // Lớp con để đại diện cho một ca làm việc cụ thể
    public static class Shift {

        private int shiftId;          // ID của ca làm việc
        private String shiftCode;     // Mã ca
        private String shiftName;     // Tên ca làm việc (ví dụ: "Ca hành chính")
        private String period;        // Thời gian ca (ví dụ: "08:00 - 17:30")
        private String checkinTime;   // Thời gian check-in
        private String checkoutTime;  // Thời gian check-out
        private int startTime;        // Giờ bắt đầu ca làm việc
        private int shiftType;        // Loại ca (ví dụ: 1 - ca hàng ngày)
        private String shiftTypeName; // Tên loại ca
        private String reason;        // Lý do (nếu có)

        // Getters and Setters
        public int getShiftId() {
            return shiftId;
        }

        public void setShiftId(int shiftId) {
            this.shiftId = shiftId;
        }

        public String getShiftCode() {
            return shiftCode;
        }

        public void setShiftCode(String shiftCode) {
            this.shiftCode = shiftCode;
        }

        public String getShiftName() {
            return shiftName;
        }

        public void setShiftName(String shiftName) {
            this.shiftName = shiftName;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getCheckinTime() {
            return checkinTime;
        }

        public void setCheckinTime(String checkinTime) {
            this.checkinTime = checkinTime;
        }

        public String getCheckoutTime() {
            return checkoutTime;
        }

        public void setCheckoutTime(String checkoutTime) {
            this.checkoutTime = checkoutTime;
        }

        public int getStartTime() {
            return startTime;
        }

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }

        public int getShiftType() {
            return shiftType;
        }

        public void setShiftType(int shiftType) {
            this.shiftType = shiftType;
        }

        public String getShiftTypeName() {
            return shiftTypeName;
        }

        public void setShiftTypeName(String shiftTypeName) {
            this.shiftTypeName = shiftTypeName;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
