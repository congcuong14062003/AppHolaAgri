package com.example.appholaagri.model.IdentificationPlantModel;
public class IdentificationPlantRequest {
    private int columnIn;
    private DateSelected dateSelected;
    private int idCropVarieties;
    private int idCultivationArea;
    private int idPlantation;
    private int isConfirmed;
    private String qrCode;
    private int rowIn;

    // Constructor
    public IdentificationPlantRequest() {

    }
    public IdentificationPlantRequest(int columnIn, DateSelected dateSelected, int idCropVarieties, int idCultivationArea, int idPlantation, int isConfirmed, String qrCode, int rowIn) {
        this.columnIn = columnIn;
        this.dateSelected = dateSelected;
        this.idCropVarieties = idCropVarieties;
        this.idCultivationArea = idCultivationArea;
        this.idPlantation = idPlantation;
        this.isConfirmed = isConfirmed;
        this.qrCode = qrCode;
        this.rowIn = rowIn;
    }

    // Getters and Setters
    public int getColumnIn() {
        return columnIn;
    }

    public void setColumnIn(int columnIn) {
        this.columnIn = columnIn;
    }

    public DateSelected getDateSelected() {
        return dateSelected;
    }

    public void setDateSelected(DateSelected dateSelected) {
        this.dateSelected = dateSelected;
    }

    public int getIdCropVarieties() {
        return idCropVarieties;
    }

    public void setIdCropVarieties(int idCropVarieties) {
        this.idCropVarieties = idCropVarieties;
    }

    public int getIdCultivationArea() {
        return idCultivationArea;
    }

    public void setIdCultivationArea(int idCultivationArea) {
        this.idCultivationArea = idCultivationArea;
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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public int getRowIn() {
        return rowIn;
    }

    public void setRowIn(int rowIn) {
        this.rowIn = rowIn;
    }

    // Nested class for dateSelected
    public static class DateSelected {
        private int id;
        private String name;

        public DateSelected(int id, String name) {
            this.id = id;
            this.name = name;
        }

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
    }
}
