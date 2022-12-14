package com.example.oncesecond;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://lets-eat-9573e-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText userName = findViewById(R.id.userName);
        final EditText password = findViewById(R.id.password);
        final EditText firstName = findViewById(R.id.firstName);
        final EditText lastName = findViewById(R.id.lastName);
        final EditText phoneNumber = findViewById(R.id.phoneNumber);
        final EditText confirmPassword = findViewById(R.id.confirmPassword);
        final Button signUpButton = findViewById(R.id.signUpButton);
        final TextView loginNowButton = findViewById(R.id.registerNowButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userNameText = userName.getText().toString();
                final String passwordText = password.getText().toString();
                final String confirmPasswordText = confirmPassword.getText().toString();
                final String firstNameText = firstName.getText().toString();
                final String lastNameText = lastName.getText().toString();
                final String phoneNumberText = phoneNumber.getText().toString();

                if (phoneNumberText.isEmpty() || passwordText.isEmpty() || firstNameText.isEmpty() || lastNameText.isEmpty() || confirmPasswordText.isEmpty()){
                    Toast.makeText(Register.this, "Please Fill all the inputs", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (!passwordText.equals(confirmPasswordText)){
                        Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {

                                if (snapshot.hasChild(phoneNumberText)){
                                    Toast.makeText(Register.this, "This user already exists", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    String fullName = firstNameText + lastNameText;
                                    databaseReference.child("Users").child(phoneNumberText).child("Name").setValue(fullName);
                                    databaseReference.child("Users").child(phoneNumberText).child("password").setValue(passwordText);
                                    databaseReference.child("Users").child(phoneNumberText).child("contact_number").setValue(phoneNumberText);
                                    databaseReference.child("Users").child(phoneNumberText).child("email").setValue(userNameText);

                                    Toast.makeText(Register.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Register.this, VerifyAccount.class));
                                    finish();
                                }

                            }

                            @Override
                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                            }
                        });

                    }
                }
            }
        });

        loginNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_intent = new Intent(Register.this, Login.class);
                startActivity(login_intent);
            }
        });
    }
}