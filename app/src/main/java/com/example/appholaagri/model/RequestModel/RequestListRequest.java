package com.example.appholaagri.model.RequestModel;

public class RequestListRequest {
    private String keySearch;
    private int page;
    private int requestType;
    private int size;
    private int status;

    // Constructor
    public RequestListRequest() {

    }
    public RequestListRequest(String keySearch, int page, int requestType, int size, int status) {
        this.keySearch = keySearch;
        this.page = page;
        this.requestType = requestType;
        this.size = size;
        this.status = status;
    }

    // Getter and Setter methods
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
}
