package com.example.rich_stich;

import android.content.Intent;
import android.os.Bundle;
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

public class RecieptActivity extends AppCompatActivity implements RecieptActivityAdapter.OnMarkAsDoneClickListener{

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
        setContentView(R.layout.activity_reciept);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set your desired icon for the navigation drawer toggle
        }
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setTitle("Reciept");
        // Inside OrdersActivity.java
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders").child("reciept");

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
                RecieptActivityAdapter ordersAdapter = new RecieptActivityAdapter(RecieptActivity.this,orderList,RecieptActivity.this);
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
        DatabaseReference doneOrdersRef = FirebaseDatabase.getInstance().getReference("orders").child("reciept");
        doneOrdersRef.child(clickedOrder.getKey()).removeValue();

        Toast.makeText(this, "reciept deleted successfully", Toast.LENGTH_SHORT).show();

        // Remove the clicked order from the local orderList
        orderList.remove(clickedOrder);

        startActivity(new Intent(RecieptActivity.this, Home.class));
    }
}