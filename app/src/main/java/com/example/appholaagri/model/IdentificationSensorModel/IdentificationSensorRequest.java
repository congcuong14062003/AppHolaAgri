package com.example.appholaagri.model.IdentificationSensorModel;

import java.util.ArrayList;
import java.util.List;

public class IdentificationSensorRequest {
    private int idCategoriesAsset;
    private int idPlantation;
    private int isConfirmed;
    private Monitoring monitoring;
    private String qrCode;

    public IdentificationSensorRequest() {

    }

    public IdentificationSensorRequest(int idCategoriesAsset, int idPlantation, int isConfirmed, Monitoring monitoring, String qrCode) {
        this.idCategoriesAsset = idCategoriesAsset;
        this.idPlantation = idPlantation;
        this.isConfirmed = isConfirmed;
        this.monitoring = monitoring;
        this.qrCode = qrCode;
    }

    // Getters and Setters
    public int getIdCategoriesAsset() {
        return idCategoriesAsset;
    }

    public void setIdCategoriesAsset(int idCategoriesAsset) {
        this.idCategoriesAsset = idCategoriesAsset;
    }

    public int getIdPlantation() {
        return idPlantation;
    }

    public void setIdPlantation(int idPlantation) {
        this.idPlantation = idPlantation;
    }

    public int getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(int isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public Monitoring getMonitoring() {
        return monitoring;
    }

    public void setMonitoring(Monitoring monitoring) {
        this.monitoring = monitoring;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    // Nested class for Monitoring
    public static class Monitoring {
        private int columnIn;
        private int idCultivationArea;
        private int idMonitoring;
        private List<MonitoringDetail> monitoringDetail;
        private int rowIn;

        public Monitoring() {
            this.monitoringDetail = new ArrayList<>();
        }

        // Getters and Setters
        public int getColumnIn() {
            return columnIn;
        }

        public void setColumnIn(int columnIn) {
            this.columnIn = columnIn;
        }

        public int getIdCultivationArea() {
            return idCultivationArea;
        }

        public void setIdCultivationArea(int idCultivationArea) {
            this.idCultivationArea = idCultivationArea;
        }

        public int getIdMonitoring() {
            return idMonitoring;
        }

        public void setIdMonitoring(int idMonitoring) {
            this.idMonitoring = idMonitoring;
        }

        public List<MonitoringDetail> getMonitoringDetail() {
            return monitoringDetail;
        }

        public void setMonitoringDetail(List<MonitoringDetail> monitoringDetail) {
            this.monitoringDetail = monitoringDetail;
        }

        public int getRowIn() {
            return rowIn;
        }

        public void setRowIn(int rowIn) {
            this.rowIn = rowIn;
        }
    }

    // Nested class for MonitoringDetail
    public static class MonitoringDetail {
        private int columnFrom;
        private int columnTo;
        private int idCultivationArea;
        private int rowFrom;
        private int rowTo;

        public MonitoringDetail() {

        }

        public MonitoringDetail(int columnFrom, int columnTo, int idCultivationArea, int rowFrom, int rowTo) {
            this.columnFrom = columnFrom;
            this.columnTo = columnTo;
            this.idCultivationArea = idCultivationArea;
            this.rowFrom = rowFrom;
            this.rowTo = rowTo;
        }

        // Getters and Setters
        public int getColumnFrom() {
            return columnFrom;
        }

        public void setColumnFrom(int columnFrom) {
            this.columnFrom = columnFrom;
        }

        public int getColumnTo() {
            return columnTo;
        }

        public void setColumnTo(int columnTo) {
            this.columnTo = columnTo;
        }

        public int getIdCultivationArea() {
            return idCultivationArea;
        }

        public void setIdCultivationArea(int idCultivationArea) {
            this.idCultivationArea = idCultivationArea;
        }

        public int getRowFrom() {
            return rowFrom;
        }

        public void setRowFrom(int rowFrom) {
            this.rowFrom = rowFrom;
        }

        public int getRowTo() {
            return rowTo;
        }

        public void setRowTo(int rowTo) {
            this.rowTo = rowTo;
        }
    }
}
