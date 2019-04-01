package com.example.android.bakingapp.object;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {
    private String mShortDesc;
    private String mDescription;
    private String mMediaUrl;

    public Step(String shortDesc, String description, String mediaUrl) {
        mShortDesc = shortDesc;
        mDescription = description;
        mMediaUrl = mediaUrl;
    }

    public Step(String shortDesc, String description) {
        this(shortDesc, description, null);
    }

    public String getShortDesc() {
        return mShortDesc;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getMediaUrl() {
        return mMediaUrl;
    }

    protected Step(Parcel in) {
        mShortDesc = in.readString();
        mDescription = in.readString();
        mMediaUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mShortDesc);
        dest.writeString(mDescription);
        dest.writeString(mMediaUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
