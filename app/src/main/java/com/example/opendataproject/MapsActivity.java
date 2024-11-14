package com.example.opendataproject;

import androidx.fragment.app.FragmentActivity;
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

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ClusterManager<CovidClusterItem> clusterManager;
    private List<CovidData> covidDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        covidDataList = getIntent().getParcelableArrayListExtra("covidDataList");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Initialize the cluster manager
        clusterManager = new ClusterManager<>(this, mMap);
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);

        addCovidDataToMap();

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View view = getLayoutInflater().inflate(R.layout.marker_info_window, null);
                TextView title = view.findViewById(R.id.info_title);
                TextView snippet = view.findViewById(R.id.info_snippet);
                title.setText(marker.getTitle());
                snippet.setText(marker.getSnippet());
                return view;
            }
        });
    }

    private void addCovidDataToMap() {
        for (CovidData data : covidDataList) {
            LatLng position = new LatLng(data.getLocation().getLat(), data.getLocation().getLon());
            CovidClusterItem item = new CovidClusterItem(position, data.getAdmin2(),
                    "Cases: " + data.getTotConfirmed() + ", Deaths: " + data.getTotDeath(), data);
            clusterManager.addItem(item);
        }
        clusterManager.cluster();
    }
}
