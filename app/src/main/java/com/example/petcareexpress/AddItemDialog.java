package com.example.petcareexpress;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class AddItemDialog extends DialogFragment {
    private final String TAG = "Add Item Dialog";
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View addItemView = View.inflate(getActivity(), R.layout.additem_dialog, null);

        final EditText petFoodBrandInput = addItemView.findViewById(R.id.pet_food_brand_input);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Add Food Item");

        builder.setView(addItemView);

        builder.setPositiveButton("Add Item", (dialog, id) -> {
            String petFoodBrand = petFoodBrandInput.getText().toString();

            Log.d(TAG, "Received input: " + petFoodBrand);

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

