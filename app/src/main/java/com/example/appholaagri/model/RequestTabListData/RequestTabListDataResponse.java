package com.example.appholaagri.model.RequestTabListData;

import java.util.List;

public class RequestTabListDataResponse {
    private List<RequestTabListData> data;
    private int numOfRecords;

    // Getter và Setter cho các thuộc tính
    public List<RequestTabListData> getData() {
        return data;
    }

    public void setData(List<RequestTabListData> data) {
        this.data = data;
    }

    public int getNumOfRecords() {
        return numOfRecords;
    }

    public void setNumOfRecords(int numOfRecords) {
        this.numOfRecords = numOfRecords;
    }

    @Override
    public String toString() {
        return "RequestTabListDataResponse{" +
                "data=" + data +
                ", numOfRecords=" + numOfRecords +
                '}';
    }
}
