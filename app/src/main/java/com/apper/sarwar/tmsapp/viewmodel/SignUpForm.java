package com.apper.sarwar.tmsapp.viewmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignUpForm {

    @JsonProperty("dob")
    private String dob;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("user")
    private UserForm userForm;


    public SignUpForm(String dob, String phoneNumber, UserForm userForm) {
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.userForm = userForm;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserForm getUserForm() {
        return userForm;
    }

    public void setUserForm(UserForm userForm) {
        this.userForm = userForm;
    }
}
