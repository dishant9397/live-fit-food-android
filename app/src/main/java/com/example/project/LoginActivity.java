package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    EditText emailEditText, passwordEditText;
    Button loginButton, registerButton, loginWithPhoneButton;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        loginWithPhoneButton = findViewById(R.id.loginWithPhoneButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (email.equals("") || password.equals("")) {
                    AlertDialog.Builder popupBox = new AlertDialog.Builder(LoginActivity.this);
                    popupBox.setTitle("Incomplete Details");
                    popupBox.setMessage("Please fill all details to login");
                    popupBox.setPositiveButton("Ok", null);
                    popupBox.show();
                }
                else {
                    loginUser(email, password);
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        loginWithPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this, LoginWithPhoneActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(final String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (email.equals("admin@gmail.com")) {
                                intent = new Intent(LoginActivity.this, AdminListActivity.class);
                            }
                            else {
                                intent = new Intent(LoginActivity.this, UserMealListActivity.class);
                            }
                            startActivity(intent);
                        } else {
                            AlertDialog.Builder popupBox = new AlertDialog.Builder(LoginActivity.this);
                            popupBox.setTitle("Incorrect Details");
                            popupBox.setMessage("Please check  email and password again to login");
                            popupBox.setPositiveButton("Ok", null);
                            popupBox.show();
                        }
                    }
                });
    }
}