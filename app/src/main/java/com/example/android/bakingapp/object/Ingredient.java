package com.example.android.bakingapp.object;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
    private String mName;
    private String mMeasure;
    private double mQuantity;

    public Ingredient(String name, String measure, double quantity) {
        mName = name;
        mMeasure = measure;
        mQuantity = quantity;
    }

    public String getName() {
        return mName;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public double getQuantity() {
        return mQuantity;
    }

    protected Ingredient(Parcel in) {
        mName = in.readString();
        mMeasure = in.readString();
        mQuantity = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mMeasure);
        dest.writeDouble(mQuantity);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
