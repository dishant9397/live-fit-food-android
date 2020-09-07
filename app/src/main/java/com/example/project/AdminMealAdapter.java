package com.example.project;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AdminMealAdapter extends RecyclerView.Adapter<AdminMealViewHolder> {

    FirebaseFirestore database;
    ArrayList<Meal> mealList;
    onRowClickedListener listener;
    Intent intent;
    Context context;
    File file;
    private StorageReference storageReference;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public AdminMealAdapter(List<Meal> mealList, onRowClickedListener listener) {
        this.mealList = (ArrayList<Meal>) mealList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        storageReference = FirebaseStorage.getInstance().getReference();
        context = parent.getContext();
        database = FirebaseFirestore.getInstance();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_admin_meal_list_row, parent, false);
        AdminMealViewHolder holder = new AdminMealViewHolder(view, listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdminMealViewHolder holder, final int position) {
        final Meal meal = mealList.get(position);
        holder.nameTextView.setText(meal.getName());
        holder.pricetextView.setText("$: " + decimalFormat.format(meal.getPrice()));
        holder.thingsIncludedTextView.setText(meal.getThingsIncluded());
        if (meal.getAllergy().equals("")) {
            holder.allergyTextView.setText("Allergy: None");
        }
        else {
            holder.allergyTextView.setText("Allergy: " + meal.getAllergy());
        }
        Picasso.get().load(Uri.parse(meal.getImageUri().get(0))).networkPolicy(NetworkPolicy.NO_STORE).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.imageView);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, EditMealActivity.class);
                intent.putExtra("meal", meal);
                context.startActivity(intent);
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMeal(meal);
                notifyDataSetChanged();
                mealList.remove(position);
            }
        });
    }

    private void deleteMeal(Meal meal) {
        database.collection("meals").document(meal.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }
}
