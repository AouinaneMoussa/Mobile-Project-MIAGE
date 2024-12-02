package com.example.opendataproject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CovidData implements Parcelable {

    @SerializedName("fips")
    @Expose
    private String fips;
    @SerializedName("province_state")
    @Expose
    private String provinceState;
    @SerializedName("admin2")
    @Expose
    private String admin2;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("tot_death")
    @Expose
    private int totDeath;
    @SerializedName("tot_confirmed")
    @Expose
    private int totConfirmed;
    @SerializedName("location")
    @Expose
    private Location location;

    // Getters and Setters
    public String getFips() {
        return fips;
    }

    public void setFips(String fips) {
        this.fips = fips;
    }

    public String getProvinceState() {
        return provinceState;
    }

    public void setProvinceState(String provinceState) {
        this.provinceState = provinceState;
    }

    public String getAdmin2() {
        return admin2;
    }

    public void setAdmin2(String admin2) {
        this.admin2 = admin2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotDeath() {
        return totDeath;
    }

    public void setTotDeath(int totDeath) {
        this.totDeath = totDeath;
    }

    public int getTotConfirmed() {
        return totConfirmed;
    }

    public void setTotConfirmed(int totConfirmed) {
        this.totConfirmed = totConfirmed;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    // Parcelable implementation
    protected CovidData(Parcel in) {
        fips = in.readString();
        provinceState = in.readString();
        admin2 = in.readString();
        date = in.readString();
        totDeath = in.readInt();
        totConfirmed = in.readInt();
        location = in.readParcelable(Location.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fips);
        dest.writeString(provinceState);
        dest.writeString(admin2);
        dest.writeString(date);
        dest.writeInt(totDeath);
        dest.writeInt(totConfirmed);
        dest.writeParcelable(location, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CovidData> CREATOR = new Creator<CovidData>() {
        @Override
        public CovidData createFromParcel(Parcel in) {
            return new CovidData(in);
        }

        @Override
        public CovidData[] newArray(int size) {
            return new CovidData[size];
        }
    };
}