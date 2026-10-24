package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SuggestedRecipesActivity extends AppCompatActivity {

    DatabaseHelper db;
    RecyclerView recyclerView;
    LinearLayout layoutEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewSuggestions);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);

        loadSuggestions();
    }

    private void loadSuggestions() {
        Cursor cursor = db.getMatchingMeals();
        if (cursor == null || cursor.getCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);
            RecipeAdapter adapter = new RecipeAdapter(this, cursor);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }
    }
}