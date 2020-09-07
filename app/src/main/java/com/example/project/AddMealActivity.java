package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AddMealActivity extends AppCompatActivity {

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    EditText nameEditText, priceEditText, thingsIncludedEditText, allergyEditText;
    Button addMealButton, galleryButton;
    FirebaseFirestore database;
    Intent intent;
    int galleryRequestCode = 9397;
    ImageView imageView;
    ArrayList<String> imageUri = new ArrayList<String>();
    File file;
    ClipData clipData;
    static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        database = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        nameEditText = findViewById(R.id.name);
        priceEditText = findViewById(R.id.price);
        thingsIncludedEditText = findViewById(R.id.thingsIncluded);
        allergyEditText = findViewById(R.id.allergy);
        imageView = findViewById(R.id.imageView);
        addMealButton = findViewById(R.id.addMealButton);
        galleryButton = findViewById(R.id.galleryButton);
        addMealButton.setEnabled(false);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                startActivityForResult(intent, galleryRequestCode);
            }
        });
        addMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String price = priceEditText.getText().toString();
                String thingsIncluded = thingsIncludedEditText.getText().toString();
                String allergy = allergyEditText.getText().toString();
                if (name.equals("") || price.equals("") || thingsIncluded.equals("")) {
                    AlertDialog.Builder popupBox = new AlertDialog.Builder(AddMealActivity.this);
                    popupBox.setTitle("Incomplete Details");
                    popupBox.setMessage("Please fill all details of add the meal");
                    popupBox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    popupBox.show();
                }
                else {
                    count = 0;
                    uploadPhoto(name, price, thingsIncluded, allergy);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == galleryRequestCode && resultCode == RESULT_OK) {
            clipData = data.getClipData();
            addMealButton.setEnabled(true);
        }
    }

    private void uploadPhoto(final String name, final String price, final String thingsIncluded, final String allergy) {
        if (clipData != null && clipData.getItemCount() == 5) {
            final String uniqueNumber = generateRandomNumber();
            addMealButton.setEnabled(false);
            final int imageCount = clipData.getItemCount();
            for (int i = 0; i < clipData.getItemCount(); i++) {
                Uri photoURI = clipData.getItemAt(i).getUri();
                final StorageReference imageReference = storageReference.child("meals/" + uniqueNumber + "/" + (i+1) + ".png");
                imageReference.putFile(photoURI)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        count++;
                                        imageUri.add(String.valueOf(uri));
                                        if (count == imageCount) {
                                            addMeal(name, price, thingsIncluded, allergy, imageUri, uniqueNumber);
                                        }
                                    }

                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });
            }
        }
        else {
            AlertDialog.Builder popupBox = new AlertDialog.Builder(AddMealActivity.this);
            popupBox.setTitle("More Images");
            popupBox.setMessage("Please select 5 images");
            popupBox.setPositiveButton("Ok", null);
            popupBox.show();
        }
    }

    private void addMeal(String name, String price, String thingsIncluded, String allergy , ArrayList<String> imageUri, String uniqueNumber) {
        Map<String, Object> meal = new HashMap<>();
        meal.put("name", name);
        meal.put("price", Double.valueOf(price));
        meal.put("thingsIncluded", thingsIncluded);
        meal.put("allergy", allergy);
        meal.put("imageUri", imageUri);
        meal.put("uniqueNumber", uniqueNumber);
        database.collection("meals")
                .add(meal)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("SUCCESS", "DocumentSnapshot added with ID: " + documentReference.getId());
                        intent = new Intent(AddMealActivity.this, AdminListActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FAILURE", "Error adding document", e);
                    }
                });
    }

    private String generateRandomNumber() {
        final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
        final java.util.Random rand = new java.util.Random();
        final Set<String> identifiers = new HashSet<String>();
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            for(int i = 0; i < 15; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }
}