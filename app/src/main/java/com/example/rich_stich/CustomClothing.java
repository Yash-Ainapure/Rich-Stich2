package com.example.rich_stich;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CustomClothing extends AppCompatActivity {

    private String selectedGender,selectedApparel;
    List<String> enteredMeasurements = new ArrayList<>();
    Button getMeasurements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_clothing);


        getMeasurements = findViewById(R.id.measurementsBtn);
        //spinner to select gender from user
        Spinner genderSpinner = findViewById(R.id.genderSpinner);
        Spinner apparelSpinner = findViewById(R.id.apparelSpinner);
        Spinner materialSpinner = findViewById(R.id.materialSpinner);

        //gender selector adapter
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        //apparel selector
        ArrayAdapter<CharSequence> apparelAdapter = ArrayAdapter.createFromResource(this,
                R.array.apparel_options, android.R.layout.simple_spinner_item);
        apparelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        apparelSpinner.setAdapter(apparelAdapter);

        //material selector
        ArrayAdapter<CharSequence> materialAdapter = ArrayAdapter.createFromResource(this,
                R.array.material_options, android.R.layout.simple_spinner_item);
        materialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        materialSpinner.setAdapter(materialAdapter);


        //gender selector
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedGender = parentView.getItemAtPosition(position).toString();
                Toast.makeText(CustomClothing.this, "Selected Gender: " + selectedGender, Toast.LENGTH_SHORT).show();
                updateApparelOptions(parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        // Apparel spinner item selection listener
        apparelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedApparel = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });


        getMeasurements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMeasurementsInputDialog(selectedGender,selectedApparel);
            }
        });
    }


    private void updateApparelOptions(String selectedGender) {
        // You can implement logic here to update the apparel options based on gender
        // For simplicity, let's assume that for Male, we only have Shirt, and for Female, we have Dress
        Spinner apparelSpinner = findViewById(R.id.apparelSpinner);
        ArrayAdapter<CharSequence> apparelAdapter = ArrayAdapter.createFromResource(this,
                    (selectedGender.equals("Male")) ? R.array.male_apparel_options : R.array.female_apparel_options,
                android.R.layout.simple_spinner_item);
        apparelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        apparelSpinner.setAdapter(apparelAdapter);
    }

    private void showMeasurementsInputDialog(String selectedGender, String selectedApparel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Measurements");

        // Inflate the dynamic layout for measurements input
        View dialogView = getLayoutInflater().inflate(R.layout.measurements_input_dialog, null);
        LinearLayout measurementsLayout = dialogView.findViewById(R.id.measurementsLayout);

        // Clear existing views in the layout
        measurementsLayout.removeAllViews();

        // Add input fields dynamically based on selected gender and apparel
        String[] measurementsArray = getMeasurementsArray(selectedGender, selectedApparel);
        for (String measurement : measurementsArray) {
            EditText editText = new EditText(this);
            editText.setHint(measurement);  // Set hint to display the type of measurement
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            editText.setLayoutParams(layoutParams);
            measurementsLayout.addView(editText);
        }

        builder.setView(dialogView);


        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve measurements from dynamically created input fields
                List<String> enteredMeasurements = new ArrayList<>();
                int childCount = measurementsLayout.getChildCount();

                for (int i = 0; i < childCount; i++) {
                    View view = measurementsLayout.getChildAt(i);
                    if (view instanceof EditText) {
                        EditText editText = (EditText) view;
                        String measurementValue = editText.getText().toString().trim();

                        // Check if the measurement value is empty
                        if (measurementValue.isEmpty()) {
                            // Display an error message or handle the case where a measurement is not provided
                            Toast.makeText(CustomClothing.this, "Please fill in all measurement fields", Toast.LENGTH_SHORT).show();
                            return; // Stop further processing if any field is empty
                        }

                        // Add the measurement to the list
                        enteredMeasurements.add(measurementValue);
                    }
                }
                // Display all entered measurements in a Toast message
                displayMeasurementsToast(enteredMeasurements);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void displayMeasurementsToast(List<String> measurements) {
        StringBuilder message = new StringBuilder("Entered Measurements:\n");
        for (String measurement : measurements) {
            message.append(measurement).append("\n");
        }
        Toast.makeText(CustomClothing.this, message.toString(), Toast.LENGTH_LONG).show();
    }
    private String[] getMeasurementsArray(String selectedGender, String selectedApparel) {

        if (selectedGender.equals("Male")) {
            if (selectedApparel.equals("Shirt")) {
                return getResources().getStringArray(R.array.measurements_shirt_male);
            } else if (selectedApparel.equals("T-Shirt")) {
                return getResources().getStringArray(R.array.measurements_tshirt_male);
            } else if (selectedApparel.equals("Pant")) {
                return getResources().getStringArray(R.array.measurements_pant_male);
            } else if (selectedApparel.equals("Coat")) {
                return getResources().getStringArray(R.array.measurements_coat_male);
            } else if (selectedApparel.equals("Jeans")) {
                return getResources().getStringArray(R.array.measurements_jeans_male);
            } else if (selectedApparel.equals("Hoodie")) {
                return getResources().getStringArray(R.array.measurements_hoodie_male);
            } else if (selectedApparel.equals("Suit")) {
                return getResources().getStringArray(R.array.measurements_suit_male);
            }
        } else if (selectedGender.equals("Female")) {
            if (selectedApparel.equals("Dress")) {
                return getResources().getStringArray( R.array.measurements_dress_female);
            } else if (selectedApparel.equals("Skirt")) {
                return getResources().getStringArray( R.array.measurements_skirt_female);
            } else if (selectedApparel.equals("Leggings")) {
                return getResources().getStringArray(R.array.measurements_leggings_female);
            } else if (selectedApparel.equals("Coat")) {
                return getResources().getStringArray(R.array.measurements_coat_female);
            } else if (selectedApparel.equals("Blouse")) {
                return getResources().getStringArray(R.array.measurements_blouse_female);
            }
        }

        // Default: return an empty array or handle other cases as needed
        return new String[0];
    }

}