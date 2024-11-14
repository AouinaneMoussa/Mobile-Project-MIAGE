package com.example.opendataproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://public.opendatasoft.com/api/explore/v2.1/catalog/datasets/";
    private static final String TAG = "COVID_APP";

    private CovidDataAdapter covidDataAdapter;
    private List<CovidData> covidDataList;

    // pagination
    private static final int LIMIT = 20;
    private boolean isLoading = false; // Prevent multiple API calls at once

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView covidDataRecyclerView = findViewById(R.id.covid_data_recycler_view);
        covidDataList = new ArrayList<>();

        covidDataAdapter = new CovidDataAdapter(this, covidDataList);
        covidDataRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        covidDataRecyclerView.setAdapter(covidDataAdapter);

        fetchCovidData(); // Initial data load

        // Set up scroll listener for pagination
        covidDataRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                // Check if we've reached the end of the list
                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == covidDataList.size() - 1) {
                    if (!isLoading) {
                        isLoading = true;
                        fetchCovidData();
                    }
                }
            }
        });

        // Start MapsActivity to display the data on the map
        findViewById(R.id.view_map_button).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.putParcelableArrayListExtra("covidDataList", new ArrayList<>(covidDataList));
            startActivity(intent);
        });
    }

    private void fetchCovidData() {
        CovidDataInterface covidDataService = RetrofitClient.getRetrofitClient(BASE_URL).create(CovidDataInterface.class);
        Call<CovidResponse> call = covidDataService.getCovidData(20, 0);

        call.enqueue(new Callback<CovidResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<CovidResponse> call, @NonNull Response<CovidResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CovidData> allData = response.body().getResults();

                    // Filter only data with confirmed cases
                    for (CovidData data : allData) {
                        if (data.getTotConfirmed() > 0) {
                            covidDataList.add(data);
                        }
                    }
                    covidDataAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Response failed!", Toast.LENGTH_SHORT).show();
                }
                isLoading = false; // Reset loading status
            }

            @Override
            public void onFailure(@NonNull Call<CovidResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Error: moussa " + t.getMessage());
                Toast.makeText(MainActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
                isLoading = false;
            }
        });
    }
}
