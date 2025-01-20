package com.example.appholaagri.model.RequestTabListData;

public class RequestTabListData {
    private int id;
    private String name;
    private int index;

    // Constructor
    public RequestTabListData(int id, String name, int index) {
        this.id = id;
        this.name = name;
        this.index = index;
    }

    // Getter và Setter cho các thuộc tính
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    // Hàm toString() để hiển thị đối tượng dưới dạng chuỗi
    @Override
    public String toString() {
        return "RequestTabListData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", index=" + index +
                '}';
    }
}
