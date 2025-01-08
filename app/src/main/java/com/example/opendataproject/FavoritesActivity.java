package com.example.opendataproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private Spinner provinceSpinner;
    private RecyclerView favoritesRecyclerView;
    private List<CovidData> covidDataList = new ArrayList<>();
    private List<CovidData> favoriteDataList = new ArrayList<>();
    private String selectedProvince = "All Provinces";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        provinceSpinner = findViewById(R.id.province_spinner);
        favoritesRecyclerView = findViewById(R.id.favorites_recycler_view);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Récupérer les données passées depuis MainActivity
        if (getIntent().hasExtra("covidDataList")) {
            covidDataList = (List<CovidData>) getIntent().getSerializableExtra("covidDataList");
        }

        // Configurer le spinner
        configureProvinceSpinner();

        // Charger les favoris pour la province sélectionnée
        loadFavorites();

        // Configurer la navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_favorites);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (id == R.id.bottom_favorites) {
                return true;
            } else if (id == R.id.bottom_map) {
                Intent intent = new Intent(FavoritesActivity.this, MapsActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void configureProvinceSpinner() {
        List<String> provinces = new ArrayList<>();
        provinces.add("All Provinces");
        provinces.add("New York");
        provinces.add("California");
        provinces.add("Texas");
        provinces.add("Florida"); // Ajouter plus de provinces si nécessaire

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinces);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setAdapter(spinnerAdapter);

        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProvince = provinces.get(position);
                //saveSpinnerState(selectedProvince); // Sauvegarder la sélection
                loadFavorites(); // Charger les favoris pour la nouvelle province
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Aucune action requise ici
            }
        });
    }

    private void loadFavorites() {
        SharedPreferences sharedPreferences = getSharedPreferences("favorites", Context.MODE_PRIVATE);

        // Filtrer les favoris selon la province sélectionnée
        favoriteDataList.clear();
        for (CovidData data : covidDataList) {
            if (sharedPreferences.getBoolean(data.getFips(), false)) {
                if (selectedProvince.equals("All Provinces") || data.getProvinceState().equals(selectedProvince)) {
                    favoriteDataList.add(data);
                }
            }
        }

        // Mettre à jour l'adaptateur
        CovidDataAdapter adapter = new CovidDataAdapter(this, favoriteDataList);
        favoritesRecyclerView.setAdapter(adapter);
    }
}
