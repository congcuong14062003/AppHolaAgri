package com.example.appholaagri.model.PlantationListModel;

import java.util.List;

public class PlantationListRequest {
    private List<Integer> idCompany;
    private String keySearch;
    private int page;
    private int size;
    private List<Integer> status;
    public  PlantationListRequest() {

    }
    public PlantationListRequest(List<Integer> idCompany, String keySearch, int page, int size, List<Integer> status) {
        this.idCompany = idCompany;
        this.keySearch = keySearch;
        this.page = page;
        this.size = size;
        this.status = status;
    }

    public List<Integer> getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(List<Integer> idCompany) {
        this.idCompany = idCompany;
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

    public List<Integer> getStatus() {
        return status;
    }

    public void setStatus(List<Integer> status) {
        this.status = status;
    }
}
