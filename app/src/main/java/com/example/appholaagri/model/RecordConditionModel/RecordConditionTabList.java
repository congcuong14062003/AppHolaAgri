package com.example.appholaagri.model.RecordConditionModel;

import java.io.Serializable;

public class RecordConditionTabList implements Serializable {
    private int id;
    private String code;
    private String name;
    private int status;
    private String color;

    // Getters
    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }

    public String getColor() {
        return color;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "RequestTabListData{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", color='" + color + '\'' +
                '}';
    }
}
