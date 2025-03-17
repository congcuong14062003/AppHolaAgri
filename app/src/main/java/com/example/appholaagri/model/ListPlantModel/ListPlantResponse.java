package com.example.appholaagri.model.ListPlantModel;

import java.util.List;

public class ListPlantResponse {
    private List<PlanData> data;
    private int totalRecord;

    // Getters and Setters
    public List<PlanData> getData() {
        return data;
    }

    public void setData(List<PlanData> data) {
        this.data = data;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public static class PlanData {
        private int id;
        private String code;
        private int idCropVarieties;
        private String nameCropVarieties;
        private int idPlantation;
        private String namePlantation;
        private int idCultivationPeriod;
        private String nameCultivationPeriod;
        private int idCultivationArea;
        private String nameCultivationArea;
        private int columnIn;
        private int rowIn;
        private int idDevelopmentStage;
        private String nameDevelopmentStage;
        private int dayDifference;
        private int status;
        private String modifiedDate;
        private String modifiedBy;
        private String statusString;
        private String cultivationAreaFrom;

        // Constructor mặc định
        public PlanData() {
        }

        // Constructor đầy đủ
        public PlanData(int id, String code, int idCropVarieties, String nameCropVarieties,
                        int idPlantation, String namePlantation, int idCultivationPeriod,
                        String nameCultivationPeriod, int idCultivationArea, String nameCultivationArea,
                        int columnIn, int rowIn, int idDevelopmentStage, String nameDevelopmentStage,
                        int dayDifference, int status, String modifiedDate, String modifiedBy,
                        String statusString, String cultivationAreaFrom) {
            this.id = id;
            this.code = code;
            this.idCropVarieties = idCropVarieties;
            this.nameCropVarieties = nameCropVarieties;
            this.idPlantation = idPlantation;
            this.namePlantation = namePlantation;
            this.idCultivationPeriod = idCultivationPeriod;
            this.nameCultivationPeriod = nameCultivationPeriod;
            this.idCultivationArea = idCultivationArea;
            this.nameCultivationArea = nameCultivationArea;
            this.columnIn = columnIn;
            this.rowIn = rowIn;
            this.idDevelopmentStage = idDevelopmentStage;
            this.nameDevelopmentStage = nameDevelopmentStage;
            this.dayDifference = dayDifference;
            this.status = status;
            this.modifiedDate = modifiedDate;
            this.modifiedBy = modifiedBy;
            this.statusString = statusString;
            this.cultivationAreaFrom = cultivationAreaFrom;
        }

        // Getters và Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getIdCropVarieties() {
            return idCropVarieties;
        }

        public void setIdCropVarieties(int idCropVarieties) {
            this.idCropVarieties = idCropVarieties;
        }

        public String getNameCropVarieties() {
            return nameCropVarieties;
        }

        public void setNameCropVarieties(String nameCropVarieties) {
            this.nameCropVarieties = nameCropVarieties;
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

        public int getIdCultivationPeriod() {
            return idCultivationPeriod;
        }

        public void setIdCultivationPeriod(int idCultivationPeriod) {
            this.idCultivationPeriod = idCultivationPeriod;
        }

        public String getNameCultivationPeriod() {
            return nameCultivationPeriod;
        }

        public void setNameCultivationPeriod(String nameCultivationPeriod) {
            this.nameCultivationPeriod = nameCultivationPeriod;
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

        public int getColumnIn() {
            return columnIn;
        }

        public void setColumnIn(int columnIn) {
            this.columnIn = columnIn;
        }

        public int getRowIn() {
            return rowIn;
        }

        public void setRowIn(int rowIn) {
            this.rowIn = rowIn;
        }

        public int getIdDevelopmentStage() {
            return idDevelopmentStage;
        }

        public void setIdDevelopmentStage(int idDevelopmentStage) {
            this.idDevelopmentStage = idDevelopmentStage;
        }

        public String getNameDevelopmentStage() {
            return nameDevelopmentStage;
        }

        public void setNameDevelopmentStage(String nameDevelopmentStage) {
            this.nameDevelopmentStage = nameDevelopmentStage;
        }

        public int getDayDifference() {
            return dayDifference;
        }

        public void setDayDifference(int dayDifference) {
            this.dayDifference = dayDifference;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public String getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public String getStatusString() {
            return statusString;
        }

        public void setStatusString(String statusString) {
            this.statusString = statusString;
        }

        public String getCultivationAreaFrom() {
            return cultivationAreaFrom;
        }

        public void setCultivationAreaFrom(String cultivationAreaFrom) {
            this.cultivationAreaFrom = cultivationAreaFrom;
        }
    }

}
