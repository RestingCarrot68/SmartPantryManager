package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PantryActivity extends AppCompatActivity {

    DatabaseHelper db;
    RecyclerView recyclerView;
    Button btnAdd, btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewPantry);
        btnAdd = findViewById(R.id.btnAddIngredient);
        btnSettings = findViewById(R.id.btnOpenSettings);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(PantryActivity.this, AddEditIngredientActivity.class);
            startActivity(intent);
        });

        // Open Settings screen when Settings button in Pantry header is clicked
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(PantryActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        loadPantryData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryData();
    }

    private void loadPantryData() {
        Cursor cursor = db.getAllInventoryItems();
        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "Pantry is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        PantryAdapter adapter = new PantryAdapter(this, cursor, db);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}