package com.example.rich_stich;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Payment extends AppCompatActivity {

    EditText QuantityEdt,TotalEdt,ClothAmtEdt,Addons;
    private Spinner customerSpinner;
    private DatabaseReference customersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        String material=(String) intent.getSerializableExtra("selectedMaterial");
        GenderAndApparelSelection obj=(GenderAndApparelSelection) intent.getSerializableExtra("genderAndApparel");
        ArrayList<String> measurementsList = (ArrayList<String>) intent.getSerializableExtra("measurements");
        ArrayList<String> hintsList = (ArrayList<String>) intent.getSerializableExtra("hints");
        String price=(String) intent.getSerializableExtra("materialPrice");


        customerSpinner = findViewById(R.id.customerSpinner);
        LinearLayout layout = findViewById(R.id.linearLayout);
        ImageView imageView = findViewById(R.id.materialImageView);
        TextView apparelTextView = findViewById(R.id.apparelTextView);
        Button payButton = findViewById(R.id.payButton);
        QuantityEdt = findViewById(R.id.Quantity);
        TotalEdt = findViewById(R.id.TotalAmount);
        ClothAmtEdt = findViewById(R.id.ClothAmount);
        Addons = findViewById(R.id.addons);


        //creating spinner options for customer selection
        customersReference = FirebaseDatabase.getInstance().getReference().child("customers");
        customersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Assuming Customer is a class representing your customer data
                List<Customer> customerList = new ArrayList<>();

                for (DataSnapshot customerSnapshot : dataSnapshot.getChildren()) {
                    Customer customer = customerSnapshot.getValue(Customer.class);
                    if (customer != null) {
                        customerList.add(customer);
                    }
                }

                // Now, you have the customerList, proceed to populate the Spinner
                ArrayAdapter<Customer> adapter = new ArrayAdapter<>(Payment.this, R.layout.simple_spinner_item, customerList);
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                customerSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });

        //displaying image in the image view
        DatabaseReference imagesRef = FirebaseDatabase.getInstance().getReference("imageCollection").child(material);
        imagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imageUrl = dataSnapshot.child("url").getValue(String.class);
                Picasso.get().load(imageUrl).into(imageView);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        apparelTextView.setText("Apparel : "+obj.getApparel());


        Log.d("MyActivity", "Material Price : " + price);
        Log.d("MyActivity", "Material: " + material);
        Log.d("MyActivity", "Gender : " + obj.getGender());
        Log.d("MyActivity", "Apparel : " + obj.getApparel());
        Log.d("MyActivity", "Measurements List:");
        for (String measurement : measurementsList) {
            Log.d("MyActivity", "   " + measurement);
        }
        Log.d("MyActivity", "Hints List:");
        for (String hint : hintsList) {
            Log.d("MyActivity", "   " + hint);
        }

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = QuantityEdt.getText().toString();
                String total = TotalEdt.getText().toString();
                String clothAmount = ClothAmtEdt.getText().toString();
                String addons = Addons.getText().toString();
                Customer customer = (Customer) customerSpinner.getSelectedItem();

                // Create a new order object
                OrderInfo order = new OrderInfo();
                order.setMaterial(material);
                order.setGender(obj.getGender());
                order.setApparel(obj.getApparel());

                // Assuming measurementsList and hintsList are ArrayList<String>
                Map<String, String> measurementsMap = new HashMap<>();
                for (int i = 0; i < measurementsList.size(); i++) {
                    String measurementKey = hintsList.get(i);
                    String measurementValue = measurementsList.get(i);
                    measurementsMap.put(measurementKey, measurementValue);
                }
                order.setMeasurements(measurementsMap);

                order.setQuantity(quantity);
                order.setTotal(total);
                order.setClothAmount(clothAmount);
                order.setAddons(addons);
                order.setCustomer(customer);


                // Save the order to the database
                DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders").child("pending");
                String orderId = ordersRef.push().getKey();
                order.setKey(orderId);
                ordersRef.child(orderId).setValue(order);

                Toast.makeText(Payment.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Payment.this, Home.class));
            }
        });


        for (int i = 0; i < measurementsList.size(); i++) {
            TextView tv = new TextView(this);
            tv.setText(hintsList.get(i)+" : "+measurementsList.get(i));
            tv.setGravity(Gravity.CENTER);

            // You can customize EditText properties here
            layout.addView(tv);
        }
    }

}