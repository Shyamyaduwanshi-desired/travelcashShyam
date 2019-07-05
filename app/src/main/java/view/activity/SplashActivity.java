package view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import java.util.TimerTask;

import constant.AppData;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkPermissions();
    }
    private static final int REQUEST_CODE_PERMISSION = 2;
    private  void checkPermissions(){
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);

        } else if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);

        } else if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);

        } else   if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSION);
        }
        else   if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
        }
        else{
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermissions();
    }
}
