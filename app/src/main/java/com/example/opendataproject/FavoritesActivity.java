package com.example.opendataproject;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class FavoritesActivity extends AppCompatActivity {

    private List<CovidData> covidDataList = new ArrayList<>(); // Déclaration de la liste

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        RecyclerView favoritesRecyclerView = findViewById(R.id.favorites_recycler_view);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Récupérer les données de l'intention ou les initialiser
        if (getIntent().hasExtra("covidDataList")) {
            covidDataList = (List<CovidData>) getIntent().getSerializableExtra("covidDataList");
        }

        // Récupérer les favoris des SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("favorites", Context.MODE_PRIVATE);

        // Filtrer les favoris
        List<CovidData> favoriteDataList = new ArrayList<>();
        for (CovidData data : covidDataList) {
            if (sharedPreferences.getBoolean(data.getFips(), false)) {
                favoriteDataList.add(data);
            }
        }

        // Configurer l'adaptateur
        CovidDataAdapter adapter = new CovidDataAdapter(this, favoriteDataList);
        favoritesRecyclerView.setAdapter(adapter);

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
}

