package com.example.opendataproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView favoritesRecyclerView;
    private CovidDataAdapter covidDataAdapter;
    private List<CovidData> favoriteCovidDataList = new ArrayList<>();
    private Set<String> favoriteFipsSet = new HashSet<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Setup Bottom Navigation
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

        // Initialize RecyclerView
        favoritesRecyclerView = findViewById(R.id.favorites_recycler_view);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        covidDataAdapter = new CovidDataAdapter(this, favoriteCovidDataList, favoriteFipsSet, this::saveFavorites);
        favoritesRecyclerView.setAdapter(covidDataAdapter);

        // Load favorite items
        loadFavorites();
    }

    private void loadFavorites() {
        SharedPreferences sharedPreferences = getSharedPreferences("favorites", MODE_PRIVATE);
        favoriteFipsSet = sharedPreferences.getStringSet("favorites", new HashSet<>());

        if (favoriteFipsSet == null || favoriteFipsSet.isEmpty()) {
            Toast.makeText(this, "No favorites added yet!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch data for the favorite FIPS codes
        favoriteCovidDataList.clear();
        for (String fips : favoriteFipsSet) {
            fetchFavoriteData(fips);
        }
    }

    private void fetchFavoriteData(String fips) {
        // Use Retrofit to fetch data based on the FIPS code
        String where = "fips='" + fips + "'";
        CovidDataInterface covidDataService = RetrofitClient.getRetrofitClient("https://public.opendatasoft.com/api/explore/v2.1/catalog/datasets/").create(CovidDataInterface.class);
        Call<CovidResponse> call = covidDataService.getCovidData(1, 0, where);

        call.enqueue(new Callback<CovidResponse>() {
            @Override
            public void onResponse(@NonNull Call<CovidResponse> call, @NonNull Response<CovidResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CovidData> results = response.body().getResults();
                    if (!results.isEmpty()) {
                        favoriteCovidDataList.add(results.get(0));
                        covidDataAdapter.notifyDataSetChanged(); // Refresh the RecyclerView
                    }
                } else {
                    Toast.makeText(FavoritesActivity.this, "Failed to load favorite data!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CovidResponse> call, @NonNull Throwable t) {
                Toast.makeText(FavoritesActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveFavorites() {
        getSharedPreferences("favorites", MODE_PRIVATE)
                .edit()
                .putStringSet("favorites", favoriteFipsSet)
                .apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recharger les favoris à chaque reprise de l'activité
        loadFavorites();
    }
}
