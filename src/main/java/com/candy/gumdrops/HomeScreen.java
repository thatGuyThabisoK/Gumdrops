package com.candy.gumdrops;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class HomeScreen  extends AppCompatActivity{



    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.home_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent toHomeScreen = new Intent(HomeScreen.this,DashBoard.class);
                startActivity(toHomeScreen);
                finish();
            }
        },5000);





    }



}
