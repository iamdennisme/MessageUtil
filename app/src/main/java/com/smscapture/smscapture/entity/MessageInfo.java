package com.smscapture.smscapture.entity;

/**
 * Created by ${dennis} on 6/21/16.
 */
public class MessageInfo {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;
    private String recipientIds;
    private long date;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getRecipientIds() {
        return recipientIds;
    }

    public void setRecipientIds(String recipientIds) {
        this.recipientIds = recipientIds;
    }
}
