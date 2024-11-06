package com.example.opendataproject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CovidResponse {
    private int totalCount;
    private List<CovidData> results;

    public int getTotalCount() { return totalCount; }
    public List<CovidData> getResults() { return results; }
}
