package com.example.appholaagri.model.ListSensorModel;

import java.util.List;

public class ListSensorRequest<T> {
    private T idPlantation;  // Có thể là Integer hoặc List<Integer>
    private String keySearch;
    private int page;
    private int size;

    // Constructor mặc định
    public ListSensorRequest() {
    }

    // Constructor cho một ID duy nhất
    public ListSensorRequest(T idPlantation, String keySearch, int page, int size) {
        this.idPlantation = idPlantation;
        this.keySearch = keySearch;
        this.page = page;
        this.size = size;
    }

    // Getters and Setters
    public T getIdPlantation() {
        return idPlantation;
    }

    public void setIdPlantation(T idPlantation) {
        this.idPlantation = idPlantation;
    }

    public String getKeySearch() {
        return keySearch;
    }

    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
