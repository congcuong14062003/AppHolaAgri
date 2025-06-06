package com.example.appholaagri.model.ListUserModel;

import com.example.appholaagri.model.RequestDetailModel.Follower;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListUserResponse {
    @SerializedName("users")
    private List<Follower> users;

    public List<Follower> getUsers() {
        return users;
    }

    public void setUsers(List<Follower> users) {
        this.users = users;
    }
}