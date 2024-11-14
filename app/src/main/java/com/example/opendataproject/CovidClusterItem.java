package com.example.opendataproject;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class CovidClusterItem implements ClusterItem {
    private final LatLng position;
    private final String title;
    private final String snippet;
    private final CovidData covidData;

    public CovidClusterItem(LatLng position, String title, String snippet, CovidData covidData) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.covidData = covidData;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public CovidData getCovidData() {
        return covidData;
    }
}
