package com.example.appholaagri.model.UserData;

public class WorkInfor {
    public Unit unit;
    public Division division;
    public Department department;
    public Team team;
    public Title title;
    public Position position;
    public Manager manager;
    public Degrees degrees;
    public String description;
    public int isAttendance;  // Chuyển kiểu sang int

    // Getter và Setter
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Degrees getDegrees() {
        return degrees;
    }

    public void setDegrees(Degrees degrees) {
        this.degrees = degrees;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Chuyển đổi isAttendance từ int thành boolean
    public boolean isAttendance() {
        return isAttendance == 1;  // Chuyển 1 thành true, 0 thành false
    }

    public void setAttendance(int attendance) {
        this.isAttendance = attendance;
    }

    @Override
    public String toString() {
        return "WorkInfo{" +
                "unit=" + unit +
                ", division=" + division +
                ", department=" + department +
                ", team=" + team +
                ", title=" + title +
                ", position=" + position +
                ", manager=" + manager +
                ", degrees=" + degrees +
                ", description='" + description + '\'' +
                ", isAttendance=" + isAttendance +
                '}';
    }
}
