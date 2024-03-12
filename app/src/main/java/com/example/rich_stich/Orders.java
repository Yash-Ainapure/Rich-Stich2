package com.example.rich_stich;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Orders extends AppCompatActivity implements OrderAdapter.OnMarkAsDoneClickListener{


    private RecyclerView recyclerViewOrders;
    private List<OrderInfo> orderList = new ArrayList<>();

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
        setContentView(R.layout.activity_orders);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set your desired icon for the navigation drawer toggle
        }
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setTitle("Pending Orders");
        // Inside OrdersActivity.java
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders").child("pending");

        // Attach a ValueEventListener to fetch orders
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    OrderInfo order = orderSnapshot.getValue(OrderInfo.class);
                    if (order != null) {
                        orderList.add(order);
                    }
                }

                // Populate RecyclerView with orders
                OrderAdapter ordersAdapter = new OrderAdapter(Orders.this,orderList,Orders.this);
                recyclerViewOrders.setAdapter(ordersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

    }
    @Override
    public void onMarkAsDoneClick(int position) {
        OrderInfo clickedOrder = orderList.get(position);
        DatabaseReference doneOrdersRef = FirebaseDatabase.getInstance().getReference("orders").child("done");
        String previousKey=clickedOrder.getKey();

        String key=doneOrdersRef.push().getKey();
        clickedOrder.setKey(key);
        // Push the clicked order to the "done" node
        doneOrdersRef.child(key).setValue(clickedOrder);

        // Remove the clicked order from the "pending" node
        DatabaseReference pendingOrderRef = FirebaseDatabase.getInstance().getReference().child("orders").child("pending").child(previousKey);
        pendingOrderRef.removeValue();

        if(clickedOrder.getCustomer()!=null && clickedOrder.getCustomer().getMobile()!=null) {
            String msg = "Heyy "+clickedOrder.getCustomer().getName()+ " Your order: " + clickedOrder.getApparel() + " is completed -Ritch-Stich";
            sendSMS(clickedOrder.getCustomer().getMobile(), msg);
            Log.d("names ","name :"+clickedOrder.getCustomer().getName());
            Log.d("names ","apparel :"+clickedOrder.getApparel());
            Log.d("names ","total :"+clickedOrder.getTotal());
            Log.d("names msg ",msg);
        }

        //Toast.makeText(this,"pending order done order done successfully,added to the completed orders section", Toast.LENGTH_SHORT).show();

        // Remove the clicked order from the local orderList
        orderList.remove(clickedOrder);

        startActivity(new Intent(Orders.this,Home.class));
    }
    private void sendSMS(String phoneNumber, String message) {
        try {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
            Log.d("sms failed reasson","the reason is "+e.getMessage());
            e.printStackTrace();
        }
    }

}