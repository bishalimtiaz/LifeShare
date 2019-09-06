package com.blz.prisoner.lifeshare;

public class NotificationData {

    String fullName;
    String dateTime;
    String bloodGroup;
    String phone;
    String lattitude;
    String longitude;
    String address;

    String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }



    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public NotificationData(String fullName, String dateTime, String bloodGroup, String phone, String address, String lattitude, String longitude,String userid) {
        this.fullName = fullName;
        this.dateTime = dateTime;
        this.bloodGroup = bloodGroup;
        this.phone = phone;
        this.lattitude = lattitude;

        this.longitude = longitude;
        this.address = address;
        this.userid = userid;
    }

    public NotificationData(){
        //empty constructor
    }



    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBloodGroup() {
        return bloodGroup;

    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}


