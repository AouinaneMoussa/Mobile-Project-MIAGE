package com.example.opendataproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // Find the TextView for the title
        TextView titleText = findViewById(R.id.titleText);

        // Fade-in animation
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f); // From fully transparent to visible
        fadeIn.setDuration(1500);  // Duration in milliseconds
        fadeIn.setStartOffset(500);  // Delay before the animation starts

        // Slide-in animation from the left
        TranslateAnimation slideIn = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,  // Start from outside the screen on the left
                Animation.RELATIVE_TO_PARENT, 0.0f,   // Move to the original position (center)
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        slideIn.setDuration(1500);  // Duration in milliseconds
        slideIn.setStartOffset(500);  // Delay before starting

        // Scale animation (slightly grows the text)
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.8f, 1.2f,  // Scale from 0.8 to 1.2 times its size
                0.8f, 1.2f,  // Same for both X and Y axes
                Animation.RELATIVE_TO_SELF, 0.5f,  // Pivot point is the center of the text
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1500);  // Duration in milliseconds
        scaleAnimation.setStartOffset(500);  // Delay before starting

        // Combine all animations into an AnimationSet
        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Start the activity after the animation ends
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                };
                handler.postDelayed(runnable, 7000);  // Wait a bit before transitioning
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        };

        // Create an AnimationSet to combine all animations
        android.view.animation.AnimationSet animationSet = new android.view.animation.AnimationSet(true);
        animationSet.addAnimation(fadeIn);
        animationSet.addAnimation(slideIn);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setAnimationListener(animationListener);

        // Start the combined animation on the titleText TextView
        titleText.startAnimation(animationSet);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(runnable);  // Clean up the handler
        }
    }
}
