package com.example.appholaagri.model.TimekeepingStatisticsModel;

public class TimekeepingStatisticsRequest {

    private String date;  // Ngày (vd: "2025-01-07")
    private int isDaily;  // 1 cho chấm công hàng ngày, 0 cho chấm công theo tháng/năm
    private int page;     // Trang của dữ liệu
    private int size;     // Số lượng bản ghi trên mỗi trang

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIsDaily() {
        return isDaily;
    }

    public void setIsDaily(int isDaily) {
        this.isDaily = isDaily;
    }

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
