package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    Button btnGoPantry, btnGoSuggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnGoPantry = findViewById(R.id.btnGoPantry);
        btnGoSuggestions = findViewById(R.id.btnGoSuggestions);

        // Manage My Pantry action button
        btnGoPantry.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, PantryActivity.class));
        });

        // View Suggested Recipes action button
        btnGoSuggestions.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, SuggestedRecipesActivity.class));
        });
    }
}