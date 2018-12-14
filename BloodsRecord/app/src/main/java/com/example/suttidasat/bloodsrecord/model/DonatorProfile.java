package com.example.suttidasat.bloodsrecord.model;

public class DonatorProfile {

    private String fName;
    private String lName;
    private String nationalID;
    private String email;
    private String bloodGroup;
    private String password;
    private String Address;

    // Singleton
    private static DonatorProfile donatorProfileInsatance;

    public static DonatorProfile getDonatorProfileInstance(){

        if (donatorProfileInsatance == null){
            donatorProfileInsatance = new DonatorProfile();
        }
        return donatorProfileInsatance;
    }

    private DonatorProfile() {
    }


    //getter, setter
    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
