package com.example.appholaagri.model.SoilDetailModel;
import java.util.List;

public class SoilDetailRespose {

        private int idPlant;
        private String codePlant;
        private int idSenSor;
        private String nameSenSor;
        private int idMonitoring;
        private String nameMonitoring;
        private int idCultivationArea;
        private String nameCultivationArea;
        private int idPlantation;
        private String namePlantation;
        private String date;
        private String plantationTime;
        private List<SoilInfo> infoSoil;

        // Getters và Setters
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

        public int getIdSenSor() {
            return idSenSor;
        }

        public void setIdSenSor(int idSenSor) {
            this.idSenSor = idSenSor;
        }

        public String getNameSenSor() {
            return nameSenSor;
        }

        public void setNameSenSor(String nameSenSor) {
            this.nameSenSor = nameSenSor;
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

        public List<SoilInfo> getInfoSoil() {
            return infoSoil;
        }

        public void setInfoSoil(List<SoilInfo> infoSoil) {
            this.infoSoil = infoSoil;
        }
        public static class SoilInfo {
            private int id;
            private String name;
            private String nameVi;
            private int type;
            private String date;
            private String warning;
            private String iconUrl;
            private SoilIndex soilIndex30cm;
            private SoilIndex soilIndex50cm;

            // Getters và Setters
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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getWarning() {
                return warning;
            }

            public void setWarning(String warning) {
                this.warning = warning;
            }

            public String getIconUrl() {
                return iconUrl;
            }

            public void setIconUrl(String iconUrl) {
                this.iconUrl = iconUrl;
            }

            public SoilIndex getSoilIndex30cm() {
                return soilIndex30cm;
            }

            public void setSoilIndex30cm(SoilIndex soilIndex30cm) {
                this.soilIndex30cm = soilIndex30cm;
            }

            public SoilIndex getSoilIndex50cm() {
                return soilIndex50cm;
            }

            public void setSoilIndex50cm(SoilIndex soilIndex50cm) {
                this.soilIndex50cm = soilIndex50cm;
            }
        }

        public static class SoilIndex {
            private float optimalQuantityFrom;
            private float optimalQuantityTo;
            private float realQuantity;
            private String unit;
            private String conclude;
            private String waring;

            // Getters và Setters
            public float getOptimalQuantityFrom() {
                return optimalQuantityFrom;
            }

            public void setOptimalQuantityFrom(float optimalQuantityFrom) {
                this.optimalQuantityFrom = optimalQuantityFrom;
            }

            public float getOptimalQuantityTo() {
                return optimalQuantityTo;
            }

            public void setOptimalQuantityTo(float optimalQuantityTo) {
                this.optimalQuantityTo = optimalQuantityTo;
            }

            public float getRealQuantity() {
                return realQuantity;
            }

            public void setRealQuantity(float realQuantity) {
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

            public String getWaring() {
                return waring;
            }

            public void setWaring(String waring) {
                this.waring = waring;
            }

        }
    }

    // Lớp biểu diễn thông tin của từng chỉ số đất


    // Lớp biểu diễn thông tin chi tiết của từng chỉ số đất ở các độ sâu khác nhau
