package com.example.opendataproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView detailTextView = findViewById(R.id.detail_text);
        ImageView detailImageView = findViewById(R.id.detail_image);

        Intent intent = getIntent();
        String fips = intent.getStringExtra("fips");
        String provinceState = intent.getStringExtra("province_state");
        String countyName = intent.getStringExtra("county_name");
        String date = intent.getStringExtra("date");
        int confirmedCases = intent.getIntExtra("confirmed_cases", 0);
        int deaths = intent.getIntExtra("deaths", 0);
        float latitude = intent.getFloatExtra("latitude", 0f);
        float longitude = intent.getFloatExtra("longitude", 0f);
        String imageUrl = intent.getStringExtra("image_url");

        String details = "FIPS: " + fips + "\n"
                + "Province/State: " + provinceState + "\n"
                + "County: " + countyName + "\n"
                + "Date: " + date + "\n"
                + "Confirmed Cases: " + confirmedCases + "\n"
                + "Deaths: " + deaths + "\n"
                + "Location: " + latitude + ", " + longitude;

        detailTextView.setText(details);
        Picasso.get().load(imageUrl).into(detailImageView);
    }
}