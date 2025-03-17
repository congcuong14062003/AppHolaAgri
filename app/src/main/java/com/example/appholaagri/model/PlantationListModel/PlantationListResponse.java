package com.example.appholaagri.model.PlantationListModel;

import java.util.List;

public class PlantationListResponse {
    private List<PlantationData> data;
    private int totalRecord;

    public List<PlantationData> getData() {
        return data;
    }

    public void setData(List<PlantationData> data) {
        this.data = data;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public static class PlantationData {
        private int id;
        private String name;
        private String code;
        private int idCompany;
        private String address;
        private String description;
        private String longitude;
        private String latitude;
        private int length;
        private int width;
        private int acreage;
        private int direction;
        private int status;
        private Object index;
        private List<Integer> idCareTechniques;
        private List<CareTechnique> listCareTechniques;
        private String nameCareTechniques;
        private String nameCompany;
        private String directionString;
        private String modifiedBy;
        private String modifiedDate;
        private String statusString;
        private String areaName;
        private int totalMonitoring;
        private int totalPlant;

        // Getters and Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public int getIdCompany() { return idCompany; }
        public void setIdCompany(int idCompany) { this.idCompany = idCompany; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getLongitude() { return longitude; }
        public void setLongitude(String longitude) { this.longitude = longitude; }
        public String getLatitude() { return latitude; }
        public void setLatitude(String latitude) { this.latitude = latitude; }
        public int getLength() { return length; }
        public void setLength(int length) { this.length = length; }
        public int getWidth() { return width; }
        public void setWidth(int width) { this.width = width; }
        public int getAcreage() { return acreage; }
        public void setAcreage(int acreage) { this.acreage = acreage; }
        public int getDirection() { return direction; }
        public void setDirection(int direction) { this.direction = direction; }
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        public Object getIndex() { return index; }
        public void setIndex(Object index) { this.index = index; }
        public List<Integer> getIdCareTechniques() { return idCareTechniques; }
        public void setIdCareTechniques(List<Integer> idCareTechniques) { this.idCareTechniques = idCareTechniques; }
        public List<CareTechnique> getListCareTechniques() { return listCareTechniques; }
        public void setListCareTechniques(List<CareTechnique> listCareTechniques) { this.listCareTechniques = listCareTechniques; }
        public String getNameCareTechniques() { return nameCareTechniques; }
        public void setNameCareTechniques(String nameCareTechniques) { this.nameCareTechniques = nameCareTechniques; }
        public String getNameCompany() { return nameCompany; }
        public void setNameCompany(String nameCompany) { this.nameCompany = nameCompany; }
        public String getDirectionString() { return directionString; }
        public void setDirectionString(String directionString) { this.directionString = directionString; }
        public String getModifiedBy() { return modifiedBy; }
        public void setModifiedBy(String modifiedBy) { this.modifiedBy = modifiedBy; }
        public String getModifiedDate() { return modifiedDate; }
        public void setModifiedDate(String modifiedDate) { this.modifiedDate = modifiedDate; }
        public String getStatusString() { return statusString; }
        public void setStatusString(String statusString) { this.statusString = statusString; }
        public String getAreaName() { return areaName; }
        public void setAreaName(String areaName) { this.areaName = areaName; }
        public int getTotalMonitoring() { return totalMonitoring; }
        public void setTotalMonitoring(int totalMonitoring) { this.totalMonitoring = totalMonitoring; }
        public int getTotalPlant() { return totalPlant; }
        public void setTotalPlant(int totalPlant) { this.totalPlant = totalPlant; }
    }

    public static class CareTechnique {
        private String name;
        private int id;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
    }
}
