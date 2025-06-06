package com.example.appholaagri.model.ListUserModel;

import java.util.List;

public class ListUserRequest {
    private List<Integer> idDepartment;
    private String keySearch;

    public ListUserRequest(List<Integer> idDepartment, String keySearch) {
        this.idDepartment = idDepartment;
        this.keySearch = keySearch;
    }

    // Getters and setters
    public List<Integer> getIdDepartment() {
        return idDepartment;
    }

    public void setIdDepartment(List<Integer> idDepartment) {
        this.idDepartment = idDepartment;
    }

    public String getKeySearch() {
        return keySearch;
    }

    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
    }
}