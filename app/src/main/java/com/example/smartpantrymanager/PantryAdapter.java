package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private Context context;
    private Cursor cursor;
    private DatabaseHelper db;

    public PantryAdapter(Context context, Cursor cursor, DatabaseHelper db) {
        this.context = context;
        this.cursor = cursor;
        this.db = db;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) return;

        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_NAME));
        String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY));
        double qty = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUANTITY));
        String unit = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UNIT));
        String expiry = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EXPIRY_DATE));

        holder.tvName.setText(name + " [" + category + "] - " + qty + " " + unit);
        holder.tvDetails.setText("Expires: " + expiry + " (Hold to delete)");

        // Single Click: Edit item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditIngredientActivity.class);
            intent.putExtra("ID", id);
            intent.putExtra("NAME", name);
            intent.putExtra("CATEGORY", category);
            intent.putExtra("QTY", qty);
            intent.putExtra("UNIT", unit);
            intent.putExtra("EXPIRY", expiry);
            context.startActivity(intent);
        });

        // Long Click: Delete item confirmation dialog
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete " + name + " from your inventory?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        boolean deleted = db.deleteInventoryItem(id);
                        if (deleted) {
                            Toast.makeText(context, name + " deleted", Toast.LENGTH_SHORT).show();
                            // Refresh activity data
                            if (context instanceof PantryActivity) {
                                ((PantryActivity) context).onResume();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDetails;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(android.R.id.text1);
            tvDetails = itemView.findViewById(android.R.id.text2);
        }
    }
}