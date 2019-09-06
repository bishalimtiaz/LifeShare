package com.blz.prisoner.lifeshare;

public class InformationData {
    String senderI,receiverI,senderI_uid,receiverI_uid,dates;

    public InformationData() {
    }

    public InformationData(String senderI, String receiverI, String senderI_uid, String receiverI_uid, String dates) {
        this.senderI = senderI;
        this.receiverI = receiverI;
        this.senderI_uid = senderI_uid;
        this.receiverI_uid = receiverI_uid;
        this.dates = dates;
    }

    public String getSenderI() {
        return senderI;
    }

    public void setSenderI(String senderI) {
        this.senderI = senderI;
    }

    public String getReceiverI() {
        return receiverI;
    }

    public void setReceiverI(String receiverI) {
        this.receiverI = receiverI;
    }

    public String getSenderI_uid() {
        return senderI_uid;
    }

    public void setSenderI_uid(String senderI_uid) {
        this.senderI_uid = senderI_uid;
    }

    public String getReceiverI_uid() {
        return receiverI_uid;
    }

    public void setReceiverI_uid(String receiverI_uid) {
        this.receiverI_uid = receiverI_uid;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }
}
