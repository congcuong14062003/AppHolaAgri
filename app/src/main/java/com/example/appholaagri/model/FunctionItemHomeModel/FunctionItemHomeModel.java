package com.example.appholaagri.model.FunctionItemHomeModel;

import androidx.annotation.DrawableRes;

public class FunctionItemHomeModel {
    private String name;
    private int iconResId;
    private Class<?> targetActivity;
    private Integer groupRequestId; // Thêm GroupRequestId
    private Integer groupRequestType; // Thêm GroupRequestType

    public FunctionItemHomeModel(String name, @DrawableRes int iconResId, Class<?> targetActivity) {
        this.name = name;
        this.iconResId = iconResId;
        this.targetActivity = targetActivity;
        this.groupRequestId = null;
        this.groupRequestType = null;
    }

    public FunctionItemHomeModel(String name, @DrawableRes int iconResId, Class<?> targetActivity, Integer groupRequestId, Integer groupRequestType) {
        this.name = name;
        this.iconResId = iconResId;
        this.targetActivity = targetActivity;
        this.groupRequestId = groupRequestId;
        this.groupRequestType = groupRequestType;
    }

    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }

    public Class<?> getTargetActivity() {
        return targetActivity;
    }

    public Integer getGroupRequestId() {
        return groupRequestId;
    }

    public Integer getGroupRequestType() {
        return groupRequestType;
    }
}