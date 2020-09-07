package com.example.project;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class AdminOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView userTextView, orderAmountTextView, orderDateTextView;

    onRowClickedListener listener;

    public AdminOrderViewHolder(View itemView, onRowClickedListener listener) {
        super(itemView);
        this.listener = listener;
        userTextView = itemView.findViewById(R.id.user);
        orderAmountTextView = itemView.findViewById(R.id.orderAmount);
        orderDateTextView = itemView.findViewById(R.id.orderDate);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onRowClicked(getAdapterPosition());
    }
}
