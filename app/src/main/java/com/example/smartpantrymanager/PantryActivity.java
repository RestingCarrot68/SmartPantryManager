package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PantryActivity extends AppCompatActivity {

    DatabaseHelper db;
    RecyclerView recyclerView;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewPantry);
        btnAdd = findViewById(R.id.btnAddIngredient);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(PantryActivity.this, AddEditIngredientActivity.class);
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
        Cursor cursor = db.getAllPantryItems();
        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "Pantry is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        // Custom Adapter binds cursor data to RecyclerView
        PantryAdapter adapter = new PantryAdapter(this, cursor, db);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}