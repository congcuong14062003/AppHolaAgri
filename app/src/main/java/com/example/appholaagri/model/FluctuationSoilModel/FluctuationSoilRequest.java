package com.example.appholaagri.model.FluctuationSoilModel;

public class FluctuationSoilRequest {
    private Integer idPlant;  // Để nullable khi không sử dụng
    private Integer idMonitoring;  // Để nullable khi không sử dụng
    private int size;

    // Constructor mặc định
    public FluctuationSoilRequest() {
    }

    // Constructor khi cần gọi với idPlant và size
    public FluctuationSoilRequest(int idPlant, int size) {
        this.idPlant = idPlant;
        this.size = size;
        this.idMonitoring = null; // Không dùng
    }

    // Constructor khi cần gọi với idMonitoring và size
    public FluctuationSoilRequest(int idMonitoring, int size, boolean isMonitoring) {
        this.idMonitoring = idMonitoring;
        this.size = size;
        this.idPlant = null; // Không dùng
    }

    public Integer getIdPlant() {
        return idPlant;
    }

    public void setIdPlant(Integer idPlant) {
        this.idPlant = idPlant;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Integer getIdMonitoring() {
        return idMonitoring;
    }

    public void setIdMonitoring(Integer idMonitoring) {
        this.idMonitoring = idMonitoring;
    }
}
