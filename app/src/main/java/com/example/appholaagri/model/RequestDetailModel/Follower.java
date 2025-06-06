package com.example.appholaagri.model.RequestDetailModel;

import java.io.Serializable;

public class Follower implements Serializable {
    private int id;
    private String code;
    private String name;
    private int status;
    private String color;
    private Object index;
    private String team;
    private String url;
    // Getters and Setters
    public int getId() {
        return id;
    }

    public Follower() {

    }
    // Constructor
    public Follower(int id, String name, String code, String team, String url) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.team = team;
        this.url = url;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Object getIndex() {
        return index;
    }

    public void setIndex(Object index) {
        this.index = index;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
