package com.blz.prisoner.lifeshare;

public class DonorData {

    String fullName;
    String password;
    String birthday;
    String bloodGroup;
    String gender;
    String phone;
    String isDonor ="yes";

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIsDonor() {
        return isDonor;
    }

    public void setIsDonor(String isDonor) {
        this.isDonor = isDonor;
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

    public String getSubLocality() {
        return subLocality;
    }

    public void setSubLocality(String subLocality) {
        this.subLocality = subLocality;
    }

    public String getThroughFare() {
        return throughFare;
    }

    public void setThroughFare(String throughFare) {
        this.throughFare = throughFare;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getSubAdminArea() {
        return subAdminArea;
    }

    public void setSubAdminArea(String subAdminArea) {
        this.subAdminArea = subAdminArea;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCanGive_A_Pos() {
        return canGive_A_Pos;
    }

    public void setCanGive_A_Pos(String canGive_A_Pos) {
        this.canGive_A_Pos = canGive_A_Pos;
    }

    public String getCanGive_A_Neg() {
        return canGive_A_Neg;
    }

    public void setCanGive_A_Neg(String canGive_A_Neg) {
        this.canGive_A_Neg = canGive_A_Neg;
    }

    public String getCanGive_B_Pos() {
        return canGive_B_Pos;
    }

    public void setCanGive_B_Pos(String canGive_B_Pos) {
        this.canGive_B_Pos = canGive_B_Pos;
    }

    public String getCanGive_B_Neg() {
        return canGive_B_Neg;
    }

    public void setCanGive_B_Neg(String canGive_B_Neg) {
        this.canGive_B_Neg = canGive_B_Neg;
    }

    public String getCanGive_AB_Pos() {
        return canGive_AB_Pos;
    }

    public void setCanGive_AB_Pos(String canGive_AB_Pos) {
        this.canGive_AB_Pos = canGive_AB_Pos;
    }

    public String getCanGive_AB_Neg() {
        return canGive_AB_Neg;
    }

    public void setCanGive_AB_Neg(String canGive_AB_Neg) {
        this.canGive_AB_Neg = canGive_AB_Neg;
    }

    public String getCanGive_O_Pos() {
        return canGive_O_Pos;
    }

    public void setCanGive_O_Pos(String canGive_O_Pos) {
        this.canGive_O_Pos = canGive_O_Pos;
    }

    public String getCanGive_O_Neg() {
        return canGive_O_Neg;
    }

    public void setCanGive_O_Neg(String canGive_O_Neg) {
        this.canGive_O_Neg = canGive_O_Neg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String lattitude;
    String longitude;

    String subLocality;
    String throughFare;

    public DonorData(String fullName, String password, String birthday, String bloodGroup, String gender, String phone, String isDonor, String lattitude, String longitude, String subLocality, String throughFare, String locality, String subAdminArea, String area, String address, String age, String canGive_A_Pos, String canGive_A_Neg, String canGive_B_Pos, String canGive_B_Neg, String canGive_AB_Pos, String canGive_AB_Neg, String canGive_O_Pos, String canGive_O_Neg, String userId,String deviceToken) {
        this.fullName = fullName;
        this.password = password;
        this.birthday = birthday;
        this.bloodGroup = bloodGroup;
        this.gender = gender;
        this.phone = phone;
        this.isDonor = isDonor;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.subLocality = subLocality;
        this.throughFare = throughFare;
        this.locality = locality;
        this.subAdminArea = subAdminArea;
        this.area = area;
        this.address = address;
        this.age = age;
        this.canGive_A_Pos = canGive_A_Pos;
        this.canGive_A_Neg = canGive_A_Neg;
        this.canGive_B_Pos = canGive_B_Pos;
        this.canGive_B_Neg = canGive_B_Neg;
        this.canGive_AB_Pos = canGive_AB_Pos;
        this.canGive_AB_Neg = canGive_AB_Neg;
        this.canGive_O_Pos = canGive_O_Pos;
        this.canGive_O_Neg = canGive_O_Neg;
        this.userId = userId;
        this.deviceToken=deviceToken;
    }

    String locality;
    String subAdminArea;
    String area;
    String address;
    String age;

    String canGive_A_Pos="No";
    String canGive_A_Neg="No";
    String canGive_B_Pos="No";
    String canGive_B_Neg="No";
    String canGive_AB_Pos="No";
    String canGive_AB_Neg="No";
    String canGive_O_Pos="No";
    String canGive_O_Neg="No";

    String userId;
    String deviceToken;

    public DonorData(){

    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
