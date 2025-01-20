package com.example.appholaagri.model.RequestStatusModel;

public class RequestStatusData {
    private int id;
    private String name;
    private int count;

    // Constructor
    public RequestStatusData(int id, String name, int count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }

    // Getters and Setters
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    // toString() for debugging
    @Override
    public String toString() {
        return "RequestStatusData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
