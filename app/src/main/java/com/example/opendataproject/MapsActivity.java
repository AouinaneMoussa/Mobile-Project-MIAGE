package com.example.opendataproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.example.opendataproject.databinding.ActivityMapsBinding;
import com.google.maps.android.clustering.ClusterManager;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private ClusterManager<CovidClusterItem> clusterManager;
    private List<CovidData> covidDataList;

    // MapsActivity.java

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.opendataproject.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve updated covid data list
        covidDataList = getIntent().getParcelableArrayListExtra("covidDataList");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // Initialize the cluster manager
        clusterManager = new ClusterManager<>(this, googleMap);
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);

        addCovidDataToMap(); // Add all Covid data to map

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(@NonNull Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(@NonNull Marker marker) {
                @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.marker_info_window, null);
                TextView title = view.findViewById(R.id.info_title);
                TextView snippet = view.findViewById(R.id.info_snippet);
                title.setText(marker.getTitle());
                snippet.setText(marker.getSnippet());
                return view;
            }
        });
    }

    private void addCovidDataToMap() {
        // Add markers for all Covid data entries
        for (CovidData data : covidDataList) {
            LatLng position = new LatLng(data.getLocation().getLat(), data.getLocation().getLon());
            CovidClusterItem item = new CovidClusterItem(position, data.getAdmin2(),
                    "Cases: " + data.getTotConfirmed() + ", Deaths: " + data.getTotDeath(), data);
            clusterManager.addItem(item);
        }
        clusterManager.cluster(); // Refresh the cluster
    }
}