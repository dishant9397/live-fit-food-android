package com.example.project;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView imageView;
    TextView nameTextView, pricetextView, thingsIncludedTextView, allergyTextView;
    onRowClickedListener listener;

    public UserViewHolder(View itemView, onRowClickedListener listener) {
        super(itemView);
        this.listener = listener;
        imageView = itemView.findViewById(R.id.imageView);
        nameTextView = itemView.findViewById(R.id.name);
        pricetextView = itemView.findViewById(R.id.price);
        thingsIncludedTextView = itemView.findViewById(R.id.thingsIncluded);
        allergyTextView = itemView.findViewById(R.id.allergy);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onRowClicked(getAdapterPosition());
    }
}
