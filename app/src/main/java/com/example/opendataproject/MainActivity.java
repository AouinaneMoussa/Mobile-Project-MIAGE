package com.example.opendataproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://public.opendatasoft.com/api/explore/v2.1/catalog/datasets/";
    private static final int LIMIT = 20;

    private Spinner provinceSpinner;
    private RecyclerView covidDataRecyclerView;
    private Button viewMapButton;
    private CovidDataAdapter covidDataAdapter;
    private List<CovidData> covidDataList = new ArrayList<>();
    private String selectedProvince = "All Provinces"; // Default to "All Provinces"
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        provinceSpinner = findViewById(R.id.province_spinner);
        covidDataRecyclerView = findViewById(R.id.covid_data_recycler_view);
        viewMapButton = findViewById(R.id.view_map_button);

        // Setup RecyclerView
        covidDataAdapter = new CovidDataAdapter(this, covidDataList);
        covidDataRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        covidDataRecyclerView.setAdapter(covidDataAdapter);

        // Setup Spinner
        configureProvinceSpinner();

        // Setup Button
        // Start MapsActivity to display the data on the map
        findViewById(R.id.view_map_button).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.putParcelableArrayListExtra("covidDataList", new ArrayList<>(covidDataList));
            startActivity(intent);
        });

        // Load initial data (showing all provinces)
        fetchCovidData();

        // Set up infinite scroll (pagination)
        covidDataRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1)) {  // Check if we're at the bottom
                    fetchCovidData();
                }
            }
        });
    }

    private void configureProvinceSpinner() {
        List<String> provinces = new ArrayList<>();
        provinces.add("All Provinces");
        provinces.add("New York");
        provinces.add("California");
        provinces.add("Texas");
        provinces.add("Florida"); // Add more provinces as needed

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinces);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setAdapter(spinnerAdapter);

        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProvince = provinces.get(position);
                covidDataList.clear(); // Clear the list for the new selection
                fetchCovidData(); // Reload the data based on the selected province
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed here
            }
        });
    }

    private void fetchCovidData() {
        if (isLoading) return;
        isLoading = true;

        // Define the 'where' clause for the API request
        String where = selectedProvince.equals("All Provinces") ? null : "province_state='" + selectedProvince + "'";

        // Create a Retrofit client and make the request
        CovidDataInterface covidDataService = RetrofitClient.getRetrofitClient(BASE_URL).create(CovidDataInterface.class);
        Call<CovidResponse> call = covidDataService.getCovidData(LIMIT, covidDataList.size(), where); // Use pagination (rows and start)

        call.enqueue(new Callback<CovidResponse>() {
            @Override
            public void onResponse(@NonNull Call<CovidResponse> call, @NonNull Response<CovidResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CovidData> data = response.body().getResults();
                    covidDataList.addAll(data);  // Add new data to the existing list
                    covidDataAdapter.notifyDataSetChanged();  // Notify the adapter to update the RecyclerView
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load data!", Toast.LENGTH_SHORT).show();
                }
                isLoading = false;
            }

            @Override
            public void onFailure(@NonNull Call<CovidResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                isLoading = false;
            }
        });
    }
}
