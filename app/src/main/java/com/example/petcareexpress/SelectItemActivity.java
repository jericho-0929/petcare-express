package com.example.petcareexpress;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
    private Button cancelButton, selectItemButton, deleteItemButton;
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
        adapter = new FoodAdapter(foodDataList, recyclerView);
        recyclerView.setAdapter(adapter);

        cancelButton = findViewById(R.id.cancel_button);
        deleteItemButton = findViewById(R.id.delete_item_button);
        selectItemButton = findViewById(R.id.select_item_button);

        cancelButton.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
        deleteItemButton.setOnClickListener(v -> {
            if (adapter.getCurrentFoodData() == null) {
                Toast.makeText(getBaseContext(), "No item selected!", Toast.LENGTH_SHORT).show();
            } else {
                // Pass foodData to method to handle deletion.
                showConfirmationDialog(adapter.getCurrentFoodData());
            }
        });
        selectItemButton.setOnClickListener(v -> {
            if (adapter.getCurrentFoodData() == null) {
                Toast.makeText(getBaseContext(), "No item selected!", Toast.LENGTH_SHORT).show();
            } else {
                Intent data = new Intent();
                data.putExtra("selectedFoodData", adapter.getCurrentFoodData());
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });

        // Get foodDataList to populate RecyclerView.
        loadFoodItemsFromSQLite(foodDataList);
    }
    private void loadFoodItemsFromSQLite(List<FoodData> foodDataList) {
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

            foodDataList.add(new FoodData(foodName, foodType, minimumPetWeight, minimumFeedAmount));
        }

        cursor.close();
        db.close();
    }
    private void deleteItemFromSQLite(String foodName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(DBContract.FoodTable.TABLE_NAME, DBContract.FoodTable.COLUMN_BRAND_NAME + "=?", new String[] {foodName});
        db.close();

        foodDataList.remove(adapter.getCurrentIndex());

        adapter.notifyItemRemoved(adapter.getCurrentIndex());
    }
    private void showConfirmationDialog(FoodData foodData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete " + foodData.name + "?");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            Toast.makeText(getBaseContext(), foodData.name + " deleted", Toast.LENGTH_SHORT).show();
            deleteItemFromSQLite(foodData.name);
            dialog.dismiss();
        });
        builder.setNegativeButton("No", (dialog, id) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    protected void onDestroy(){
        super.onDestroy();
        dbHelper.close();
    }
}
