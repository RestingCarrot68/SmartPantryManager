package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartInventory.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_INVENTORY = "inventory";
    public static final String TABLE_MEALS = "meals";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_EXPIRY_DATE = "expiry_date";

    public static final String COLUMN_MEAL_TITLE = "title";
    public static final String COLUMN_INGREDIENTS = "ingredients";
    public static final String COLUMN_STEPS = "steps";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_INVENTORY_TABLE = "CREATE TABLE " + TABLE_INVENTORY + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ITEM_NAME + " TEXT, "
                + COLUMN_CATEGORY + " TEXT, "
                + COLUMN_QUANTITY + " REAL, "
                + COLUMN_UNIT + " TEXT, "
                + COLUMN_EXPIRY_DATE + " TEXT)";

        String CREATE_MEALS_TABLE = "CREATE TABLE " + TABLE_MEALS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MEAL_TITLE + " TEXT, "
                + COLUMN_INGREDIENTS + " TEXT, "
                + COLUMN_STEPS + " TEXT)";

        db.execSQL(CREATE_INVENTORY_TABLE);
        db.execSQL(CREATE_MEALS_TABLE);

        seedMeals(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALS);
        onCreate(db);
    }

    private void seedMeals(SQLiteDatabase db) {
        insertMealSeed(db, "Avocado Toast", "bread:2:slices,avocado:1:pcs,salt:1:pinch", "Toast bread. Mash avocado with salt and spread on top.");
        insertMealSeed(db, "Classic Pancakes", "flour:1:cup,milk:1:cup,egg:1:pcs,sugar:1:tbsp", "Mix batter. Pour onto hot oiled skillet and flip when bubbling.");
        insertMealSeed(db, "Garlic Butter Rice", "rice:1:cup,garlic:2:clove,butter:1:tbsp", "Sauté chopped garlic in butter. Mix in cooked rice.");
        insertMealSeed(db, "Creamy Oats", "oats:1:cup,milk:2:cups,honey:1:tbsp", "Simmer oats in milk. Stir in honey before serving.");
        insertMealSeed(db, "Cheese Omelette", "egg:2:pcs,cheese:1:slice,butter:1:tbsp", "Whisk eggs. Melt butter, cook eggs, fold cheese inside.");
    }

    private void insertMealSeed(SQLiteDatabase db, String title, String ingredients, String steps) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MEAL_TITLE, title);
        cv.put(COLUMN_INGREDIENTS, ingredients);
        cv.put(COLUMN_STEPS, steps);
        db.insert(TABLE_MEALS, null, cv);
    }

    public boolean addInventoryItem(String name, String category, double qty, String unit, String expiry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ITEM_NAME, name);
        cv.put(COLUMN_CATEGORY, category);
        cv.put(COLUMN_QUANTITY, qty);
        cv.put(COLUMN_UNIT, unit);
        cv.put(COLUMN_EXPIRY_DATE, expiry);
        long result = db.insert(TABLE_INVENTORY, null, cv);
        return result != -1;
    }

    public boolean updateInventoryItem(int id, String name, String category, double qty, String unit, String expiry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ITEM_NAME, name);
        cv.put(COLUMN_CATEGORY, category);
        cv.put(COLUMN_QUANTITY, qty);
        cv.put(COLUMN_UNIT, unit);
        cv.put(COLUMN_EXPIRY_DATE, expiry);
        int result = db.update(TABLE_INVENTORY, cv, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deleteInventoryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_INVENTORY, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public Cursor getAllInventoryItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_INVENTORY, null);
    }

    public Cursor getMatchingMeals() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor inventoryCursor = db.rawQuery("SELECT * FROM " + TABLE_INVENTORY, null);
        java.util.List<InventoryModel> inventoryList = new java.util.ArrayList<>();
        if (inventoryCursor.moveToFirst()) {
            do {
                String name = inventoryCursor.getString(inventoryCursor.getColumnIndexOrThrow(COLUMN_ITEM_NAME)).trim().toLowerCase().replaceAll("s$", "");
                double qty = inventoryCursor.getDouble(inventoryCursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
                inventoryList.add(new InventoryModel(name, qty));
            } while (inventoryCursor.moveToNext());
        }
        inventoryCursor.close();

        Cursor mealCursor = db.rawQuery("SELECT * FROM " + TABLE_MEALS, null);
        java.util.List<Integer> matchedMealIds = new java.util.ArrayList<>();

        if (mealCursor.moveToFirst()) {
            do {
                int mealId = mealCursor.getInt(mealCursor.getColumnIndexOrThrow(COLUMN_ID));
                String ingredientsRaw = mealCursor.getString(mealCursor.getColumnIndexOrThrow(COLUMN_INGREDIENTS));

                String[] reqArray = ingredientsRaw.split(",");
                boolean mealFullyMatched = true;

                for (String reqStr : reqArray) {
                    String[] parts = reqStr.split(":");
                    if (parts.length < 2) continue;
                    String reqName = parts[0].trim().toLowerCase().replaceAll("s$", "");
                    double reqQty = Double.parseDouble(parts[1]);

                    boolean foundInInventory = false;
                    for (InventoryModel item : inventoryList) {
                        if (item.name.equals(reqName) && item.quantity >= reqQty) {
                            foundInInventory = true;
                            break;
                        }
                    }

                    if (!foundInInventory) {
                        mealFullyMatched = false;
                        break;
                    }
                }

                if (mealFullyMatched && reqArray.length > 0) {
                    matchedMealIds.add(mealId);
                }
            } while (mealCursor.moveToNext());
        }
        mealCursor.close();

        if (matchedMealIds.isEmpty()) return null;

        StringBuilder idsString = new StringBuilder();
        for (int i = 0; i < matchedMealIds.size(); i++) {
            idsString.append(matchedMealIds.get(i));
            if (i < matchedMealIds.size() - 1) idsString.append(",");
        }

        return db.rawQuery("SELECT * FROM " + TABLE_MEALS + " WHERE " + COLUMN_ID + " IN (" + idsString.toString() + ")", null);
    }

    public static class InventoryModel {
        public String name;
        public double quantity;
        public InventoryModel(String name, double quantity) {
            this.name = name;
            this.quantity = quantity;
        }
    }
}