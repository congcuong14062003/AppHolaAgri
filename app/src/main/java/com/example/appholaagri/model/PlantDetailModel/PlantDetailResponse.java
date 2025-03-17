package com.example.appholaagri.model.PlantDetailModel;

import java.util.List;

public class PlantDetailResponse {
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
    private List<CropCareWork> aomPlantCropCareWork;
    private List<NutritionalRes> aomPlantNutritionalRes;
    private List<CropYieldRes> aomPlantCropYieldRes;

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

    public List<CropCareWork> getAomPlantCropCareWork() {
        return aomPlantCropCareWork;
    }

    public void setAomPlantCropCareWork(List<CropCareWork> aomPlantCropCareWork) {
        this.aomPlantCropCareWork = aomPlantCropCareWork;
    }

    public List<NutritionalRes> getAomPlantNutritionalRes() {
        return aomPlantNutritionalRes;
    }

    public void setAomPlantNutritionalRes(List<NutritionalRes> aomPlantNutritionalRes) {
        this.aomPlantNutritionalRes = aomPlantNutritionalRes;
    }

    public List<CropYieldRes> getAomPlantCropYieldRes() {
        return aomPlantCropYieldRes;
    }

    public void setAomPlantCropYieldRes(List<CropYieldRes> aomPlantCropYieldRes) {
        this.aomPlantCropYieldRes = aomPlantCropYieldRes;
    }

    // Getters and Setters
    // Inner classes for nested objects
    public static class CropCareWork {
        private int id;
        private String realTime;
        private int idCropCareWork;
        private String nameCropCareWork;
        private int performBy;
        private String performByName;
        private int idTeam;
        private String nameTeam;
        private String description;

        // Getters and Setters

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRealTime() {
            return realTime;
        }

        public void setRealTime(String realTime) {
            this.realTime = realTime;
        }

        public int getIdCropCareWork() {
            return idCropCareWork;
        }

        public void setIdCropCareWork(int idCropCareWork) {
            this.idCropCareWork = idCropCareWork;
        }

        public String getNameCropCareWork() {
            return nameCropCareWork;
        }

        public void setNameCropCareWork(String nameCropCareWork) {
            this.nameCropCareWork = nameCropCareWork;
        }

        public int getPerformBy() {
            return performBy;
        }

        public void setPerformBy(int performBy) {
            this.performBy = performBy;
        }

        public String getPerformByName() {
            return performByName;
        }

        public void setPerformByName(String performByName) {
            this.performByName = performByName;
        }

        public int getIdTeam() {
            return idTeam;
        }

        public void setIdTeam(int idTeam) {
            this.idTeam = idTeam;
        }

        public String getNameTeam() {
            return nameTeam;
        }

        public void setNameTeam(String nameTeam) {
            this.nameTeam = nameTeam;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class NutritionalRes {
        private int id;
        private String name;
        private String nameVi;
        private int stt;
        private String realTime;
        private double optimalQuantityFrom;
        private double optimalQuantityTo;
        private String optimalString;
        private Double highLevelResults;
        private String concludeHighString;
        private String warning;
        private double lowLevelResults;
        private String concludeLowString;
        private String warningDeep;

        // Getters and Setters

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

        public String getNameVi() {
            return nameVi;
        }

        public void setNameVi(String nameVi) {
            this.nameVi = nameVi;
        }

        public int getStt() {
            return stt;
        }

        public void setStt(int stt) {
            this.stt = stt;
        }

        public String getRealTime() {
            return realTime;
        }

        public void setRealTime(String realTime) {
            this.realTime = realTime;
        }

        public double getOptimalQuantityFrom() {
            return optimalQuantityFrom;
        }

        public void setOptimalQuantityFrom(double optimalQuantityFrom) {
            this.optimalQuantityFrom = optimalQuantityFrom;
        }

        public double getOptimalQuantityTo() {
            return optimalQuantityTo;
        }

        public void setOptimalQuantityTo(double optimalQuantityTo) {
            this.optimalQuantityTo = optimalQuantityTo;
        }

        public String getOptimalString() {
            return optimalString;
        }

        public void setOptimalString(String optimalString) {
            this.optimalString = optimalString;
        }

        public Double getHighLevelResults() {
            return highLevelResults;
        }

        public void setHighLevelResults(Double highLevelResults) {
            this.highLevelResults = highLevelResults;
        }

        public String getConcludeHighString() {
            return concludeHighString;
        }

        public void setConcludeHighString(String concludeHighString) {
            this.concludeHighString = concludeHighString;
        }

        public String getWarning() {
            return warning;
        }

        public void setWarning(String warning) {
            this.warning = warning;
        }

        public double getLowLevelResults() {
            return lowLevelResults;
        }

        public void setLowLevelResults(double lowLevelResults) {
            this.lowLevelResults = lowLevelResults;
        }

        public String getConcludeLowString() {
            return concludeLowString;
        }

        public void setConcludeLowString(String concludeLowString) {
            this.concludeLowString = concludeLowString;
        }

        public String getWarningDeep() {
            return warningDeep;
        }

        public void setWarningDeep(String warningDeep) {
            this.warningDeep = warningDeep;
        }
    }

    public static class CropYieldRes {
        private int id;
        private String harvestTime;
        private String crop;
        private String standardQuantity;
        private double realQuantity;
        private String fruitsStandardQuantity;
        private double fruitsRealQuantity;
        private double averageNum;

        // Getters and Setters

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getHarvestTime() {
            return harvestTime;
        }

        public void setHarvestTime(String harvestTime) {
            this.harvestTime = harvestTime;
        }

        public String getCrop() {
            return crop;
        }

        public void setCrop(String crop) {
            this.crop = crop;
        }

        public String getStandardQuantity() {
            return standardQuantity;
        }

        public void setStandardQuantity(String standardQuantity) {
            this.standardQuantity = standardQuantity;
        }

        public double getRealQuantity() {
            return realQuantity;
        }

        public void setRealQuantity(double realQuantity) {
            this.realQuantity = realQuantity;
        }

        public String getFruitsStandardQuantity() {
            return fruitsStandardQuantity;
        }

        public void setFruitsStandardQuantity(String fruitsStandardQuantity) {
            this.fruitsStandardQuantity = fruitsStandardQuantity;
        }

        public double getFruitsRealQuantity() {
            return fruitsRealQuantity;
        }

        public void setFruitsRealQuantity(double fruitsRealQuantity) {
            this.fruitsRealQuantity = fruitsRealQuantity;
        }

        public double getAverageNum() {
            return averageNum;
        }

        public void setAverageNum(double averageNum) {
            this.averageNum = averageNum;
        }
    }
}
