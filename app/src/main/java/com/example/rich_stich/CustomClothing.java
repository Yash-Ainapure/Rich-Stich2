package com.example.rich_stich;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomClothing extends AppCompatActivity {

    private String selectedGender,selectedApparel;
    Button selectMaterial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_clothing);

        selectMaterial = findViewById(R.id.placeOrder);

        //spinner to select gender from user
        Spinner genderSpinner = findViewById(R.id.genderSpinner);
        Spinner apparelSpinner = findViewById(R.id.apparelSpinner);

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


        selectMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //place order
                String[] measurementsArray=getMeasurementsArray(selectedGender,selectedApparel);
                GenderAndApparelSelection obj=new GenderAndApparelSelection(selectedGender,selectedApparel,measurementsArray);
                Intent intent = new Intent(CustomClothing.this,MaterialSelection.class);
                intent.putExtra("genderAndApparel", obj);
                startActivity(intent);
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