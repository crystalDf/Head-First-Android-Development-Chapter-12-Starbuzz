package com.star.starbuzz;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StarbuzzDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "starbuzz";
    private static final int DB_VERSION = 2;

    public static final String TABLE_DRINK = "DRINK";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_IMAGE_RESOURCE_ID = "IMAGE_RESOURCE_ID";
    public static final String COLUMN_FAVORITE = "FAVORITE";

    private static final String CREATE_TABLE_DRINK = "CREATE TABLE " + TABLE_DRINK + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_IMAGE_RESOURCE_ID + " INTEGER " + ");";

    private static final String ALTER_TABLE_DRINK = "ALTER TABLE " + TABLE_DRINK +
            " ADD COLUMN " + COLUMN_FAVORITE + " NUMERIC;";

    public StarbuzzDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        updateDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        updateDatabase(db, oldVersion, newVersion);
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL(CREATE_TABLE_DRINK);

            insertDrink(db, "Latte", "Espresso and steamed milk", R.drawable.latte);
            insertDrink(db, "Cappuccino", "Espresso, hot milk and steamed-milk foam",
                    R.drawable.cappuccino);
            insertDrink(db, "Filter", "Our best drip coffee", R.drawable.filter);
        }

        if (oldVersion < 2) {
            db.execSQL(ALTER_TABLE_DRINK);
        }
    }

    private static void insertDrink(SQLiteDatabase db, String name,
                                    String description, int imageResourceId) {

        ContentValues drinkValues = new ContentValues();

        drinkValues.put(COLUMN_NAME, name);
        drinkValues.put(COLUMN_DESCRIPTION, description);
        drinkValues.put(COLUMN_IMAGE_RESOURCE_ID, imageResourceId);

        db.insert(TABLE_DRINK, null, drinkValues);
    }
}
