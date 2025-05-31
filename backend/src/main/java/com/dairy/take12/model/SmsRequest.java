package com.dairy.take12.model;

public class SmsRequest {
    private String phone;
    private String message;

    public SmsRequest(String phone, String message) {
        this.phone = phone;
        this.message = message;
    }

    public String getPhone() { return phone; }
    public String getMessage() { return message; }
}