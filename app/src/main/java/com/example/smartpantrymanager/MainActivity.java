package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_pantry) {
                startActivity(new Intent(MainActivity.this, PantryActivity.class));
                return true;
            } else if (itemId == R.id.nav_suggestions) {
                startActivity(new Intent(MainActivity.this, SuggestedRecipesActivity.class));
                return true;
            }
            return false;
        });
    }
}