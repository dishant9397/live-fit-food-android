package com.example.project;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class AdminMealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView nameTextView, pricetextView, thingsIncludedTextView, allergyTextView;
    Button editButton, deleteButton;
    ImageView imageView;
    onRowClickedListener listener;

    public AdminMealViewHolder(View itemView, onRowClickedListener listener) {
        super(itemView);
        this.listener = listener;
        nameTextView = itemView.findViewById(R.id.name);
        pricetextView = itemView.findViewById(R.id.price);
        thingsIncludedTextView = itemView.findViewById(R.id.thingsIncluded);
        allergyTextView = itemView.findViewById(R.id.allergy);
        editButton = itemView.findViewById(R.id.editButton);
        deleteButton = itemView.findViewById(R.id.deleteButton);
        imageView = itemView.findViewById(R.id.imageView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onRowClicked(getAdapterPosition());
    }
}
