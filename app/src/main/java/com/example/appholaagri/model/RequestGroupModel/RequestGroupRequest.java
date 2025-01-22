package com.example.appholaagri.model.RequestGroupModel;

public class RequestGroupRequest {
    private int page;
    private int size;

    // Constructor
    public RequestGroupRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    // Getters và Setters
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
