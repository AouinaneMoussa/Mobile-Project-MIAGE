// CovidDataAdapter.java
package com.example.opendataproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CovidDataAdapter extends RecyclerView.Adapter<CovidDataAdapter.CovidDataViewHolder> {

    private final Context context;
    private final List<CovidData> covidDataList;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public CovidDataAdapter(Context context, List<CovidData> covidDataList) {
        this.context = context;
        this.covidDataList = covidDataList;

        // Initialize SharedPreferences
        sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @NonNull
    @Override
    public CovidDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.covid_list_item, parent, false);
        return new CovidDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CovidDataViewHolder holder, int position) {
        CovidData data = covidDataList.get(position);

        // Set data
        holder.countyTextView.setText("County: " + data.getAdmin2());
        holder.stateTextView.setText("State: " + data.getProvinceState());
        holder.casesTextView.setText("Cases: " + data.getTotConfirmed() + ", Deaths: " + data.getTotDeath());

        // Set like button state
        String fips = data.getFips();
        boolean isFavorite = sharedPreferences.getBoolean(fips, false);
        holder.likeButton.setColorFilter(isFavorite ? context.getResources().getColor(R.color.red) : context.getResources().getColor(R.color.grey));

        // Set click listener for the like button
        holder.likeButton.setOnClickListener(v -> {
            boolean currentlyLiked = sharedPreferences.getBoolean(fips, false);

            if (currentlyLiked) {
                // Remove from favorites
                editor.remove(fips);
                holder.likeButton.setColorFilter(context.getResources().getColor(R.color.grey));
                //Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show();
            } else {
                // Add to favorites
                editor.putBoolean(fips, true);
                holder.likeButton.setColorFilter(context.getResources().getColor(R.color.red));
                //Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();
            }

            editor.apply();
        });

        // Set the image using Picasso
        Picasso.get()
                .load("https://mlyoasgpptk8.i.optimole.com/cb:PSMF~43db5/w:auto/h:auto/q:mauto/f:avif/https://www.iris-france.org/wp-content/uploads/2021/02/Coronavirus-monde-scaled.jpg")
                .into(holder.iconImageView);

        // Item click listener to show details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("fips", fips);
            intent.putExtra("province_state", data.getProvinceState());
            intent.putExtra("county_name", data.getAdmin2());
            intent.putExtra("date", data.getDate());
            intent.putExtra("confirmed_cases", data.getTotConfirmed());
            intent.putExtra("deaths", data.getTotDeath());
            intent.putExtra("latitude", data.getLocation().getLat());
            intent.putExtra("longitude", data.getLocation().getLon());
            intent.putExtra("image_url", "https://mlyoasgpptk8.i.optimole.com/cb:PSMF~43db5/w:auto/h:auto/q:mauto/f:avif/https://www.iris-france.org/wp-content/uploads/2021/02/Coronavirus-monde-scaled.jpg");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return covidDataList.size();
    }

    public static class CovidDataViewHolder extends RecyclerView.ViewHolder {
        TextView countyTextView, stateTextView, casesTextView;
        ImageView iconImageView;
        ImageButton likeButton;

        public CovidDataViewHolder(@NonNull View itemView) {
            super(itemView);
            countyTextView = itemView.findViewById(R.id.county_name);
            stateTextView = itemView.findViewById(R.id.state_name);
            casesTextView = itemView.findViewById(R.id.covid_data);
            iconImageView = itemView.findViewById(R.id.icon);
            likeButton = itemView.findViewById(R.id.like_button);
        }
    }
}
