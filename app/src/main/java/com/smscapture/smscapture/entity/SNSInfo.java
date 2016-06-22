package com.smscapture.smscapture.entity;

import java.util.Date;

/**
 * Created by ${dennis} on 6/22/16.
 */
public class SNSInfo {
    private String date;
    private String number;
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
