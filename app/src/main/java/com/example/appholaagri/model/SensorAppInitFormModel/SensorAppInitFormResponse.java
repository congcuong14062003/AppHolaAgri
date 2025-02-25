package com.example.appholaagri.model.SensorAppInitFormModel;

import java.util.List;

public class SensorAppInitFormResponse {
    private int isQrAvailable;
    private List<Plantation> plantationList;
    private List<Asset> assetList;
    private Object objectSelected;

    public int getIsQrAvailable() {
        return isQrAvailable;
    }

    public void setIsQrAvailable(int isQrAvailable) {
        this.isQrAvailable = isQrAvailable;
    }

    public List<Plantation> getPlantationList() {
        return plantationList;
    }

    public void setPlantationList(List<Plantation> plantationList) {
        this.plantationList = plantationList;
    }

    public List<Asset> getAssetList() {
        return assetList;
    }

    public void setAssetList(List<Asset> assetList) {
        this.assetList = assetList;
    }

    public Object getObjectSelected() {
        return objectSelected;
    }

    public void setObjectSelected(Object objectSelected) {
        this.objectSelected = objectSelected;
    }

    public static class Plantation {
        private int id;
        private String name;
        private int index;
        private List<Area> area;

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

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public List<Area> getArea() {
            return area;
        }

        public void setArea(List<Area> area) {
            this.area = area;
        }
    }

    public static class Area {
        private int id;
        private String name;
        private int index;
        private int totalRow;
        private int totalColumn;
        private List<Monitoring> monitoring;

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

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getTotalRow() {
            return totalRow;
        }

        public void setTotalRow(int totalRow) {
            this.totalRow = totalRow;
        }

        public int getTotalColumn() {
            return totalColumn;
        }

        public void setTotalColumn(int totalColumn) {
            this.totalColumn = totalColumn;
        }

        public List<Monitoring> getMonitoring() {
            return monitoring;
        }

        public void setMonitoring(List<Monitoring> monitoring) {
            this.monitoring = monitoring;
        }
    }

    public static class Monitoring {
        private int id;
        private String name;
        private int index;
        private int rowIn;
        private int columnIn;
        private List<MonitoringDetail> monitoringDetail;

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

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getRowIn() {
            return rowIn;
        }

        public void setRowIn(int rowIn) {
            this.rowIn = rowIn;
        }

        public int getColumnIn() {
            return columnIn;
        }

        public void setColumnIn(int columnIn) {
            this.columnIn = columnIn;
        }

        public List<MonitoringDetail> getMonitoringDetail() {
            return monitoringDetail;
        }

        public void setMonitoringDetail(List<MonitoringDetail> monitoringDetail) {
            this.monitoringDetail = monitoringDetail;
        }
    }

    public static class MonitoringDetail {
        private int id;
        private String nameCultivationArea;
        private int idCultivationArea;
        private int index;
        private int columnFrom;
        private int columnTo;
        private int rowFrom;
        private int rowTo;
        private int indexAreaSelected;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNameCultivationArea() {
            return nameCultivationArea;
        }

        public void setNameCultivationArea(String nameCultivationArea) {
            this.nameCultivationArea = nameCultivationArea;
        }

        public int getIdCultivationArea() {
            return idCultivationArea;
        }

        public void setIdCultivationArea(int idCultivationArea) {
            this.idCultivationArea = idCultivationArea;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

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

        public int getIndexAreaSelected() {
            return indexAreaSelected;
        }

        public void setIndexAreaSelected(int indexAreaSelected) {
            this.indexAreaSelected = indexAreaSelected;
        }
    }

    public static class Asset {
        private int id;
        private String name;
        private int isFixed;

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

        public int getIsFixed() {
            return isFixed;
        }

        public void setIsFixed(int isFixed) {
            this.isFixed = isFixed;
        }
    }

}