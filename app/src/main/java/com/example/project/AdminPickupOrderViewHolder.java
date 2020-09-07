package com.example.project;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class AdminPickupOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView orderNumberTextView, userTextView, pickupNumberTextView, pickupDateTextView;

    onRowClickedListener listener;

    public AdminPickupOrderViewHolder(View itemView, onRowClickedListener listener) {
        super(itemView);
        this.listener = listener;
        orderNumberTextView = itemView.findViewById(R.id.orderNumber);
        userTextView = itemView.findViewById(R.id.user);
        pickupNumberTextView = itemView.findViewById(R.id.pickupNumber);
        pickupDateTextView = itemView.findViewById(R.id.pickupDate);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onRowClicked(getAdapterPosition());
    }
}