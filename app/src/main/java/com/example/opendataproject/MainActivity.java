package com.example.opendataproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
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

    private RecyclerView covidDataRecyclerView;
    private CovidDataAdapter covidDataAdapter;
    private List<CovidData> covidDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        covidDataRecyclerView = findViewById(R.id.covid_data_recycler_view);
        covidDataList = new ArrayList<>();

        covidDataAdapter = new CovidDataAdapter(this, covidDataList);
        covidDataRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        covidDataRecyclerView.setAdapter(covidDataAdapter);

        fetchCovidData();

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
            @Override
            public void onResponse(Call<CovidResponse> call, Response<CovidResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CovidData> allData = response.body().getResults();
                    // Filter out only data with confirmed cases
                    for (CovidData data : allData) {
                        if (data.getTotConfirmed() > 0) { // Only add data with cases > 0
                            covidDataList.add(data);
                        }
                        if (covidDataList.size() >= 20) break; // Limit to 20 entries
                    }
                    covidDataAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Response failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CovidResponse> call, Throwable t) {
                Log.e(TAG, "Error: moussa " + t.getMessage());
                Toast.makeText(MainActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
