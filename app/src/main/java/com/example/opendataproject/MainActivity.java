package com.example.opendataproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL = "https://public.opendatasoft.com/api/explore/v2.1/catalog/datasets/";
    private static final String TAG = "COVID_APP";

    //private ListView covidDataListView;
    //private ArrayAdapter<String> arrayAdapter;
    //private List<String> covidDataList;

    private RecyclerView covidDataRecyclerView;
    private CovidDataAdapter covidDataAdapter;
    private List<CovidData> covidDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        covidDataRecyclerView = findViewById(R.id.covid_data_recycler_view);
        covidDataList = new ArrayList<>();


        //covidDataListView = findViewById(R.id.covid_data_list_view);
        //arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.list_item_text, covidDataList);
        //covidDataListView.setAdapter(arrayAdapter);

        // Configurer l'adapter personnalisé et le RecyclerView
        covidDataAdapter = new CovidDataAdapter(this, covidDataList);
        covidDataRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        covidDataRecyclerView.setAdapter(covidDataAdapter);

        CovidDataInterface covidDataService = RetrofitClient.getRetrofitClient(BASE_URL).create(CovidDataInterface.class);

        Call<CovidResponse> call = covidDataService.getCovidData(20, 0); // Exemple : récupérer 20 résultats

        call.enqueue(new Callback<CovidResponse>() {
            @Override
            public void onResponse(Call<CovidResponse> call, Response<CovidResponse> response) {
                /*if (response.isSuccessful() && response.body() != null) {
                    List<CovidData> data = response.body().getResults();
                    // Afficher ou traiter les données ici
                    for (CovidData countyData : data) {
                        // Affiche "County: [Nom du comté], Confirmed Cases: [Nombre]"
                        String displayText = "County: " + countyData.getAdmin2() + ", Confirmed Cases: " + countyData.getTotConfirmed() + ", Confirmed death:" + countyData.getTotDeath();
                        covidDataList.add(displayText);

                        Log.d(TAG, "County: " + countyData.getAdmin2() + ", Confirmed Cases: " + countyData.getTotConfirmed());
                    }
                    arrayAdapter.notifyDataSetChanged(); // Met à jour l'interface
                } else {
                    Toast.makeText(MainActivity.this, "Response failed!", Toast.LENGTH_SHORT).show();
                }*/
                if (response.isSuccessful() && response.body() != null) {
                    covidDataList.addAll(response.body().getResults());
                    covidDataAdapter.notifyDataSetChanged(); // Mettre à jour l'affichage
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
