// CovidDataAdapter.java
package com.example.opendataproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CovidDataAdapter extends RecyclerView.Adapter<CovidDataAdapter.CovidDataViewHolder> {

    private final Context context;
    private final List<CovidData> covidDataList;

    private final Set<String> likedItems = new HashSet<>(); // Gérer les éléments "likés"

    private final Set<String> favoriteFipsSet;
    private final Runnable saveFavoritesCallback;

    public CovidDataAdapter(Context context, List<CovidData> covidDataList, Set<String> favoriteFipsSet, Runnable saveFavoritesCallback) {
        this.context = context;
        this.covidDataList = covidDataList;
        this.favoriteFipsSet = favoriteFipsSet;
        this.saveFavoritesCallback = saveFavoritesCallback;
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
        Picasso.get().load("https://mlyoasgpptk8.i.optimole.com/cb:PSMF~43db5/w:auto/h:auto/q:mauto/f:avif/https://www.iris-france.org/wp-content/uploads/2021/02/Coronavirus-monde-scaled.jpg").into(holder.iconImageView);

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
            intent.putExtra("image_url", "https://mlyoasgpptk8.i.optimole.com/cb:PSMF~43db5/w:auto/h:auto/q:mauto/f:avif/https://www.iris-france.org/wp-content/uploads/2021/02/Coronavirus-monde-scaled.jpg"); //image URL
            context.startActivity(intent);
        });

        // Gérer le clic sur le bouton "like"
        /*holder.likeButton.setOnClickListener(v -> {
            if (likedItems.contains(data.getFips())) {
                likedItems.remove(data.getFips());
                holder.likeButton.setColorFilter(context.getResources().getColor(R.color.grey)); // Icône grise
            } else {
                likedItems.add(data.getFips());
                holder.likeButton.setColorFilter(context.getResources().getColor(R.color.purple_200)); // Icône rouge
            }
        });*/

        holder.likeButton.setOnClickListener(v -> {
            if (likedItems.contains(data.getFips())) {
                likedItems.remove(data.getFips());
                favoriteFipsSet.remove(data.getFips()); // Supprimer des favoris
                holder.likeButton.setColorFilter(context.getResources().getColor(R.color.grey)); // Icône grise
            } else {
                likedItems.add(data.getFips());
                favoriteFipsSet.add(data.getFips()); // Ajouter aux favoris
                holder.likeButton.setColorFilter(context.getResources().getColor(R.color.purple_200)); // Icône rouge
            }
            saveFavoritesCallback.run(); // Enregistrer dans SharedPreferences
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
            likeButton = itemView.findViewById(R.id.like_button); // Bouton "like"

        }
    }
}