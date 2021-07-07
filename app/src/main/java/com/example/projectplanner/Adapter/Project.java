package com.example.projectplanner.Adapter;

public class Project {
    private String name;
    private String description;
    private String date;
    private String daysLeft;
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(String daysLeft) {
        this.daysLeft = daysLeft;
    }

    public Project(String name, String description, String date, String daysLeft, Integer staus) {
        this.name = name;
        this.status = staus;
        this.description = description;
        this.date = date;
        this.daysLeft = daysLeft;
    }
}
