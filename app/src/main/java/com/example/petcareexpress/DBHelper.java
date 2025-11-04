package com.example.petcareexpress;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

// Handles SQLite creation and updates.
public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    private static final String DATABASE_NAME = "petcare.db";
    private static final String SQL_CREATE_INVENTORY_TABLE =
            "CREATE TABLE IF NOT EXISTS " + DBContract.FoodTable.TABLE_NAME + " (" +
                    DBContract.FoodTable.COLUMN_ID + " INTEGER PRIMARY KEY, " + // Autoincrement not needed, reusing deleted indices is preferred.
                    DBContract.FoodTable.COLUMN_BRAND_NAME + " TEXT NOT NULL, " +
                    DBContract.FoodTable.COLUMN_FOOD_TYPE + " TEXT NOT NULL, " +
                    DBContract.FoodTable.COLUMN_MIN_PET_WEIGHT + " REAL NOT NULL, " +
                    DBContract.FoodTable.COLUMN_MIN_FEED_AMOUNT + " REAL NOT NULL, " +
                    DBContract.FoodTable.COLUMN_FOR_DELETION + " INTEGER DEFAULT 0" +
                    ");";
    // Expected behavior for below: Should not fire if there is already a Generic entry.
    private static final String SQL_INSERT_DEFAULT_ROW =
            "INSERT OR IGNORE INTO " + DBContract.FoodTable.TABLE_NAME + " (" +
            DBContract.FoodTable.COLUMN_BRAND_NAME + ", " +
            DBContract.FoodTable.COLUMN_FOOD_TYPE + ", " +
            DBContract.FoodTable.COLUMN_MIN_PET_WEIGHT + ", " +
            DBContract.FoodTable.COLUMN_MIN_FEED_AMOUNT + ") VALUES (" +
            "'Generic Cat Dry Food', " +
            "'Dry Food', " +
            + 2.0 + ", " +
            + 40 + ");";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
        db.execSQL(SQL_INSERT_DEFAULT_ROW);

        Log.d(TAG, "onCreate() finished.");
    }
    public Cursor getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                DBContract.FoodTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.FoodTable.TABLE_NAME);
        onCreate(db);

        Log.d(TAG, "onUpgrade() executed.");
    }
}
