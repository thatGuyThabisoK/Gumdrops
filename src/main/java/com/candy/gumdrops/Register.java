package com.candy.gumdrops;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    EditText nickname,password,phone_number,email,description;
    CheckBox terms;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        terms = findViewById(R.id.chkTandC);

        nickname = findViewById(R.id.regNickname);
        password = findViewById(R.id.regPassword);
        phone_number = findViewById(R.id.regPhoneNumber);
        email = findViewById(R.id.regEmail);
        description = findViewById(R.id.description);

    }

    public void getBacktoHomepage(View view){
        startActivity(new Intent(this,HomeScreen.class));
        finish();
    }

    public void createAccount(View view){
        if(terms.isChecked()){
            final String pass_word = password.getText().toString().trim();
            final String email_user = email.getText().toString().trim();
            if(pass_word.length() < 6){
                password.setError("required password of length greater than 6");
                return;
            }

            if(email_user.isEmpty()){
                email.setError("required user email");
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Hi,you are about to create a new account\nPlease confirm")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            auth.createUserWithEmailAndPassword(email_user,pass_word).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(Register.this,MainActivity.class));
                                        finish();
                                    }else{
                                        Toast.makeText(Register.this, "Registration failed try again later", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();






        }else{
            Toast.makeText(this, "you need to agree to proceed", Toast.LENGTH_SHORT).show();
        }


    }



}
