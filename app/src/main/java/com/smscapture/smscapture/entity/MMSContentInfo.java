package com.smscapture.smscapture.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${dennis} on 6/22/16.
 */
public class MMSContentInfo implements Parcelable {
    private Bitmap bitmap = null;
    //0 图片 1 文字
    private int type = -1;
    private String message = null;

    public MMSContentInfo(Bitmap bitmap, int type, String message) {
        this.bitmap = bitmap;
        this.type = type;
        this.message = message;
    }

    protected MMSContentInfo(Parcel in) {
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        type = in.readInt();
        message = in.readString();
    }

    public static final Creator<MMSContentInfo> CREATOR = new Creator<MMSContentInfo>() {
        @Override
        public MMSContentInfo createFromParcel(Parcel in) {
            return new MMSContentInfo(in);
        }

        @Override
        public MMSContentInfo[] newArray(int size) {
            return new MMSContentInfo[size];
        }
    };

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(bitmap, i);
        parcel.writeInt(type);
        parcel.writeString(message);
    }
}
