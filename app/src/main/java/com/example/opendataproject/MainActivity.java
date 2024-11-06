package com.example.opendataproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL = "https://public.opendatasoft.com/api/explore/v2.1/catalog/datasets/";
    private static final String TAG = "COVID_APP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CovidDataInterface covidDataService = RetrofitClient.getRetrofitClient(BASE_URL).create(CovidDataInterface.class);

        Call<CovidResponse> call = covidDataService.getCovidData(50, 0); // Exemple : récupérer 50 résultats

        call.enqueue(new Callback<CovidResponse>() {
            @Override
            public void onResponse(Call<CovidResponse> call, Response<CovidResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CovidData> data = response.body().getResults();
                    // Afficher ou traiter les données ici
                    for (CovidData countyData : data) {
                        Log.d(TAG, "County: " + countyData.getAdmin2() + ", Confirmed Cases: " + countyData.getTotConfirmed());
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Response failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CovidResponse> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
