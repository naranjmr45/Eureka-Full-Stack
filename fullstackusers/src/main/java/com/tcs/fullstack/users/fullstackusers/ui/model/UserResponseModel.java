package com.tcs.fullstack.users.fullstackusers.ui.model;

import java.util.List;

public class UserResponseModel {

    private String firstName;
    private String lastName;
    private String email;
    private String userId;
    private List<ItemResponseModel> items;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<ItemResponseModel> getItems() {
        return items;
    }

    public void setItems(List<ItemResponseModel> items) {
        this.items = items;
    }

}
