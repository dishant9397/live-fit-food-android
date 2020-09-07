package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserMealAdapter extends RecyclerView.Adapter<UserViewHolder> {

    FirebaseFirestore database;
    ArrayList<Meal> mealList;
    onRowClickedListener listener;
    Intent intent;

    public UserMealAdapter(List<Meal> mealList, onRowClickedListener listener) {
        this.mealList = (ArrayList<Meal>) mealList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        database = FirebaseFirestore.getInstance();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_user_meal_list_row, parent, false);
        UserViewHolder holder = new UserViewHolder(view, listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, final int position) {
        final Meal meal = mealList.get(position);
        holder.nameTextView.setText(meal.getName());
        holder.pricetextView.setText("$: " + String.format("%.2f", meal.getPrice()));
        holder.thingsIncludedTextView.setText(meal.getThingsIncluded());
        if (meal.getAllergy().equals("")) {
            holder.allergyTextView.setText("Allergic to none");
        }
        else {
            holder.allergyTextView.setText("Allergic to " + meal.getAllergy());
        }
        Picasso.get().load(Uri.parse(meal.getImageUri().get(0))).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }
}
