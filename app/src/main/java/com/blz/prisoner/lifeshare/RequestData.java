package com.blz.prisoner.lifeshare;

public class RequestData {

    String receiver_uid,sender_uid,sender,reciver,request_Type,receiver_device_token;

    public RequestData() {
    }

    public RequestData(String receiver_uid, String sender_uid, String sender, String reciver, String request_Type,String receiver_device_token) {
        this.receiver_uid = receiver_uid;
        this.sender_uid = sender_uid;
        this.sender = sender;
        this.reciver = reciver;
        this.request_Type = request_Type;
        this.receiver_device_token = receiver_device_token;
    }

    public String getReceiver_uid() {
        return receiver_uid;
    }

    public void setReceiver_uid(String receiver_uid) {
        this.receiver_uid = receiver_uid;
    }

    public String getSender_uid() {
        return sender_uid;
    }

    public void setSender_uid(String sender_uid) {
        this.sender_uid = sender_uid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public String getRequest_Type() {
        return request_Type;
    }

    public void setRequest_Type(String request_Type) {
        this.request_Type = request_Type;
    }

    public String getReceiver_device_token() {
        return receiver_device_token;
    }

    public void setReceiver_device_token(String receiver_device_token) {
        this.receiver_device_token = receiver_device_token;
    }
}
