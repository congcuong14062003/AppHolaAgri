package com.example.appholaagri.model.RequestGroupModel;

import java.util.List;

public class RequestGroupResponse {

    private List<RequestGroup> data;
    private int numOfRecords;
    private int totalRecord;

    public static class RequestGroup {
        private int id;
        private String name;
        private int position;
        private int detailType;
        private String url;

        // Getters và Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getDetailType() {
            return detailType;
        }

        public void setDetailType(int detailType) {
            this.detailType = detailType;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    // Getters và Setters
    public List<RequestGroup> getData() {
        return data;
    }

    public void setData(List<RequestGroup> data) {
        this.data = data;
    }

    public int getNumOfRecords() {
        return numOfRecords;
    }

    public void setNumOfRecords(int numOfRecords) {
        this.numOfRecords = numOfRecords;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }
}


