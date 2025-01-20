package com.example.appholaagri.model.RequestStatusModel;

public class RequestStatusRequest {
    private String keySearch;
    private int page;
    private int requestType;
    private int size;
    private int status;

    // Constructor
    public RequestStatusRequest() {

    }
    public RequestStatusRequest(String keySearch, int page, int requestType, int size, int status) {
        this.keySearch = keySearch;
        this.page = page;
        this.requestType = requestType;
        this.size = size;
        this.status = status;
    }

    // Getters and Setters
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

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // toString() for debugging
    @Override
    public String toString() {
        return "RequestStatusRequest{" +
                "keySearch='" + keySearch + '\'' +
                ", page=" + page +
                ", requestType=" + requestType +
                ", size=" + size +
                ", status=" + status +
                '}';
    }
}
