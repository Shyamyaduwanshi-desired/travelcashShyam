package view.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import constant.AppData;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppData appData = new AppData(SplashActivity.this);
                String userID = appData.getUserID();
                String googleID = appData.getGoogleID();
                if(!userID.equals("NA")){
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    Animatoo.animateSlideUp(SplashActivity.this);
                }else if(!googleID.equals("NA")){
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    Animatoo.animateSlideUp(SplashActivity.this);
                }else {
                    Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                    Animatoo.animateSlideRight(SplashActivity.this);
                }

            }
        }, 3000);
    }
}
