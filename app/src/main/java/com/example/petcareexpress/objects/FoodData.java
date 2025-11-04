package com.example.petcareexpress.objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class FoodData implements Parcelable {
    public String name;
    public String foodType;
    public double minimumPetWeight;
    public double minimumFeedingAmount;
    public double recommendedServing;
    private boolean isSelected = false;
    public FoodData(String name, String foodType, double minimumPetWeight, double minimumFeedingAmount) {
        this.name = name;
        this.foodType = foodType;
        this.minimumPetWeight = minimumPetWeight;
        this.minimumFeedingAmount = minimumFeedingAmount;
        this.recommendedServing = getRecommendedServing();
    }

    protected FoodData(Parcel in) {
        name = in.readString();
        foodType = in.readString();
        minimumPetWeight = in.readDouble();
        minimumFeedingAmount = in.readDouble();
        recommendedServing = in.readDouble();
    }

    public static final Creator<FoodData> CREATOR = new Creator<>() {
        @Override
        public FoodData createFromParcel(Parcel in) {
            return new FoodData(in);
        }

        @Override
        public FoodData[] newArray(int size) {
            return new FoodData[size];
        }
    };

    public double getRecommendedServing() {
        return minimumFeedingAmount / minimumPetWeight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(foodType);
        dest.writeDouble(minimumPetWeight);
        dest.writeDouble(minimumFeedingAmount);
        dest.writeDouble(recommendedServing);
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
