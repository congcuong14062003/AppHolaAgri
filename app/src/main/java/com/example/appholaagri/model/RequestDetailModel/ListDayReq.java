package com.example.appholaagri.model.RequestDetailModel;

import java.io.Serializable;
import java.util.List;

public class ListDayReq implements Serializable {
        private List<BreakTime> breakTimes;
        private String day;
        private String endTime;
        private String startTime;
        private String purpose;
        private String result;
        public ListDayReq() {

        }
        public ListDayReq(List<BreakTime> breakTimes, String day, String startTime, String endTime) {
            this.breakTimes = breakTimes;
            this.day = day;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    public ListDayReq(List<BreakTime> breakTimes, String day, String startTime, String endTime, String purpose, String result) {
        this.breakTimes = breakTimes;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.result = result;
    }

    public List<BreakTime> getBreakTimes() {
        return breakTimes;
    }

    public void setBreakTimes(List<BreakTime> breakTimes) {
        this.breakTimes = breakTimes;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
