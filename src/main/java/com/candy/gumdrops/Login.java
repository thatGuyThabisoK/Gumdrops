package com.candy.gumdrops;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {


    EditText email,password;

    String userPassword,userEmail;


    int[] mode = new int[1];
    SharedPreferences userDetails;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        mode[0] = 0;

        //make method to keep user signed in

        userDetails = getPreferences(Context.MODE_PRIVATE);



        if(userDetails.contains("email") && userDetails.contains("password")){

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Logging in.....");
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    progressDialog.cancel();

                }
            });
            progressDialog.show();

            String em = userDetails.getString("email","no mail");
            String pass = userDetails.getString("password","no password");

            FirebaseAuth.getInstance().signInWithEmailAndPassword(em,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressDialog.dismiss();

                                if(task.isSuccessful()){

                                    startActivity((new Intent(Login.this,DashBoard.class)));
                                    finish();

                                }else{
                                    Toast.makeText(Login.this, "Please check email/internet connection", Toast.LENGTH_LONG).show();
                                }
                            }
                        });



        }

    }





    public void login(View view){

        userEmail = email.getText().toString();
        userPassword = password.getText().toString();

        if(userEmail.isEmpty()){
            email.setError("no email was provided");
            return;
        }

        if(userPassword.isEmpty()){
            password.setError("no password provided");
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Logging in.....");
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressDialog.cancel();

            }
        });
        progressDialog.show();


        FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    if(mode[0] == 1){
                        startActivity((new Intent(Login.this,DashBoard.class)));
                    }else {
                        startActivity((new Intent(Login.this, DashBoard.class)
                                .putExtra("password", userPassword)
                                .putExtra("email", userEmail)));
                    }
                    finish();

                }else{
                    Toast.makeText(Login.this, "Please check email/password is correct", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void goToScreen(View view){

        userEmail = email.getText().toString();
        userPassword = password.getText().toString();

        if(view.getId() == R.id.logSignUp){

            startActivity(new Intent(this,Register.class));
            finish();

        }else if(view.getId() == R.id.logRemember){

            if(((CheckBox) view).isChecked() && !userEmail.isEmpty() && !userPassword.isEmpty()){


                editor = userDetails.edit();
                editor.putString("email",userEmail);
                editor.putString("password",userPassword);
                editor.apply();
                mode[0] = 1;



            }else{
                Toast.makeText(this, "one of the fields is empty", Toast.LENGTH_SHORT).show();
            }

        }else{

            //this is for when user cannot remember their password

            startActivity(new Intent(this,ForgotPassword.class));
            finish();

        }



    }







}
