package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_PANTRY = "pantry";
    public static final String TABLE_RECIPES = "recipes";

    // Common Columns
    public static final String COLUMN_ID = "id";

    // Pantry Columns
    public static final String COLUMN_PANTRY_NAME = "name";
    public static final String COLUMN_PANTRY_QTY = "quantity";
    public static final String COLUMN_PANTRY_UNIT = "unit";
    public static final String COLUMN_PANTRY_EXPIRY = "expiry_date";

    // Recipe Columns
    public static final String COLUMN_RECIPE_TITLE = "title";
    public static final String COLUMN_RECIPE_INGREDIENTS = "ingredients"; // Formatted: name:qty:unit,name:qty:unit
    public static final String COLUMN_RECIPE_INSTRUCTIONS = "instructions";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PANTRY_TABLE = "CREATE TABLE " + TABLE_PANTRY + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PANTRY_NAME + " TEXT, "
                + COLUMN_PANTRY_QTY + " REAL, "
                + COLUMN_PANTRY_UNIT + " TEXT, "
                + COLUMN_PANTRY_EXPIRY + " TEXT)";

        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_RECIPE_TITLE + " TEXT, "
                + COLUMN_RECIPE_INGREDIENTS + " TEXT, "
                + COLUMN_RECIPE_INSTRUCTIONS + " TEXT)";

        db.execSQL(CREATE_PANTRY_TABLE);
        db.execSQL(CREATE_RECIPES_TABLE);

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    // Seed 15-20 Initial Recipes
    private void seedRecipes(SQLiteDatabase db) {
        insertRecipeSeed(db, "Simple Omelette", "egg:2:pcs,salt:1:pinch,butter:1:tbsp", "Whisk eggs. Melt butter in pan. Cook until set.");
        insertRecipeSeed(db, "Tomato Pasta", "pasta:200:g,tomato:2:pcs,garlic:1:clove,olive oil:1:tbsp", "Boil pasta. Fry garlic and tomatoes in oil. Mix together.");
        insertRecipeSeed(db, "Scrambled Eggs", "egg:3:pcs,butter:1:tbsp", "Melt butter, scramble eggs over medium heat.");
        insertRecipeSeed(db, "Garlic Rice", "rice:1:cup,garlic:2:clove,butter:1:tbsp", "Cook garlic in butter. Add boiled rice and stir.");
        insertRecipeSeed(db, "Boiled Eggs", "egg:2:pcs,water:2:cups", "Boil water. Cook eggs for 8 minutes.");
        insertRecipeSeed(db, "Butter Toast", "bread:2:slices,butter:1:tbsp", "Toast bread and spread butter evenly.");
        insertRecipeSeed(db, "Pancakes", "flour:1:cup,milk:1:cup,egg:1:pcs,butter:1:tbsp", "Mix ingredients into batter. Pour on hot pan and flip.");
        insertRecipeSeed(db, "Grilled Cheese", "bread:2:slices,cheese:1:slice,butter:1:tbsp", "Butter bread outside, place cheese inside, grill until golden.");
        insertRecipeSeed(db, "French Toast", "bread:2:slices,egg:1:pcs,milk:0.5:cup,butter:1:tbsp", "Dip bread in beaten egg and milk. Fry in butter.");
        insertRecipeSeed(db, "Mashed Potatoes", "potato:3:pcs,butter:2:tbsp,milk:0.25:cup", "Boil potatoes until soft. Mash with butter and milk.");
        insertRecipeSeed(db, "Potato Fries", "potato:2:pcs,oil:2:tbsp,salt:1:pinch", "Cut potatoes into strips. Deep fry in oil and season with salt.");
        insertRecipeSeed(db, "Garlic Butter Pasta", "pasta:150:g,garlic:2:clove,butter:2:tbsp", "Boil pasta. Sauté garlic in butter, pour over pasta.");
        insertRecipeSeed(db, "Warm Milk Drink", "milk:1:cup,sugar:1:tsp", "Heat milk in a saucepan and stir in sugar.");
        insertRecipeSeed(db, "Cheesy Scrambled Eggs", "egg:2:pcs,cheese:1:slice,butter:1:tbsp", "Melt butter, add eggs and cheese slice while scrambling.");
        insertRecipeSeed(db, "Tomato Salad", "tomato:3:pcs,olive oil:1:tbsp,salt:1:pinch", "Slice tomatoes, drizzle olive oil, and sprinkle salt.");
    }

    private void insertRecipeSeed(SQLiteDatabase db, String title, String ingredients, String instructions) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RECIPE_TITLE, title);
        cv.put(COLUMN_RECIPE_INGREDIENTS, ingredients);
        cv.put(COLUMN_RECIPE_INSTRUCTIONS, instructions);
        db.insert(TABLE_RECIPES, null, cv);
    }

    // CRUD Operations for Pantry
    public boolean addPantryItem(String name, double qty, String unit, String expiry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PANTRY_NAME, name);
        cv.put(COLUMN_PANTRY_QTY, qty);
        cv.put(COLUMN_PANTRY_UNIT, unit);
        cv.put(COLUMN_PANTRY_EXPIRY, expiry);
        long result = db.insert(TABLE_PANTRY, null, cv);
        return result != -1;
    }

    public boolean updatePantryItem(int id, String name, double qty, String unit, String expiry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PANTRY_NAME, name);
        cv.put(COLUMN_PANTRY_QTY, qty);
        cv.put(COLUMN_PANTRY_UNIT, unit);
        cv.put(COLUMN_PANTRY_EXPIRY, expiry);
        int result = db.update(TABLE_PANTRY, cv, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deletePantryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PANTRY, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public Cursor getAllPantryItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PANTRY, null);
    }

    // STRICT MATCHING ALGORITHM LOGIC
    public Cursor getSuggestedRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Fetch all pantry items into a structure
        Cursor pantryCursor = db.rawQuery("SELECT * FROM " + TABLE_PANTRY, null);
        List<PantryModel> pantryList = new ArrayList<>();
        if (pantryCursor.moveToFirst()) {
            do {
                String name = pantryCursor.getString(pantryCursor.getColumnIndexOrThrow(COLUMN_PANTRY_NAME)).trim().toLowerCase().replaceAll("s$", "");
                double qty = pantryCursor.getDouble(pantryCursor.getColumnIndexOrThrow(COLUMN_PANTRY_QTY));
                pantryList.add(new PantryModel(name, qty));
            } while (pantryCursor.moveToNext());
        }
        pantryCursor.close();

        // Fetch all recipes and test against pantry
        Cursor recipeCursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPES, null);
        List<Integer> matchedRecipeIds = new ArrayList<>();

        if (recipeCursor.moveToFirst()) {
            do {
                int recipeId = recipeCursor.getInt(recipeCursor.getColumnIndexOrThrow(COLUMN_ID));
                String ingredientsRaw = recipeCursor.getString(recipeCursor.getColumnIndexOrThrow(COLUMN_RECIPE_INGREDIENTS));

                String[] reqArray = ingredientsRaw.split(",");
                boolean recipeFullyMatched = true;

                for (String reqStr : reqArray) {
                    String[] parts = reqStr.split(":");
                    if (parts.length < 2) continue;
                    String reqName = parts[0].trim().toLowerCase().replaceAll("s$", "");
                    double reqQty = Double.parseDouble(parts[1]);

                    boolean foundInPantry = false;
                    for (PantryModel item : pantryList) {
                        if (item.name.equals(reqName) && item.quantity >= reqQty) {
                            foundInPantry = true;
                            break;
                        }
                    }

                    if (!foundInPantry) {
                        recipeFullyMatched = false;
                        break; // Exclude recipe strictly if even 1 ingredient is missing/insufficient
                    }
                }

                if (recipeFullyMatched && reqArray.length > 0) {
                    matchedRecipeIds.add(recipeId);
                }
            } while (recipeCursor.moveToNext());
        }
        recipeCursor.close();

        if (matchedRecipeIds.isEmpty()) {
            return null; // Signals zero matching recipes
        }

        // Return cursor with matching recipes
        StringBuilder idsString = new StringBuilder();
        for (int i = 0; i < matchedRecipeIds.size(); i++) {
            idsString.append(matchedRecipeIds.get(i));
            if (i < matchedRecipeIds.size() - 1) idsString.append(",");
        }

        return db.rawQuery("SELECT * FROM " + TABLE_RECIPES + " WHERE " + COLUMN_ID + " IN (" + idsString.toString() + ")", null);
    }

    // Simple inner helper model
    private static class PantryModel {
        String name;
        double quantity;
        PantryModel(String name, double quantity) {
            this.name = name;
            this.quantity = quantity;
        }
    }
}

