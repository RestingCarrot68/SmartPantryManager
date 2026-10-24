package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    Switch switchNotifications;
    Button btnClearData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchNotifications = findViewById(R.id.switchNotifications);
        btnClearData = findViewById(R.id.btnClearData);

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "Expiration notifications enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Expiration notifications disabled", Toast.LENGTH_SHORT).show();
            }
        });

        btnClearData.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Clear All Data")
                    .setMessage("Are you sure you want to clear your entire pantry inventory?")
                    .setPositiveButton("Clear All", (dialog, which) -> {
                        DatabaseHelper db = new DatabaseHelper(this);
                        db.getWritableDatabase().execSQL("DELETE FROM " + DatabaseHelper.TABLE_INVENTORY);
                        Toast.makeText(this, "All inventory items cleared", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }
}