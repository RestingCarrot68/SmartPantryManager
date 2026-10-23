package com.example.smartpantrymanager;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {

    EditText etName, etQty, etUnit, etExpiry;
    Button btnSave;
    DatabaseHelper db;
    int editId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        db = new DatabaseHelper(this);
        etName = findViewById(R.id.etName);
        etQty = findViewById(R.id.etQuantity);
        etUnit = findViewById(R.id.etUnit);
        etExpiry = findViewById(R.id.etExpiry);
        btnSave = findViewById(R.id.btnSave);

        if (getIntent().hasExtra("ID")) {
            editId = getIntent().getIntExtra("ID", -1);
            etName.setText(getIntent().getStringExtra("NAME"));
            etQty.setText(String.valueOf(getIntent().getDoubleExtra("QTY", 0)));
            etUnit.setText(getIntent().getStringExtra("UNIT"));
            etExpiry.setText(getIntent().getStringExtra("EXPIRY"));
        }

        btnSave.setOnClickListener(v -> saveItem());
    }

    private void saveItem() {
        String name = etName.getText().toString().trim();
        String qtyStr = etQty.getText().toString().trim();
        String unit = etUnit.getText().toString().trim();
        String expiry = etExpiry.getText().toString().trim();

        // Strict Form Input Validation
        if (TextUtils.isEmpty(name)) {
            etName.setError("Ingredient name is required");
            return;
        }
        if (TextUtils.isEmpty(qtyStr)) {
            etQty.setError("Quantity is required");
            return;
        }

        double qty;
        try {
            qty = Double.parseDouble(qtyStr);
            if (qty <= 0) {
                etQty.setError("Quantity must be greater than zero");
                return;
            }
        } catch (NumberFormatException e) {
            etQty.setError("Enter a valid number");
            return;
        }

        if (editId == -1) {
            db.addPantryItem(name, qty, unit, expiry);
            Toast.makeText(this, "Ingredient Added", Toast.LENGTH_SHORT).show();
        } else {
            db.updatePantryItem(editId, name, qty, unit, expiry);
            Toast.makeText(this, "Ingredient Updated", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}