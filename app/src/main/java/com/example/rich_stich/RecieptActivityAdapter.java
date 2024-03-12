package com.example.rich_stich;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class RecieptActivityAdapter extends RecyclerView.Adapter<RecieptActivityAdapter.OrderViewHolder>{

    private Context context;
    private List<OrderInfo> orders;
    private RecieptActivityAdapter.OnMarkAsDoneClickListener markAsDoneClickListener;

    public RecieptActivityAdapter(Context context, List<OrderInfo> orders, RecieptActivityAdapter.OnMarkAsDoneClickListener listener) {
        this.context = context;
        this.orders = orders;
        this.markAsDoneClickListener = listener;
    }

    @NonNull
    @Override
    public RecieptActivityAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(context).inflate(R.layout.reciept_item_order, parent, false);
        return new RecieptActivityAdapter.OrderViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecieptActivityAdapter.OrderViewHolder holder, int position) {
        OrderInfo currentOrder = orders.get(position);

        DatabaseReference ordersRef = FirebaseDatabase.getInstance()
                .getReference("imageCollection").child(currentOrder.getMaterial())
                .child("url");
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imageUrl = dataSnapshot.getValue(String.class);

                if (imageUrl != null) {
                    // Load the image using Picasso
                    Picasso.get().load(imageUrl).into(holder.materialImageView);
                } else {
                    // Handle the case where imageUrl is null or not available
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        // Populate views
        holder.genderTextView.setText("Gender: " + currentOrder.getGender());
        holder.apparelTextView.setText("Apparel: " + currentOrder.getApparel());
        holder.materialTextView.setText("Material: " + currentOrder.getMaterial());
        holder.measurementsTextView.setText("Measurements: " + getFormattedMeasurements(currentOrder.getMeasurements()));
        if(currentOrder.getCustomer()!=null && currentOrder.getCustomer().getName()!=null)
            holder.customerNameTextView.setText("Customer: " + currentOrder.getCustomer().getName());
        holder.quantityTextView.setText("Quantity: " + currentOrder.getQuantity());
        holder.totalAmountTextView.setText("Total Amount: " + currentOrder.getTotal());
        holder.clothAmountTextView.setText("Cloth Amount: " + currentOrder.getClothAmount());
        holder.addonsTextView.setText("Addons: " + currentOrder.getAddons());

        // Set click listener for the "Mark as Done" button
        holder.markAsDoneButton.setOnClickListener(view -> {
            if (markAsDoneClickListener != null) {
                markAsDoneClickListener.onMarkAsDoneClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    // ViewHolder class
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView materialImageView;
        TextView genderTextView;
        TextView apparelTextView;
        TextView materialTextView;
        TextView measurementsTextView;
        TextView customerNameTextView;
        TextView quantityTextView;
        TextView totalAmountTextView;
        TextView clothAmountTextView;
        TextView addonsTextView;
        Button markAsDoneButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views
            materialImageView = itemView.findViewById(R.id.materialImageView);
            genderTextView = itemView.findViewById(R.id.genderTextView);
            apparelTextView = itemView.findViewById(R.id.apparelTextView);
            materialTextView = itemView.findViewById(R.id.materialTextView);
            measurementsTextView = itemView.findViewById(R.id.measurementsTextView);
            customerNameTextView = itemView.findViewById(R.id.customerNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            totalAmountTextView = itemView.findViewById(R.id.totalAmountTextView);
            clothAmountTextView = itemView.findViewById(R.id.clothAmountTextView);
            addonsTextView = itemView.findViewById(R.id.addonsTextView);
            markAsDoneButton = itemView.findViewById(R.id.markAsDoneButton);
        }
    }

    // Interface to handle the "Mark as Done" button click
    public interface OnMarkAsDoneClickListener {
        void onMarkAsDoneClick(int position);
    }

    // Add other necessary methods and interfaces

    // Method to get the image resource based on material (you need to implement this)
    private int getImageResource(String material) {
        // Implement this method based on your data structure
        // Return the appropriate image resource ID
        return R.drawable.ic_launcher_foreground; // Placeholder image
    }

    // Method to format measurements (you need to implement this)
    private String getFormattedMeasurements(Map<String, String> measurements) {
        // Implement this method based on your data structure
        // Return the formatted measurements string
        StringBuilder formattedMeasurements = new StringBuilder();
        for (Map.Entry<String, String> entry : measurements.entrySet()) {
            formattedMeasurements.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append(", ");
        }
        if (formattedMeasurements.length() > 2) {
            // Remove the trailing comma and space
            formattedMeasurements.setLength(formattedMeasurements.length() - 2);
        }
        return formattedMeasurements.toString();
    }
}
