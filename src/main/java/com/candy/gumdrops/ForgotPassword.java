package com.candy.gumdrops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }

    public void requestPassword(View view){

        EditText email = findViewById(R.id.forgotPassword);
        String userEmail = email.getText().toString().trim();

        if(!userEmail.isEmpty()) {

            FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgotPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(ForgotPassword.this, "Reset link sent to email", Toast.LENGTH_SHORT).show();

                }
            });

        }else{
            Toast.makeText(this, "Email field is empty", Toast.LENGTH_SHORT).show();
        }

    }




    public void backHome(View view) {

        startActivity(new Intent(this,HomeScreen.class));
        finish();

    }
}