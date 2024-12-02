// CovidDataAdapter.java
package com.example.opendataproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CovidDataAdapter extends RecyclerView.Adapter<CovidDataAdapter.CovidDataViewHolder> {
    private final Context context;
    private final List<CovidData> covidDataList;

    public CovidDataAdapter(Context context, List<CovidData> covidDataList) {
        this.context = context;
        this.covidDataList = covidDataList;
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

        // Bind county and state to the TextViews
        holder.countyTextView.setText("County: " + data.getAdmin2());
        holder.stateTextView.setText("State: " + data.getProvinceState()); // Adding the state information
        holder.casesTextView.setText("Cases: " + data.getTotConfirmed() + ", Deaths: " + data.getTotDeath());

        // Using Picasso to load a placeholder image for demonstration
        Picasso.get().load("https://static.wikia.nocookie.net/psychology/images/c/c3/WHO_logo.gif/revision/latest?cb=20060221154725").into(holder.iconImageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("fips", data.getFips());
            intent.putExtra("province_state", data.getProvinceState());
            intent.putExtra("county_name", data.getAdmin2());
            intent.putExtra("date", data.getDate());
            intent.putExtra("confirmed_cases", data.getTotConfirmed());
            intent.putExtra("deaths", data.getTotDeath());
            intent.putExtra("latitude", data.getLocation().getLat());
            intent.putExtra("longitude", data.getLocation().getLon());
            intent.putExtra("image_url", "https://static.wikia.nocookie.net/psychology/images/c/c3/WHO_logo.gif/revision/latest?cb=20060221154725"); // Replace with actual image URL
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

        public CovidDataViewHolder(@NonNull View itemView) {
            super(itemView);
            countyTextView = itemView.findViewById(R.id.county_name);
            stateTextView = itemView.findViewById(R.id.state_name); // New TextView for the state
            casesTextView = itemView.findViewById(R.id.covid_data);
            iconImageView = itemView.findViewById(R.id.icon);
        }
    }
}