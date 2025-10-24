package com.example.petcareexpress;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petcareexpress.objects.FoodData;

import java.util.ArrayList;
import java.util.List;

public class SelectItemActivity extends AppCompatActivity {
    private final String TAG = "SelectItemActivity";
    private DBHelper dbHelper;
    private Button cancelButton, selectItemButton;
    RecyclerView recyclerView;
    private final String[] columnsToTakeFromSQLite = {
            DBContract.FoodTable.COLUMN_BRAND_NAME,
            DBContract.FoodTable.COLUMN_FOOD_TYPE,
            DBContract.FoodTable.COLUMN_MIN_PET_WEIGHT,
            DBContract.FoodTable.COLUMN_MIN_FEED_AMOUNT,
            DBContract.FoodTable.COLUMN_FOR_DELETION
    };
    private FoodAdapter adapter;
    private List<FoodData> foodDataList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectitem_dialog);

        dbHelper = new DBHelper(this);
        foodDataList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FoodAdapter(foodDataList);
        recyclerView.setAdapter(adapter);

        cancelButton = findViewById(R.id.cancel_button);

        cancelButton.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });

        // Get foodDataList to populate RecyclerView.
        loadFoodItemsFromSQLite(foodDataList);
    }
    private void loadFoodItemsFromSQLite(List<FoodData> foodData) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DBContract.FoodTable.TABLE_NAME,
                columnsToTakeFromSQLite,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String foodName = cursor.getString(cursor.getColumnIndex(DBContract.FoodTable.COLUMN_BRAND_NAME));
            @SuppressLint("Range") String foodType = cursor.getString(cursor.getColumnIndex(DBContract.FoodTable.COLUMN_FOOD_TYPE));
            @SuppressLint("Range") double minimumPetWeight = cursor.getDouble(cursor.getColumnIndex(DBContract.FoodTable.COLUMN_MIN_PET_WEIGHT));
            @SuppressLint("Range") double minimumFeedAmount = cursor.getDouble(cursor.getColumnIndex(DBContract.FoodTable.COLUMN_MIN_FEED_AMOUNT));

            foodData.add(new FoodData(foodName, foodType, minimumPetWeight, minimumFeedAmount));
        }

        cursor.close();
        db.close();
    }
    protected void onDestroy(){
        super.onDestroy();
        dbHelper.close();
    }
}
