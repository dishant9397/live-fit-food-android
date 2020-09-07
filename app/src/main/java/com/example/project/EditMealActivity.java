package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class EditMealActivity extends AppCompatActivity {

    private StorageReference storageReference;
    EditText nameEditText, priceEditText, thingsIncludedEditText, allergyEditText;
    Button updateMealButton;
    FirebaseFirestore database;
    ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
    ArrayList<String> imageUris = new ArrayList<String>();
    Intent intent;
    String mealName = "";
    int cameraRequestCode = 1500;
    int galleryRequestCode = 9397;
    int index;
    File file;
    Meal meal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meal);
        storageReference = FirebaseStorage.getInstance().getReference();
        database = FirebaseFirestore.getInstance();
        meal = (Meal) getIntent().getSerializableExtra("meal");
        nameEditText = findViewById(R.id.name);
        priceEditText = findViewById(R.id.price);
        thingsIncludedEditText = findViewById(R.id.thingsIncluded);
        allergyEditText = findViewById(R.id.allergy);
        for (int i = 0; i < 5; i++) {
            int id = R.id.imageView + (i+1);
            imageViews.add((ImageView) findViewById(id));
            imageUris.add(meal.getImageUri().get(i));
        }
        updateMealButton = findViewById(R.id.updateMealButton);
        mealName = meal.getName();
        nameEditText.setText(meal.getName());
        priceEditText.setText(meal.getPrice().toString());
        thingsIncludedEditText.setText(meal.getThingsIncluded());
        allergyEditText.setText(meal.getAllergy());
        for (int i = 0; i < meal.getImageUri().size(); i++) {
            Picasso.get().load(Uri.parse(meal.getImageUri().get(i))).networkPolicy(NetworkPolicy.NO_STORE).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageViews.get(i));
        }
        imageViews.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 0;
                AlertDialog.Builder popupBox = new AlertDialog.Builder(EditMealActivity.this);
                popupBox.setTitle("Select Input Type");
                popupBox.setMessage("Please select the input type of this image");
                popupBox.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openCamera(0);
                    }
                });
                popupBox.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openGallery();
                    }
                });
                popupBox.show();
            }
        });
        imageViews.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 1;
                AlertDialog.Builder popupBox = new AlertDialog.Builder(EditMealActivity.this);
                popupBox.setTitle("Select Input Type");
                popupBox.setMessage("Please select the input type of this image");
                popupBox.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openCamera(1);
                    }
                });
                popupBox.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openGallery();
                    }
                });
                popupBox.show();
            }
        });
        imageViews.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 2;
                AlertDialog.Builder popupBox = new AlertDialog.Builder(EditMealActivity.this);
                popupBox.setTitle("Select Input Type");
                popupBox.setMessage("Please select the input type of this image");
                popupBox.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openCamera(2);
                    }
                });
                popupBox.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openGallery();
                    }
                });
                popupBox.show();
            }
        });
        imageViews.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 3;
                AlertDialog.Builder popupBox = new AlertDialog.Builder(EditMealActivity.this);
                popupBox.setTitle("Select Input Type");
                popupBox.setMessage("Please select the input type of this image");
                popupBox.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openCamera(3);
                    }
                });
                popupBox.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openGallery();
                    }
                });
                popupBox.show();
            }
        });
        imageViews.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 4;
                AlertDialog.Builder popupBox = new AlertDialog.Builder(EditMealActivity.this);
                popupBox.setTitle("Select Input Type");
                popupBox.setMessage("Please select the input type of this image");
                popupBox.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openCamera(4);
                    }
                });
                popupBox.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openGallery();
                    }
                });
                popupBox.show();
            }
        });
        updateMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String price = priceEditText.getText().toString();
                String thingsIncluded = thingsIncludedEditText.getText().toString();
                String allergy = allergyEditText.getText().toString();
                if (name.equals("") || price.equals("") || thingsIncluded.equals("")) {
                    AlertDialog.Builder popupBox = new AlertDialog.Builder(EditMealActivity.this);
                    popupBox.setTitle("Incomplete Details");
                    popupBox.setMessage("Please fill all details of update the meal");
                    popupBox.setPositiveButton("Ok", null);
                    popupBox.show();
                }
                else {
                    editMeal(name, price, thingsIncluded, allergy);
                }
            }
        });
    }

    private void openGallery() {
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, galleryRequestCode);
        }

    }

    private void openCamera(int index) {
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = getFileForPhoto((index+1) + ".png");
        Uri fileProvider = FileProvider.getUriForFile(EditMealActivity.this, "com.example.project", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, cameraRequestCode);
        }
    }

    public File getFileForPhoto(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Name");
        if (mediaStorageDir.exists() == false && mediaStorageDir.mkdirs() == false) {
            Log.d("Response", "Cannot create directory for storing photos");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == cameraRequestCode) {
            if (resultCode == RESULT_OK) {
                Uri photoURI = Uri.parse(String.valueOf(file.toURI()));
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                int id = R.id.imageView + (index+1);
                uploadImage(photoURI);
                imageViews.set(index, (ImageView) findViewById(id));
                imageViews.get(index).setImageBitmap(bitmap);
            } else {
                Toast t = Toast.makeText(this, "Not able to take photo", Toast.LENGTH_SHORT);
                t.show();
            }
        }
        if (requestCode == galleryRequestCode) {
            if (data != null) {
                Uri photoURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI);
                    int id = R.id.imageView + (index+1);
                    uploadImage(photoURI);
                    imageViews.set(index, (ImageView) findViewById(id));
                    imageViews.get(index).setImageBitmap(bitmap);
                }
                catch (FileNotFoundException e) {
                    Log.d("File Not Found", "FileNotFoundException: Unable to open photo gallery file");
                    e.printStackTrace();
                }
                catch (IOException e) {
                    Log.d("Exception", "IOException: Unable to open photo gallery file");
                    e.printStackTrace();
                }
            }
        }
    }

    public void uploadImage(Uri uri) {
        updateMealButton.setEnabled(false);
        final Uri imageUri = uri;
        int indexCount = meal.getImageUri().get(index).indexOf("?");
        String name = meal.getImageUri().get(index).substring(0, indexCount);
        final StorageReference imageReference = storageReference.child("meals/" + meal.getUniqueNumber() + "/" + (index+1) + ".png");
        //final StorageReference imageReference = storageReference.child(image);
        imageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        updateMealButton.setEnabled(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
    }

    private void editMeal(String name, String price, String thingsIncluded, String allergy) {
        Meal newMeal = new Meal(meal.getId(), name, Double.valueOf(price), thingsIncluded, allergy, meal.getImageUri(), meal.getUniqueNumber());
        database.collection("meals").document(meal.getId())
                .set(newMeal)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        intent = new Intent(EditMealActivity.this, AdminListActivity.class);
                        startActivity(intent);
                    }
                });
    }
}