package com.example.rich_stich;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciept);

        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

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