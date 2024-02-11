package com.example.rich_stich;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNewCustomer extends AppCompatActivity {

    private EditText editTextName, editTextMobile, editTextAddress;
    private Button btnSaveCustomer;
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
    private DatabaseReference customersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set your desired icon for the navigation drawer toggle
        }
        customersReference = FirebaseDatabase.getInstance().getReference().child("customers");
        getSupportActionBar().setTitle("Add New Customer");
        // Initialize UI elements
        editTextName = findViewById(R.id.editTextName);
        editTextMobile = findViewById(R.id.editTextMobile);
        editTextAddress = findViewById(R.id.editTextAddress);
        btnSaveCustomer = findViewById(R.id.buttonSave);

        btnSaveCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCustomer();
            }
        });
    }

    private void saveCustomer() {
        // Get input values from EditTexts
        String name = editTextName.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();

        // Validate input
        if (name.isEmpty() || mobile.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique key for the customer
        String customerId = customersReference.push().getKey();

        // Create a Customer object
        Customer newCustomer = new Customer(customerId, name, mobile, address);

        // Save the customer to the Firebase Realtime Database
        customersReference.child(customerId).setValue(newCustomer);

        // Display success message
        Toast.makeText(this, "Customer added successfully", Toast.LENGTH_SHORT).show();

        // Clear input fields
        editTextName.getText().clear();
        editTextMobile.getText().clear();
        editTextAddress.getText().clear();
    }
}