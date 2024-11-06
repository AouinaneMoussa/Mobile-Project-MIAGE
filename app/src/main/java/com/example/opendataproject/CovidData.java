package com.example.opendataproject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CovidData {

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


}
