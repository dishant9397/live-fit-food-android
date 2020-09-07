package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginWithPhoneActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    String codeSent;
    EditText phoneNumberEditText, codeEditText;
    Button sendCodeButton, loginButton;
    Intent intent;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_phone);
        firebaseAuth = FirebaseAuth.getInstance();
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        sendCodeButton = findViewById(R.id.sendCodeButton);
        codeEditText = findViewById(R.id.code);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setEnabled(false);
        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberEditText.getText().toString();
                if (phoneNumber.length() == 12) {
                    sendVerificationCode(phoneNumber);
                }
                else {
                    AlertDialog.Builder popupBox = new AlertDialog.Builder(LoginWithPhoneActivity.this);
                    popupBox.setTitle("Incomplete Details");
                    popupBox.setMessage("Please fill Phone Number to send a code");
                    popupBox.setPositiveButton("Ok", null);
                    popupBox.show();
                }
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeEditText.getText().toString();
                if (code.equals("")) {
                    AlertDialog.Builder popupBox = new AlertDialog.Builder(LoginWithPhoneActivity.this);
                    popupBox.setTitle("Incomplete details");
                    popupBox.setMessage("Please enter complete code to login");
                    popupBox.setPositiveButton("Ok", null);
                    popupBox.show();
                }
                else {
                    verifyVerificationCode(code);
                }
            }
        });
    }

    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            intent = new Intent(LoginWithPhoneActivity.this, UserMealListActivity.class);
                            startActivity(intent);
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                AlertDialog.Builder popupBox = new AlertDialog.Builder(LoginWithPhoneActivity.this);
                                popupBox.setTitle("Incorrect Code");
                                popupBox.setMessage("Please fill correct code to login");
                                popupBox.setPositiveButton("Ok", null);
                                popupBox.show();
                            }
                        }
                    }
                });
    }

    private void sendVerificationCode(String phoneNumber) {
        loginButton.setEnabled(true);
        phoneNumberEditText.setEnabled(false);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                LoginWithPhoneActivity.this,
                mCallbacks);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {

            } else if (e instanceof FirebaseTooManyRequestsException) {

            }
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
        }
    };

}