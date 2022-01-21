package com.android.nosh.src.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.nosh.R;
import com.android.nosh.src.customer.CustomerActivity;
import com.android.nosh.src.deliveryuser.DeliveryActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends Activity {

    private TextInputLayout emailText, passwordText;
    private Button loginButton, registerButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userTypeRef;
    private String isEmployee;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.textEmail);
        passwordText = findViewById(R.id.textPassword);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(v -> login());
        registerButton.setOnClickListener(v -> register());

        mAuth = FirebaseAuth.getInstance();
    }



    private void register() {

        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }


    private void login() {

        String email = emailText.getEditText().getText().toString();
        String password = passwordText.getEditText().getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        //Firebase sign-in function
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        //We will use this to query the database and determine if the user is an employee
                        //or a customer. For now, the login will take you straight to the CustomerActivity

                        Toast.makeText(this, "Authentication Successful", Toast.LENGTH_SHORT).show();
                        String userUID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                        userTypeRef = FirebaseDatabase.getInstance().getReference().child("users").child(userUID).child("isEmployee");

                        userTypeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                isEmployee = Objects.requireNonNull(snapshot.getValue()).toString();
                                Intent intent;
                                if (isEmployee.equals("true")) {
                                    intent = new Intent(LoginActivity.this, DeliveryActivity.class);
                                } else {
                                    intent = new Intent(LoginActivity.this, CustomerActivity.class);

                                }
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                                Toast.makeText(LoginActivity.this, "Successful Login", Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
            });
    }
}

