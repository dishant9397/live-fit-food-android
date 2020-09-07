package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderViewHolder> {

    ArrayList<Order> orderList;
    onRowClickedListener listener;
    Context context;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public AdminOrderAdapter(List<Order> orderList, onRowClickedListener listener) {
        this.orderList = (ArrayList<Order>) orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_admin_order_list_row, parent, false);
        AdminOrderViewHolder holder = new AdminOrderViewHolder(view, listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdminOrderViewHolder holder, final int position) {
        final Order order = orderList.get(position);
        holder.userTextView.setText("User: " + order.getUser());
        holder.orderAmountTextView.setText("Total Amount: $" + decimalFormat.format(order.getOrderAmount()));
        holder.orderDateTextView.setText("Order Date: " + order.getOrderDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
