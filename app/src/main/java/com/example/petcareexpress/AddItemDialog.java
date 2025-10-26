package com.example.petcareexpress;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
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
    private Cursor dbCursor;

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
            double minimumPetWeight = Double.parseDouble(minimumPetWeightInput.getText().toString());
            double minimumFeedAmount = Double.parseDouble(minimumFeedAmountInput.getText().toString());

            if (insertRow(petFoodBrand, petFoodType.toString(), minimumPetWeight, minimumFeedAmount)) {
                showShortToast("Added entry for: " + petFoodBrand);

                dbHelper.close();
                dialog.dismiss();
            } else {
                showShortToast("Add entry failed.");
            }
        })
        .setNegativeButton("Cancel", (dialog, id) -> {
           showShortToast("Canceled.");
           dialog.dismiss();
        });
        return builder.create();
    }
    private void showShortToast(String string) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
        }
    }
    private boolean insertRow(String petFoodBrand, String petFoodType, double minimumPetWeight, double minimumFeedAmount) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();

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
    public void onDismiss(@NonNull DialogInterface dialog){
        Log.d(TAG, "Dialog dismissed.");

        super.onDismiss(dialog);
        dbHelper.close();
    }
}

