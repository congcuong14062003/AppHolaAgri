package com.example.appholaagri.model.FluctuationSoilModel;

import java.util.List;

public class FluctuationSoilResponse {
    private int idPlant;
    private String codePlant;
    private int idSensor;
    private String nameSensor;
    private int idMonitoring;
    private String nameMonitoring;
    private int idCultivationArea;
    private String nameCultivationArea;
    private int idPlantation;
    private String namePlantation;
    private String date;
    private String plantationTime;
    private List<FluctuationValue> fluctuationValue;
    private List<FluctuationManualValue> fluctuationManualValue;
    private int max30Cm;
    private int max50Cm;
    private boolean isEmpty30Cm;
    private boolean isEmpty50Cm;
    private int maxManual30Cm;
    private int maxManual50Cm;
    private boolean isEmptyManual30Cm;
    private boolean isEmptyManual50Cm;

    public int getIdPlant() {
        return idPlant;
    }

    public void setIdPlant(int idPlant) {
        this.idPlant = idPlant;
    }

    public String getCodePlant() {
        return codePlant;
    }

    public void setCodePlant(String codePlant) {
        this.codePlant = codePlant;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlantationTime() {
        return plantationTime;
    }

    public void setPlantationTime(String plantationTime) {
        this.plantationTime = plantationTime;
    }

    public List<FluctuationValue> getFluctuationValue() {
        return fluctuationValue;
    }

    public void setFluctuationValue(List<FluctuationValue> fluctuationValue) {
        this.fluctuationValue = fluctuationValue;
    }

    public List<FluctuationManualValue> getFluctuationManualValue() {
        return fluctuationManualValue;
    }

    public void setFluctuationManualValue(List<FluctuationManualValue> fluctuationManualValue) {
        this.fluctuationManualValue = fluctuationManualValue;
    }

    public int getMax30Cm() {
        return max30Cm;
    }

    public void setMax30Cm(int max30Cm) {
        this.max30Cm = max30Cm;
    }

    public int getMax50Cm() {
        return max50Cm;
    }

    public void setMax50Cm(int max50Cm) {
        this.max50Cm = max50Cm;
    }

    public boolean isEmpty30Cm() {
        return isEmpty30Cm;
    }

    public void setEmpty30Cm(boolean empty30Cm) {
        isEmpty30Cm = empty30Cm;
    }

    public boolean isEmpty50Cm() {
        return isEmpty50Cm;
    }

    public void setEmpty50Cm(boolean empty50Cm) {
        isEmpty50Cm = empty50Cm;
    }

    public int getMaxManual30Cm() {
        return maxManual30Cm;
    }

    public void setMaxManual30Cm(int maxManual30Cm) {
        this.maxManual30Cm = maxManual30Cm;
    }

    public int getMaxManual50Cm() {
        return maxManual50Cm;
    }

    public void setMaxManual50Cm(int maxManual50Cm) {
        this.maxManual50Cm = maxManual50Cm;
    }

    public boolean isEmptyManual30Cm() {
        return isEmptyManual30Cm;
    }

    public void setEmptyManual30Cm(boolean emptyManual30Cm) {
        isEmptyManual30Cm = emptyManual30Cm;
    }

    public boolean isEmptyManual50Cm() {
        return isEmptyManual50Cm;
    }

    public void setEmptyManual50Cm(boolean emptyManual50Cm) {
        isEmptyManual50Cm = emptyManual50Cm;
    }
    // Getters and Setters
    // Constructor
    // toString()
    public static class FluctuationValue {
        private String date;
        private int byGroup;
        private List<DataIndex> dataIndex30cm;
        private List<DataIndex> dataIndex50cm;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getByGroup() {
            return byGroup;
        }

        public void setByGroup(int byGroup) {
            this.byGroup = byGroup;
        }

        public List<DataIndex> getDataIndex30cm() {
            return dataIndex30cm;
        }

        public void setDataIndex30cm(List<DataIndex> dataIndex30cm) {
            this.dataIndex30cm = dataIndex30cm;
        }

        public List<DataIndex> getDataIndex50cm() {
            return dataIndex50cm;
        }

        public void setDataIndex50cm(List<DataIndex> dataIndex50cm) {
            this.dataIndex50cm = dataIndex50cm;
        }
    }

    public static class FluctuationManualValue {
        private String date;
        private int byGroup;
        private List<DataIndex> dataIndex30cm;
        private List<DataIndex> dataIndex50cm;

        // Getters and Setters
        // Constructor
        // toString()

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getByGroup() {
            return byGroup;
        }

        public void setByGroup(int byGroup) {
            this.byGroup = byGroup;
        }

        public List<DataIndex> getDataIndex30cm() {
            return dataIndex30cm;
        }

        public void setDataIndex30cm(List<DataIndex> dataIndex30cm) {
            this.dataIndex30cm = dataIndex30cm;
        }

        public List<DataIndex> getDataIndex50cm() {
            return dataIndex50cm;
        }

        public void setDataIndex50cm(List<DataIndex> dataIndex50cm) {
            this.dataIndex50cm = dataIndex50cm;
        }
    }

    public static class DataIndex {
        private Integer optimalQuantityFrom;
        private Integer optimalQuantityTo;
        private Integer realQuantity;
        private String unit;
        private String conclude;
        private String warning;
        private Integer id;
        private String name;
        private String nameVi;
        private String color;

        // Getters and Setters
        // Constructor
        // toString()

        public Integer getOptimalQuantityFrom() {
            return optimalQuantityFrom;
        }

        public void setOptimalQuantityFrom(Integer optimalQuantityFrom) {
            this.optimalQuantityFrom = optimalQuantityFrom;
        }

        public Integer getOptimalQuantityTo() {
            return optimalQuantityTo;
        }

        public void setOptimalQuantityTo(Integer optimalQuantityTo) {
            this.optimalQuantityTo = optimalQuantityTo;
        }

        public Integer getRealQuantity() {
            return realQuantity;
        }

        public void setRealQuantity(Integer realQuantity) {
            this.realQuantity = realQuantity;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getConclude() {
            return conclude;
        }

        public void setConclude(String conclude) {
            this.conclude = conclude;
        }

        public String getWarning() {
            return warning;
        }

        public void setWarning(String warning) {
            this.warning = warning;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}

