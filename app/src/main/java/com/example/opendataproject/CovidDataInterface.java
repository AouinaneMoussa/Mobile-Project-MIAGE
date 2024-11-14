package com.example.opendataproject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CovidDataInterface {

    @GET("coronavirus-covid-19-pandemic-usa-counties/records?") // Remplacez "your_api_endpoint" par l'URL de l'API après le domaine
    Call<CovidResponse> getCovidData(
            @Query("rows") int rows, // Paramètre pour limiter le nombre de résultats, si nécessaire
            @Query("start") int start // Paramètre pour la pagination, si nécessaire
    );
}
