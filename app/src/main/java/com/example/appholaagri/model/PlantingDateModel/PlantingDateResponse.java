package com.example.appholaagri.model.PlantingDateModel;

import java.util.List;

public class PlantingDateResponse {
    private List<DateModel> dateList;

    public List<DateModel> getDateList() {
        return dateList;
    }

    public void setDateList(List<DateModel> dateList) {
        this.dateList = dateList;
    }
    public static class DateModel {
        private int id;

        private String name;

        private String name1;

        private String name2;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName1() {
            return name1;
        }

        public void setName1(String name1) {
            this.name1 = name1;
        }

        public String getName2() {
            return name2;
        }

        public void setName2(String name2) {
            this.name2 = name2;
        }
    }

}
