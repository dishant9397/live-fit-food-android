package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserMealListDetailActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore database;
    TextView nameTextView, thingsIncludedTextView, allergyTextView;
    Button placeOrderButton;
    ImageView imageView;
    ImageButton minusButton, plusButton;
    int index = 0;
    RadioGroup radioGroup;
    RadioButton month, months, year;
    Intent intent;
    Meal meal;
    ArrayList<String> pickUpDates = new ArrayList<String>();
    final float discount = (float) 0.95;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_meal_list_detail);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database = FirebaseFirestore.getInstance();
        meal = (Meal) getIntent().getSerializableExtra("meal");
        nameTextView = findViewById(R.id.name);
        thingsIncludedTextView = findViewById(R.id.thingsIncluded);
        allergyTextView = findViewById(R.id.allergy);
        imageView = findViewById(R.id.imageView);
        minusButton = findViewById(R.id.minusButton);
        plusButton = findViewById(R.id.plusButton);
        placeOrderButton = findViewById(R.id.placeOrderButton);
        radioGroup = findViewById(R.id.radioGroup);
        month = findViewById(R.id.month);
        months = findViewById(R.id.months);
        year = findViewById(R.id.year);
        nameTextView.setText("Name: " + meal.getName());
        thingsIncludedTextView.setText("Description: " + meal.getThingsIncluded());
        minusButton.setEnabled(false);
        Picasso.get().load(Uri.parse(meal.getImageUri().get(0))).networkPolicy(NetworkPolicy.NO_STORE).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
        if (meal.getAllergy().equals("")) {
            allergyTextView.setText("Allergy: None");
        }
        else {
            allergyTextView.setText("Allergy:" + meal.getAllergy());
        }
        month.setText("1 month at $:" + decimalFormat.format(meal.getPrice()) + " which is $:" + decimalFormat.format(meal.getPrice()) + "/month");
        months.setText("6 months at $:" + decimalFormat.format(meal.getPrice() * 6.0 * discount) + " which is $:" + decimalFormat.format(meal.getPrice() * discount) + "/month");
        year.setText("12 months at $:" + decimalFormat.format(meal.getPrice() * 12.0 * discount * discount * discount) + " which is $:" + decimalFormat.format(meal.getPrice() * discount * discount * discount) + "/month");
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index != 0) {
                    plusButton.setEnabled(true);
                    index--;
                    Picasso.get().load(Uri.parse(meal.getImageUri().get(index))).networkPolicy(NetworkPolicy.NO_STORE).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
                }
                else {
                    minusButton.setEnabled(false);
                }
            }
        });
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index != 4) {
                    minusButton.setEnabled(true);
                    index++;
                    Picasso.get().load(Uri.parse(meal.getImageUri().get(index))).networkPolicy(NetworkPolicy.NO_STORE).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
                }
                else {
                    plusButton.setEnabled(false);
                }
            }
        });
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    AlertDialog.Builder popupBox = new AlertDialog.Builder(UserMealListDetailActivity.this);
                    popupBox.setTitle("Incomplete Details");
                    popupBox.setMessage("Please select any subscription period to go ahead");
                    popupBox.setPositiveButton("Ok", null);
                    popupBox.show();
                }
                else {
                    placeOrder();
                }
            }
        });
    }

    private void placeOrder() {
        pickUpDates.clear();
        Map<String, Object> order = new HashMap<>();
        if (firebaseUser.getEmail() == null) {
            order.put("user", firebaseUser.getPhoneNumber());
        }
        else {
            order.put("user", firebaseUser.getEmail());
        }
        order.put("mealId", meal.getId());
        order.put("mealName", meal.getName());
        order.put("mealPrice", meal.getPrice());
        order.put("mealThingsIncluded", meal.getThingsIncluded());
        order.put("orderDate", LocalDate.now().toString());
        Double orderAmount = 1.13;
        int orderDuration;
        if (month.isChecked()) {
            orderDuration = 1;
            order.put("orderDuration", orderDuration);
            orderAmount = meal.price * orderAmount;
        }
        else if (months.isChecked()) {
            orderDuration = 6;
            order.put("orderDuration", orderDuration);
            orderAmount = meal.price * 6 * orderAmount;
        }
        else {
            orderDuration = 12;
            order.put("orderDuration", orderDuration);
            orderAmount = meal.price * 12 * discount * discount * discount * orderAmount;
        }
        LocalDate localDate = LocalDate.now().plusDays(7);
        for (int i = 0; i < orderDuration; i++) {
            pickUpDates.add(localDate.toString());
            localDate = localDate.plusDays(30);
        }
        order.put("pickUpDates", pickUpDates);
        order.put("orderAmount", orderAmount);
        order.put("orderNumber", generateRandomOrderNumber());
        database.collection("orders")
                .add(order)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        intent = new Intent(UserMealListDetailActivity.this, CheckInActivity.class);
                        intent.putExtra("id", documentReference.getId());
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FAILED", "Error adding document", e);
                    }
                });
    }

    private String generateRandomOrderNumber() {
        final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
        final java.util.Random rand = new java.util.Random();
        final Set<String> identifiers = new HashSet<String>();
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            for(int i = 0; i < 5; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }
}