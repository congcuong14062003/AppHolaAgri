package com.example.appholaagri.model.PlanAppInitFormModel;

import java.util.List;

public class PlanAppInitFormResponse {
    private List<CropVariety> cropVarietiesList;
    private List<Plantation> plantationList;

    public List<CropVariety> getCropVarietiesList() {
        return cropVarietiesList;
    }

    public void setCropVarietiesList(List<CropVariety> cropVarietiesList) {
        this.cropVarietiesList = cropVarietiesList;
    }

    public List<Plantation> getPlantationList() {
        return plantationList;
    }

    public void setPlantationList(List<Plantation> plantationList) {
        this.plantationList = plantationList;
    }
    public static class CropVariety {
        private String name;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class Plantation {
        private List<Area> area;
        private String name;
        private int index;
        private int id;

        public List<Area> getArea() {
            return area;
        }

        public void setArea(List<Area> area) {
            this.area = area;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class Area {
        private int totalRow;
        private String name;
        private int totalColumn;
        private int index;
        private int id;

        public int getTotalRow() {
            return totalRow;
        }

        public void setTotalRow(int totalRow) {
            this.totalRow = totalRow;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTotalColumn() {
            return totalColumn;
        }

        public void setTotalColumn(int totalColumn) {
            this.totalColumn = totalColumn;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}



