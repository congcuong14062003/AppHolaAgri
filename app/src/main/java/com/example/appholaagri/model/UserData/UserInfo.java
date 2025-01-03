package com.example.appholaagri.model.UserData;

public class UserInfo {
    private String birthDay;
    private String identityCode;
    private String taxCode;
    private String identityLicense;
    private String address;

    // Getters và setters
    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getIdentityCode() {
        return identityCode;
    }

    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getIdentityLicense() {
        return identityLicense;
    }

    public void setIdentityLicense(String identityLicense) {
        this.identityLicense = identityLicense;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
