package com.example.petcareexpress;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petcareexpress.objects.FoodData;

import java.util.ArrayList;
import java.util.List;

// TODO: Delete once re-written to SelectItemActivity

public class SelectItemDialog extends DialogFragment {
    private final String TAG = "SelectItemDialog";
    private DBHelper dbHelper;
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
    @NonNull
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        View selectItemView = View.inflate(getActivity(), R.layout.selectitem_dialog, null);

        foodDataList = new ArrayList<>();

        recyclerView = selectItemView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FoodAdapter(foodDataList);
        recyclerView.setAdapter(adapter);

        dbHelper = new DBHelper(getContext());
        loadFoodItemsFromSQLite(foodDataList);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Select Food Item");

        builder.setView(selectItemView);
        builder.setPositiveButton("Select Item", (dialog, id) -> {
                    showShortToast("Feature not yet implemented.");
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    showShortToast("Canceled.");
                    dialog.dismiss();
                });

        return builder.create();
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
    private void showShortToast(String string) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
        }
    }
    public void onDismiss(@NonNull DialogInterface dialog){
        super.onDismiss(dialog);
        dbHelper.close();
    }
}
