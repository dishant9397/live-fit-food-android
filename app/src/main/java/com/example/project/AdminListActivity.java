package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class AdminListActivity extends AppCompatActivity implements onRowClickedListener{

    FirebaseFirestore database;
    RecyclerView recyclerView;
    AdminMealAdapter adminMealAdapter;
    AdminOrderAdapter adminOrderAdapter;
    AdminPickupOrderAdapter adminPickupOrderAdapter;
    ArrayList<Meal> mealList = new ArrayList<Meal>();
    ArrayList<Order> orderList = new ArrayList<Order>();
    ArrayList<PickedUpOrder> pickupOrderList = new ArrayList<PickedUpOrder>();
    Button addMealButton, showMealsButton, showOrdersButton, showPickupOrdersButton;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_meal_list);
        database = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        addMealButton = findViewById(R.id.addMealButton);
        showMealsButton = findViewById(R.id.showMealsButton);
        showOrdersButton = findViewById(R.id.showOrdersButton);
        showPickupOrdersButton = findViewById(R.id.showPickupOrdersButton);
        addMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AdminListActivity.this, AddMealActivity.class);
                startActivity(intent);
            }
        });
        showMealsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMealData();
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        });
        showOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrderData();
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        });
        showPickupOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPickupOrderData();
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        });
    }

    private void createPickupOrderData() {
        pickupOrderList.clear();
        adminPickupOrderAdapter = new AdminPickupOrderAdapter(pickupOrderList, this);
        database.collection("pickedUpOrders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String orderNumber = document.getData().get("orderNumber").toString();
                                String user = document.getData().get("user").toString();
                                int pickupNumber = Integer.parseInt(document.getData().get("pickupNumber").toString());
                                String pickupDate = document.getData().get("pickupDate").toString();
                                pickupOrderList.add(new PickedUpOrder(orderNumber, user, pickupNumber, pickupDate));
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.setAdapter(adminPickupOrderAdapter);
                                }
                            });
                        } else {
                            Log.w("FAILURE", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void createOrderData() {
        orderList.clear();
        adminOrderAdapter = new AdminOrderAdapter(orderList, this);
        database.collection("orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("SUCCESS", document.getId() + " => " + document.getData());
                                String user = document.getData().get("user").toString();
                                Double orderAmount = (Double) document.getData().get("orderAmount");
                                String orderDate = document.getData().get("orderDate").toString();
                                String orderNumber = document.getData().get("orderNumber").toString();
                                orderList.add(new Order(user, orderAmount, orderDate, orderNumber));
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.setAdapter(adminOrderAdapter);
                                }
                            });
                        } else {
                            Log.w("FAILURE", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void createMealData() {
        mealList.clear();
        adminMealAdapter = new AdminMealAdapter(mealList, this);
        database.collection("meals").orderBy("name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String name = document.getData().get("name").toString();
                                Double price = (Double) document.getData().get("price");
                                String thingsIncluded = document.getData().get("thingsIncluded").toString();
                                String allergy = document.getData().get("allergy").toString();
                                ArrayList<String> imageUri = (ArrayList<String>) document.getData().get("imageUri");
                                String uniqueNumber = document.getData().get("uniqueNumber").toString();
                                Collections.sort(imageUri);
                                mealList.add(new Meal(id, name, price, thingsIncluded, allergy, imageUri, uniqueNumber));
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.setAdapter(adminMealAdapter);
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
    }
}