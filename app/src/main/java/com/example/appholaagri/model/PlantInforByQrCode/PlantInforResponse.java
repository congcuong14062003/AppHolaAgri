package com.example.appholaagri.model.PlantInforByQrCode;

public class PlantInforResponse {

    private ManualNote manualNote;
    private DataInfoPlant dataInfoPlant;

    // Getters and Setters
    public ManualNote getManualNote() {
        return manualNote;
    }

    public void setManualNote(ManualNote manualNote) {
        this.manualNote = manualNote;
    }

    public DataInfoPlant getDataInfoPlant() {
        return dataInfoPlant;
    }

    public void setDataInfoPlant(DataInfoPlant dataInfoPlant) {
        this.dataInfoPlant = dataInfoPlant;
    }

    // ManualNote inner class
    public static class ManualNote {
        private String imageUrlNote1;
        private String note2;
        private String imageUrlNote2;
        private String note1;

        // Getters and Setters
        public String getImageUrlNote1() {
            return imageUrlNote1;
        }

        public void setImageUrlNote1(String imageUrlNote1) {
            this.imageUrlNote1 = imageUrlNote1;
        }

        public String getNote2() {
            return note2;
        }

        public void setNote2(String note2) {
            this.note2 = note2;
        }

        public String getImageUrlNote2() {
            return imageUrlNote2;
        }

        public void setImageUrlNote2(String imageUrlNote2) {
            this.imageUrlNote2 = imageUrlNote2;
        }

        public String getNote1() {
            return note1;
        }

        public void setNote1(String note1) {
            this.note1 = note1;
        }
    }

    // DataInfoPlant inner class
    public static class DataInfoPlant {
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

        // Getters and Setters
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
}
