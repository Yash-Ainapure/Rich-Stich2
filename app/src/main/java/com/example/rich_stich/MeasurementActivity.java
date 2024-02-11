package com.example.rich_stich;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MeasurementActivity extends AppCompatActivity {

    Button measurementsBtn;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Handle the back button
            case android.R.id.home:
                onBackPressed(); // This will call the default back button behavior
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set your desired icon for the navigation drawer toggle
        }
        getSupportActionBar().setTitle("Measurements");
        Intent intent = getIntent();
        String material=(String) intent.getSerializableExtra("selectedMaterial");
        GenderAndApparelSelection obj=(GenderAndApparelSelection) intent.getSerializableExtra("genderAndApparel");
        String[] measurements=obj.getMeasurements();
        String price=(String) intent.getSerializableExtra("materialPrice");

        LinearLayout layout = findViewById(R.id.linearLayout);

        ArrayList<String> hintsList = new ArrayList<>();
        ArrayList<String> measurementsList = new ArrayList<>();

        measurementsBtn = findViewById(R.id.getMeasurements);

        measurementsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                measurementsList.clear();
                for (int i = 0; i < measurements.length; i++) {
                    EditText editText = findViewById(layout.getChildAt(i).getId());

                    String measurementValue = editText.getText().toString();
                    measurementsList.add(measurementValue);
                }

                Intent intent = new Intent(MeasurementActivity.this, Payment.class);
                intent.putExtra("selectedMaterial", material);
                intent.putExtra("genderAndApparel", obj);
                intent.putExtra("measurements", measurementsList);
                intent.putExtra("hints", hintsList);
                intent.putExtra("materialPrice", price);
                startActivity(intent);
            }
        });


        //display edit texts of measurements
        for (String measurement : measurements) {
            EditText editText = new EditText(this);
            editText.setBackgroundResource(R.drawable.custom_edit_bg);
            editText.setPadding(50, 50, 50, 50);
            editText.setTextColor(getResources().getColor(R.color.black));

// Set layout parameters with vertical margins
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            layoutParams.setMargins(0, 20, 0, 20); // 0 for left and right margins, 10 for top and bottom margins
            editText.setLayoutParams(layoutParams);
            hintsList.add(measurement);
            editText.setHint(measurement);
            editText.setId(View.generateViewId());

            // You can customize EditText properties here
            layout.addView(editText);
        }

    }
}