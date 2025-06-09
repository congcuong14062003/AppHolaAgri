package com.example.appholaagri.model.RequestDetailModel;

import java.io.Serializable;
import java.util.List;

public class ListDayReq implements Serializable {
        private List<BreakTime> breakTimes;
        private String day;
        private String endTime;
        private String startTime;
        public ListDayReq() {

        }
        public ListDayReq(List<BreakTime> breakTimes, String day, String startTime, String endTime) {
            this.breakTimes = breakTimes;
            this.day = day;
            this.startTime = startTime;
            this.endTime = endTime;
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
}
