package com.example.appholaagri.model.RequestDetailModel;

public class RequestMethod {
    private int id;
    private String code;
    private String name;
    private int status;
    private String color;
    private int index;
    private Threshold threshold;

    // Nested Threshold class
    public static class Threshold {
        private int minute;
        private double coEfficient;

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public double getCoEfficient() {
            return coEfficient;
        }

        public void setCoEfficient(double coEfficient) {
            this.coEfficient = coEfficient;
        }
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }

    public Threshold getThreshold() { return threshold; }
    public void setThreshold(Threshold threshold) { this.threshold = threshold; }
}
