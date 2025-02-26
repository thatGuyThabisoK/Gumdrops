package com.candy.gumdrops;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DashBoard extends AppCompatActivity{

    private int ok_success = 111;
    static ArrayList<File> myPhotos = new ArrayList<>();
    ImageButton submit ;

    EditText message,nickname,gender,location,age;
    SharedPreferences uEmail;
    String from,userPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        submit = findViewById(R.id.btnSubmit);
        nickname = findViewById(R.id.edtNickname);
        gender = findViewById(R.id.edtGender);
        location = findViewById(R.id.edtLocation);
        age = findViewById(R.id.edtAge);




        Intent userEmail = getIntent();

        from = userEmail.getStringExtra("email");
        userPassword = userEmail.getStringExtra("password");



        if(from == null){
            uEmail = getSharedPreferences("Login", Context.MODE_PRIVATE);
            from = uEmail.getString("email","no email");
            userPassword = uEmail.getString("password","no pass");

        }


    }


    public void signOut(View view){

        FirebaseAuth.getInstance().signOut();

        if(uEmail != null) {

            SharedPreferences.Editor del = uEmail.edit();
            del.clear();
            del.apply();
        }

        startActivity(new Intent(this,MainActivity.class));
        finish();

    }

    public void sendEmail(View view){

        message = findViewById(R.id.dashDescription);
        String userMessage = message.getText().toString();
        String userNickname = nickname.getText().toString();
        String userAge = age.getText().toString();
        String userGender = gender.getText().toString();
        String userLocation = location.getText().toString();

        if(userMessage.isEmpty()){
            userMessage = "no message provided";
        }else if(userAge.isEmpty()){
            userAge = "age not provided";
        }else if(userLocation.isEmpty()){
                userLocation = "location not provided";
        }else if(userGender.isEmpty()){
                userGender = "gender not provided";
        }else if(userNickname.isEmpty()){
            userNickname = "nickname not provided";
        }


        String myMessage = "Hi\nI'm "+userNickname+" from "+userLocation+" and i am "+userGender+" my age is "+userAge+"\nThis is some information about me "+userMessage;


        if(from == null){
            Toast.makeText(this, "There is no email", Toast.LENGTH_SHORT).show();
        }else{
            if(!from.equalsIgnoreCase("no email")) {

                if(myPhotos.size() > 0){


                    JavaHotMail javaHotMail = new JavaHotMail(this, from,userPassword, myMessage,myPhotos);
                    javaHotMail.execute();

                }else{
                    Toast.makeText(this, "no photos to send", Toast.LENGTH_SHORT).show();
                }


            }

        }

        message.setText("");
        nickname.setText("");
        location.setText("");
        gender.setText("");
        age.setText("");






    }


    public String BitmapToString(Bitmap bitmap){
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,arrayOutputStream);
        byte[] bytes = arrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }


    private void addPhoto(){
        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePhoto,ok_success);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ok_success){
            ImageView imageView = new ImageView(DashBoard.this);
           if(data == null){
               Toast.makeText(this,"no photo taken",Toast.LENGTH_SHORT).show();
           }else {

               Bitmap picture = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
               imageView.setImageBitmap(picture);

               assert picture != null;
              // Uri tempUri = getImageUri(getApplicationContext(),picture);

               final File filepath = new File(getRealPathFromUri(data.getData()));

               new AlertDialog.Builder(DashBoard.this)
                       .setTitle("Photo ")
                       .setView(imageView)
                       .setNegativeButton("delete", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                               Toast.makeText(DashBoard.this, "photo has been deleted", Toast.LENGTH_SHORT).show();
                           }
                       })
                       .setPositiveButton("add", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               myPhotos.add(filepath);
                               Toast.makeText(DashBoard.this, "successfully added photo", Toast.LENGTH_SHORT).show();

                           }
                       }).create().show();

           }

        }

    }



    public Uri getImageUri(Context context,Bitmap image){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),image,"MyPhoto",null);
        return Uri.parse(path);
    }

    public String getRealPathFromUri(Uri uri){
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void takePicture(View view){

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            addPhoto();

        }else{
            requestCamera();
        }

    }


    private void requestCamera(){

        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){

            new AlertDialog.Builder(this)
                    .setTitle("Permission ")
                    .setMessage("This permission is needed to gain access to the camera")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(DashBoard.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},ok_success);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        }else{
            ActivityCompat.requestPermissions(DashBoard.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},ok_success);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == ok_success && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
            addPhoto();
        }else{
            Toast.makeText(this, "You need to allow all the permissions", Toast.LENGTH_SHORT).show();
        }
    }




}
