package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class UserMealListActivity extends AppCompatActivity implements onRowClickedListener {

    FirebaseFirestore database;
    RecyclerView recyclerView;
    UserMealAdapter userMealAdapter;
    ArrayList<Meal> mealList = new ArrayList<Meal>();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_meal_list);
        database = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        createData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void createData() {
        userMealAdapter = new UserMealAdapter(mealList, this);
        database.collection("meals").orderBy("name", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                String id = document.getId();
                                String name = document.getData().get("name").toString();
                                Double price = (Double) document.getData().get("price");
                                String thingsIncluded = document.getData().get("thingsIncluded").toString();
                                String allergy = document.getData().get("allergy").toString();
                                ArrayList<String> imageUri = (ArrayList<String>) document.getData().get("imageUri");
                                String uniqueNumber = document.getData().get("uniqueNumber").toString();
                                mealList.add(new Meal(id, name, price, thingsIncluded, allergy, imageUri, uniqueNumber));
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.setAdapter(userMealAdapter);
                                }
                            });
                        } else {
                            Log.w("FAILURE", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onRowClicked(int position) {
        intent = new Intent(UserMealListActivity.this, UserMealListDetailActivity.class);
        intent.putExtra("meal", mealList.get(position));
        startActivity(intent);
    }
}