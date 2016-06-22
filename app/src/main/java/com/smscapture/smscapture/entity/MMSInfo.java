package com.smscapture.smscapture.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${dennis} on 6/21/16.
 */
public class MMSInfo implements Parcelable {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;
    private String date;
    private String address;
    private List<MMSContentInfo> content =new ArrayList<>();

    public MMSInfo() {
    }


    protected MMSInfo(Parcel in) {
        date = in.readString();
        address = in.readString();
        id=in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(address);
        dest.writeLong(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MMSInfo> CREATOR = new Creator<MMSInfo>() {
        @Override
        public MMSInfo createFromParcel(Parcel in) {
            return new MMSInfo(in);
        }

        @Override
        public MMSInfo[] newArray(int size) {
            return new MMSInfo[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<MMSContentInfo> getContent() {
        return content;
    }

    public void setContent(List<MMSContentInfo> content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
