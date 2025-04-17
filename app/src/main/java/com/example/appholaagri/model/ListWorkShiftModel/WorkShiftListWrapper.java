package com.example.appholaagri.model.ListWorkShiftModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorkShiftListWrapper {
    @SerializedName("data")
    private List<ListWorkShiftResponse.WorkShiftData> data;

    @SerializedName("numOfRecords")
    private int numOfRecords;

    @SerializedName("totalRecord")
    private int totalRecord;

    public List<ListWorkShiftResponse.WorkShiftData> getData() {
        return data;
    }

    public int getNumOfRecords() {
        return numOfRecords;
    }

    public int getTotalRecord() {
        return totalRecord;
    }
}
