package com.example.appholaagri.model.RequestStatusModel;

import java.util.List;

public class RequestStatusResponse {
    private List<RequestStatusData> data;
    private int numOfRecords;

    // Constructor
    public RequestStatusResponse(List<RequestStatusData> data, int numOfRecords) {
        this.data = data;
        this.numOfRecords = numOfRecords;
    }

    // Getters and Setters
    public List<RequestStatusData> getData() {
        return data;
    }

    public void setData(List<RequestStatusData> data) {
        this.data = data;
    }

    public int getNumOfRecords() {
        return numOfRecords;
    }

    public void setNumOfRecords(int numOfRecords) {
        this.numOfRecords = numOfRecords;
    }

    // toString() for debugging
    @Override
    public String toString() {
        return "RequestStatusRequest{" +
                "data=" + data +
                ", numOfRecords=" + numOfRecords +
                '}';
    }
}
