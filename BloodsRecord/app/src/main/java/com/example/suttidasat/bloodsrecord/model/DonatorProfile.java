package com.example.suttidasat.bloodsrecord.model;

public class DonatorProfile {

    private String birth;
    private String fName;
    private String lName;
    private String nationalID;
    private String email;
    private String bloodGroup;

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



    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

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


}
