package com.android.nosh.src.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.nosh.R;
import com.android.nosh.src.customer.CustomerActivity;
import com.android.nosh.src.deliveryuser.DeliveryActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends Activity {

    private EditText regName, regEmail, regPassword;
    private CheckBox regCheckBox;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        //Data from views

        regName = findViewById(R.id.regNameBox);
        regEmail = findViewById(R.id.regEmailBox);
        regPassword = findViewById(R.id.regPassWordBox);
        regCheckBox = findViewById(R.id.regCheckBox);

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> register());
    }


    private void register(){

        // This is where we will put code for registration

        String name = regName.getText().toString().trim();
        String email = regEmail.getText().toString().trim();
        String password = regPassword.getText().toString().trim();
        boolean isEmployee = regCheckBox.isChecked();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {


                    if(task.isSuccessful())
                    {
                        Toast.makeText(RegisterActivity.this, "Authentication Successful", Toast.LENGTH_SHORT).show();
                        String userUID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                        DatabaseReference fbUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(userUID);
                        User user = new User(name, email, isEmployee);
                        fbUserRef.setValue(user);

                        Intent intent;

                        if(isEmployee){
                            intent = new Intent(RegisterActivity.this, DeliveryActivity.class);
                        }
                        else{
                            intent = new Intent(RegisterActivity.this, CustomerActivity.class);
                        }

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });

    }
}