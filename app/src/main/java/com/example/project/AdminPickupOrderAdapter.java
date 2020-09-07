package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdminPickupOrderAdapter extends RecyclerView.Adapter<AdminPickupOrderViewHolder> {

    ArrayList<PickedUpOrder> pickupOrderList;
    onRowClickedListener listener;
    Context context;

    public AdminPickupOrderAdapter(List<PickedUpOrder> pickupOrderList, onRowClickedListener listener) {
        this.pickupOrderList = (ArrayList<PickedUpOrder>) pickupOrderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminPickupOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_admin_pickeduporder_list_row, parent, false);
        AdminPickupOrderViewHolder holder = new AdminPickupOrderViewHolder(view, listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdminPickupOrderViewHolder holder, final int position) {
        final PickedUpOrder pickedUpOrder = pickupOrderList.get(position);
        holder.orderNumberTextView.setText("Order Number: " + pickedUpOrder.getOrderNumber());
        holder.userTextView.setText("User: " + pickedUpOrder.getUser());
        holder.pickupNumberTextView.setText("Pickup Spot: " + String.valueOf(pickedUpOrder.getPickupNumber()));
        holder.pickupDateTextView.setText(pickedUpOrder.getPickupDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
    }

    @Override
    public int getItemCount() {
        return pickupOrderList.size();
    }
}