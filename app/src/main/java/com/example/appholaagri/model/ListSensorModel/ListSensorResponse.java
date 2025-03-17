package com.example.appholaagri.model.ListSensorModel;

import java.util.List;

public class ListSensorResponse {
    private List<SensorData> data;
    private int totalRecord;

    // Getters and Setters
    public List<SensorData> getData() {
        return data;
    }

    public void setData(List<SensorData> data) {
        this.data = data;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    // Lớp con để ánh xạ từng sensor
    public static class SensorData {
        private int idSensor;
        private String nameSensor;
        private int idMonitoring;
        private String nameMonitoring;
        private int idPlantation;
        private String namePlantation;
        private int idCultivationArea;
        private String nameCultivationArea;
        private String area;
        private int status;
        private String statusString;

        // Getters and Setters
        public int getIdSensor() {
            return idSensor;
        }

        public void setIdSensor(int idSensor) {
            this.idSensor = idSensor;
        }

        public String getNameSensor() {
            return nameSensor;
        }

        public void setNameSensor(String nameSensor) {
            this.nameSensor = nameSensor;
        }

        public int getIdMonitoring() {
            return idMonitoring;
        }

        public void setIdMonitoring(int idMonitoring) {
            this.idMonitoring = idMonitoring;
        }

        public String getNameMonitoring() {
            return nameMonitoring;
        }

        public void setNameMonitoring(String nameMonitoring) {
            this.nameMonitoring = nameMonitoring;
        }

        public int getIdPlantation() {
            return idPlantation;
        }

        public void setIdPlantation(int idPlantation) {
            this.idPlantation = idPlantation;
        }

        public String getNamePlantation() {
            return namePlantation;
        }

        public void setNamePlantation(String namePlantation) {
            this.namePlantation = namePlantation;
        }

        public int getIdCultivationArea() {
            return idCultivationArea;
        }

        public void setIdCultivationArea(int idCultivationArea) {
            this.idCultivationArea = idCultivationArea;
        }

        public String getNameCultivationArea() {
            return nameCultivationArea;
        }

        public void setNameCultivationArea(String nameCultivationArea) {
            this.nameCultivationArea = nameCultivationArea;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getStatusString() {
            return statusString;
        }

        public void setStatusString(String statusString) {
            this.statusString = statusString;
        }
    }
}
