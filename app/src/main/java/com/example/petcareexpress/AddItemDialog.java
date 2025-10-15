package com.example.petcareexpress;

import android.app.AlertDialog;
import android.app.Dialog;
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
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View addItemView = View.inflate(getActivity(), R.layout.additem_dialog, null);

        final EditText petFoodBrandInput = addItemView.findViewById(R.id.pet_food_brand_input);
        final EditText minimumPetWeightInput = addItemView.findViewById(R.id.minimum_pet_weight_input);
        final EditText minimumFeedAmountInput = addItemView.findViewById(R.id.minimum_feed_amount_input);

        final RadioButton wetRadioButton = addItemView.findViewById(R.id.wet_food_radio_select);
        final RadioButton dryRadioButton = addItemView.findViewById(R.id.dry_food_radio_select);

        AtomicReference<String> petFoodType = new AtomicReference<>();

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

            // Debugging logs.
            // TODO: Disable before push to production.
            Log.d(TAG, "Received input for petFoodBrand: " + petFoodBrand);
            Log.d(TAG, "Received input for petFoodType: " + petFoodType);
            Log.d(TAG, "Received input for minimumPetWeight: " + minimumPetWeight);
            Log.d(TAG, "Received input for minimumFeedAmount: " + minimumFeedAmount);


            // TODO: Implement database insertion/edit.
            showShortToast("Feature not yet implemented.");
            dialog.dismiss();
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
}

