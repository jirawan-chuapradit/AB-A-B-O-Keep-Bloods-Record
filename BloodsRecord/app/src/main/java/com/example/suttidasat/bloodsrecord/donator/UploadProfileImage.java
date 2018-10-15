package com.example.suttidasat.bloodsrecord.donator;

public class UploadProfileImage {
        private String mName;
        private String mImageUrl;

    public UploadProfileImage() {
    }

    public UploadProfileImage(String mName, String mImageUrl) {
        if(mName.trim().equals("")){
            mName = "No name";
        }
        this.mName = mName;
        this.mImageUrl = mImageUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
