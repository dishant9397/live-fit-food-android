package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckInActivity extends AppCompatActivity implements OnMapReadyCallback {

    FirebaseFirestore database;
    private GoogleMap googleMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    boolean locationPermissionGranted;
    Location lastKnownLocation;
    Location storeLocation = new Location(LocationManager.GPS_PROVIDER);
    int DEFAULT_ZOOM = 10;
    final LatLng defaultLocation = new LatLng(43.7023, -79.5296);
    final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;
    TextView orderNumber, mealName, orderAmount, orderDate, pickUpDate;
    Button checkInButton;
    Order order = new Order();
    int pickupSpot = 0;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        storeLocation.setLatitude(43.6284);
        storeLocation.setLongitude(-79.5074);
        database = FirebaseFirestore.getInstance();
        final String id = getIntent().getStringExtra("id");
        searchOrder(id);
        orderNumber = findViewById(R.id.orderNumber);
        mealName = findViewById(R.id.mealName);
        orderAmount = findViewById(R.id.orderAmount);
        orderDate = findViewById(R.id.orderDate);
        pickUpDate = findViewById(R.id.pickUpDate);
        checkInButton = findViewById(R.id.checkInButton);
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAfter = LocalDate.now().isAfter(order.getPickUpDates().get(0));
                checkDistance(lastKnownLocation, storeLocation);
                /*if (isAfter) {
                    checkDistance(lastKnownLocation, storeLocation);
                }
                else {
                    AlertDialog.Builder popupBox = new AlertDialog.Builder(CheckInActivity.this);
                    popupBox.setTitle("Error");
                    popupBox.setMessage("You cannot pickup your order before the pickup Date.");
                    popupBox.setPositiveButton("Ok", null);
                    popupBox.show();
                }*/
            }
        });
    }

    private void checkDistance(Location lastKnownLocation, Location storeLocation) {
        float distance = lastKnownLocation.distanceTo(storeLocation);
        if (distance <= 100) {
            AlertDialog.Builder popupBox = new AlertDialog.Builder(CheckInActivity.this);
            popupBox.setTitle("Enter Pickup Spot");
            String[] spots = {"Spot-1", "Spot-2", "Spot-3"};
            int checkedItem = -1;
            popupBox.setSingleChoiceItems(spots, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        pickupSpot = 1;
                    }
                    else if (which == 1) {
                        pickupSpot = 2;
                    }
                    else {
                        pickupSpot = 3;
                    }
                }
            });
            popupBox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    savePickupOrder(order.getOrderNumber(), order.getUser(), pickupSpot);
                    AlertDialog.Builder popupBox = new AlertDialog.Builder(CheckInActivity.this);
                    popupBox.setTitle("Congratulations");
                    popupBox.setMessage("You have picked up your order. Thanks for shopping with us!!");
                    popupBox.setPositiveButton("Ok", null);
                    popupBox.show();
                }
            });
            AlertDialog alertDialog = popupBox.create();
            alertDialog.show();
        }
        else {
            AlertDialog.Builder popupBox = new AlertDialog.Builder(CheckInActivity.this);
            popupBox.setTitle("Wrong Details");
            popupBox.setMessage("You aren't at the store yet! Please retry when you are at the store.");
            popupBox.setPositiveButton("Ok", null);
            popupBox.show();
        }
    }

    private void savePickupOrder(String orderNumber, String user, int pickupNumber) {
        Map<String, Object> pickedUpOrder = new HashMap<>();
        pickedUpOrder.put("orderNumber", orderNumber);
        pickedUpOrder.put("user", user);
        pickedUpOrder.put("pickupNumber", pickupNumber);
        pickedUpOrder.put("pickupDate", LocalDate.now().toString());
        database.collection("pickedUpOrders")
                .add(pickedUpOrder)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FAILURE", "Error adding document", e);
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
    }

    public void searchOrder(final String id) {
        database.collection("orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(id)) {
                                    String id = document.getId();
                                    String user = document.getData().get("user").toString();
                                    Double orderAmount = (Double) document.getData().get("orderAmount");
                                    String orderDate = document.getData().get("orderDate").toString();
                                    ArrayList<String> pickUpDates = (ArrayList<String>) document.getData().get("pickUpDates");
                                    String mealId = document.getData().get("mealId").toString();
                                    String mealName = document.getData().get("mealName").toString();
                                    Double mealPrice = (Double) document.getData().get("mealPrice");
                                    String mealThingsIncluded = document.getData().get("mealThingsIncluded").toString();
                                    String orderNumber = document.getData().get("orderNumber").toString();
                                    order  = new Order(id, user, orderAmount, orderDate, pickUpDates, mealId, mealName, mealPrice, mealThingsIncluded, orderNumber);
                                    setData();
                                }
                            }
                        } else {
                            Log.w("FAILURE", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void setData() {
        orderNumber.setText("Order Number: " + order.getOrderNumber());
        mealName.setText("Package: " + order.getMealName());
        orderAmount.setText("Total: $" + decimalFormat.format(order.getOrderAmount()));
        orderDate.setText("Order Date: " + order.getOrderDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        pickUpDate.setText("Pickup Date: " + order.getPickUpDates().get(0).format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
                else {
                    Toast.makeText(this, "Your location won't be used to track current location", Toast.LENGTH_SHORT).show();
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                LatLng location = new LatLng(storeLocation.getLatitude(), storeLocation.getLongitude());
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                googleMap.addMarker(new MarkerOptions().position(location).title("Store Location"));
                            }
                        } else {
                            Log.d("TAG", "Current location is null. Using defaults.");
                            Log.e("TAG", "Exception: %s", task.getException());
                            googleMap.addMarker(new MarkerOptions().position(defaultLocation).title("Current Location"));
                            googleMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
}