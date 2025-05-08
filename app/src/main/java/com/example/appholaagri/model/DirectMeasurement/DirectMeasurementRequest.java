package com.example.appholaagri.model.DirectMeasurement;

import java.io.Serializable;

public class DirectMeasurementRequest implements Serializable {
    private String codeSensor;
    private DataInfoPlant dataInfoPlant;
    private DataWriteSoilManual dataWriteSoilManual;
    private int idSensor;
    private int isWrite;
    private String nameSensor;

    // Getter và Setter
    public String getCodeSensor() {
        return codeSensor;
    }

    public void setCodeSensor(String codeSensor) {
        this.codeSensor = codeSensor;
    }

    public DataInfoPlant getDataInfoPlant() {
        return dataInfoPlant;
    }

    public void setDataInfoPlant(DataInfoPlant dataInfoPlant) {
        this.dataInfoPlant = dataInfoPlant;
    }

    public DataWriteSoilManual getDataWriteSoilManual() {
        return dataWriteSoilManual;
    }

    public void setDataWriteSoilManual(DataWriteSoilManual dataWriteSoilManual) {
        this.dataWriteSoilManual = dataWriteSoilManual;
    }

    public int getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(int idSensor) {
        this.idSensor = idSensor;
    }

    public int getIsWrite() {
        return isWrite;
    }

    public void setIsWrite(int isWrite) {
        this.isWrite = isWrite;
    }

    public String getNameSensor() {
        return nameSensor;
    }

    public void setNameSensor(String nameSensor) {
        this.nameSensor = nameSensor;
    }

    // Nested Classes
    public static class DataInfoPlant implements Serializable {
        private int idCropVarieties;
        private String nameCropVarieties;
        private String namePlantation;
        private String codePlant;
        private int rowIn;
        private int idPlantation;
        private int idCultivationArea;
        private int idPlant;
        private int columnIn;
        private String nameCultivationArea;

        // Getter và Setter
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

        public String getNamePlantation() {
            return namePlantation;
        }

        public void setNamePlantation(String namePlantation) {
            this.namePlantation = namePlantation;
        }

        public String getCodePlant() {
            return codePlant;
        }

        public void setCodePlant(String codePlant) {
            this.codePlant = codePlant;
        }

        public int getRowIn() {
            return rowIn;
        }

        public void setRowIn(int rowIn) {
            this.rowIn = rowIn;
        }

        public int getIdPlantation() {
            return idPlantation;
        }

        public void setIdPlantation(int idPlantation) {
            this.idPlantation = idPlantation;
        }

        public int getIdCultivationArea() {
            return idCultivationArea;
        }

        public void setIdCultivationArea(int idCultivationArea) {
            this.idCultivationArea = idCultivationArea;
        }

        public int getIdPlant() {
            return idPlant;
        }

        public void setIdPlant(int idPlant) {
            this.idPlant = idPlant;
        }

        public int getColumnIn() {
            return columnIn;
        }

        public void setColumnIn(int columnIn) {
            this.columnIn = columnIn;
        }

        public String getNameCultivationArea() {
            return nameCultivationArea;
        }

        public void setNameCultivationArea(String nameCultivationArea) {
            this.nameCultivationArea = nameCultivationArea;
        }
    }

    public static class DataWriteSoilManual implements Serializable {
        private String humidityName;
        private float humidity;
        private String temperatureName;
        private float temperature;
        private String electricalConductivityName;
        private float electricalConductivity;
        private String phName;
        private float ph;
        private String nitrogenName;
        private float nitrogen;
        private String phosphorusName;
        private float phosphorus;
        private String kaliumName;
        private float kalium;

        // Getter và Setter
        public String getHumidityName() {
            return humidityName;
        }

        public void setHumidityName(String humidityName) {
            this.humidityName = humidityName;
        }

        public float getHumidity() {
            return humidity;
        }

        public void setHumidity(float humidity) {
            this.humidity = humidity;
        }

        public String getTemperatureName() {
            return temperatureName;
        }

        public void setTemperatureName(String temperatureName) {
            this.temperatureName = temperatureName;
        }

        public float getTemperature() {
            return temperature;
        }

        public void setTemperature(float temperature) {
            this.temperature = temperature;
        }

        public String getElectricalConductivityName() {
            return electricalConductivityName;
        }

        public void setElectricalConductivityName(String electricalConductivityName) {
            this.electricalConductivityName = electricalConductivityName;
        }

        public float getElectricalConductivity() {
            return electricalConductivity;
        }

        public void setElectricalConductivity(float electricalConductivity) {
            this.electricalConductivity = electricalConductivity;
        }

        public String getPhName() {
            return phName;
        }

        public void setPhName(String phName) {
            this.phName = phName;
        }

        public float getPh() {
            return ph;
        }

        public void setPh(float ph) {
            this.ph = ph;
        }

        public String getNitrogenName() {
            return nitrogenName;
        }

        public void setNitrogenName(String nitrogenName) {
            this.nitrogenName = nitrogenName;
        }

        public float getNitrogen() {
            return nitrogen;
        }

        public void setNitrogen(float nitrogen) {
            this.nitrogen = nitrogen;
        }

        public String getPhosphorusName() {
            return phosphorusName;
        }

        public void setPhosphorusName(String phosphorusName) {
            this.phosphorusName = phosphorusName;
        }

        public float getPhosphorus() {
            return phosphorus;
        }

        public void setPhosphorus(float phosphorus) {
            this.phosphorus = phosphorus;
        }

        public String getKaliumName() {
            return kaliumName;
        }

        public void setKaliumName(String kaliumName) {
            this.kaliumName = kaliumName;
        }

        public float getKalium() {
            return kalium;
        }

        public void setKalium(float kalium) {
            this.kalium = kalium;
        }
    }
}