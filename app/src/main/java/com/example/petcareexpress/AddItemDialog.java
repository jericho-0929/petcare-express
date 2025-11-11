package com.example.petcareexpress;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.concurrent.atomic.AtomicReference;

public class AddItemDialog extends DialogFragment {
    private final String TAG = "Add Item Dialog";
    private DBHelper dbHelper;
    private Context context;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View addItemView = View.inflate(getActivity(), R.layout.activity_add_item, null);
        // Get ItemViews
        // EditTexts
        final EditText petFoodBrandInput = addItemView.findViewById(R.id.pet_food_brand_input);
        final EditText minimumPetWeightInput = addItemView.findViewById(R.id.minimum_pet_weight_input);
        final EditText minimumFeedAmountInput = addItemView.findViewById(R.id.minimum_feed_amount_input);
        // RadioButton
        final RadioButton wetRadioButton = addItemView.findViewById(R.id.wet_food_radio_select);
        final RadioButton dryRadioButton = addItemView.findViewById(R.id.dry_food_radio_select);
        // Handles petFoodType string value from RadioButtons
        AtomicReference<String> petFoodType = new AtomicReference<>();

        // TODO: Change to use MainActivity's dbHelper.
        dbHelper = new DBHelper(getContext());
        context = getContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Add Food Item");

        wetRadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            petFoodType.set("Wet");
        });
        dryRadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            petFoodType.set("Dry");
        });

        builder.setView(addItemView);

        builder.setPositiveButton("Add Item", (dialog, id) -> {
            String petFoodBrand = petFoodBrandInput.getText().toString();
            String minimumPetWeight = minimumPetWeightInput.getText().toString();
            String minimumFeedAmount = minimumFeedAmountInput.getText().toString();

            if (!petFoodBrand.isEmpty() && !petFoodType.toString().isEmpty() && !minimumPetWeight.isEmpty() && !minimumFeedAmount.isEmpty()) {
                if (checkIfRowExists(petFoodBrand)) {
                    showUpdateDialog(petFoodBrand, petFoodType.toString(), Double.parseDouble(minimumPetWeight), Double.parseDouble(minimumFeedAmount));
                }
                else if (insertRow(petFoodBrand, petFoodType.toString(), Double.parseDouble(minimumPetWeight), Double.parseDouble(minimumFeedAmount))) {
                    showShortToast("Added entry for: " + petFoodBrand);
                } else {
                    showShortToast("Add entry failed.");
                }

                dbHelper.close();
                dialog.dismiss();
            }

            // Should only fire if the preceding if-statement is not triggered.
            showShortToast("One or more entries are empty!");
        })
        .setNegativeButton("Cancel", (dialog, id) -> {
           showShortToast("Canceled.");
           dialog.dismiss();
        });
        return builder.create();
    }
    private void showShortToast(String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
    private boolean insertRow(String petFoodBrand, String petFoodType, double minimumPetWeight, double minimumFeedAmount) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues insertionRow = new ContentValues();
        insertionRow.put(DBContract.FoodTable.COLUMN_BRAND_NAME, petFoodBrand);
        insertionRow.put(DBContract.FoodTable.COLUMN_FOOD_TYPE, petFoodType);
        insertionRow.put(DBContract.FoodTable.COLUMN_MIN_PET_WEIGHT, minimumPetWeight);
        insertionRow.put(DBContract.FoodTable.COLUMN_MIN_FEED_AMOUNT, minimumFeedAmount);

        long newRowId = db.insertWithOnConflict(
                DBContract.FoodTable.TABLE_NAME,
                null,
                insertionRow,
                SQLiteDatabase.CONFLICT_REPLACE);
        if (newRowId < 0) {
            Log.d(TAG, "Row insertion failed.");
            return false;
        }
        return true;
    }
    private boolean checkIfRowExists(String petFoodName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DBContract.FoodTable.TABLE_NAME,
                new String[] {DBContract.FoodTable.COLUMN_ID}, // Not used.
                DBContract.FoodTable.COLUMN_BRAND_NAME + "=?",
                new String[] {petFoodName},
                null,
                null,
                null
        );

        boolean returnBool = cursor.moveToFirst();

        cursor.close();
        db.close();

        return returnBool;
    }
    private int updateRow(String petFoodBrand, String petFoodType, double minimumPetWeight, double minimumFeedAmount) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.FoodTable.COLUMN_FOOD_TYPE, petFoodType);
        contentValues.put(DBContract.FoodTable.COLUMN_MIN_PET_WEIGHT, minimumPetWeight);
        contentValues.put(DBContract.FoodTable.COLUMN_MIN_FEED_AMOUNT, minimumFeedAmount);

        int rowsAffected = db.update(
                DBContract.FoodTable.TABLE_NAME,
                contentValues,
                DBContract.FoodTable.COLUMN_BRAND_NAME + "=?",
                new String[] {petFoodBrand}
        );

        db.close();

        return rowsAffected;
    }
    private void showUpdateDialog(String petFoodBrand, String petFoodType, double minimumPetWeight, double minimumFeedAmount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Update Item");
        builder.setMessage(petFoodBrand + " already exists! Update instead?");
        builder.setPositiveButton("Yes", (dialog, id) -> {
           if (updateRow(petFoodBrand, petFoodType, minimumPetWeight, minimumFeedAmount) > 0) {
               showShortToast("Updated item: " + petFoodBrand);
           } else {
               showShortToast("Error: Item not found!");
           }
           dialog.dismiss();
        });
        builder.setNegativeButton("No", (dialog, id) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void onDismiss(@NonNull DialogInterface dialog){
        Log.d(TAG, "Dialog dismissed.");

        super.onDismiss(dialog);
        dbHelper.close();
    }
}

