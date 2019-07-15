package view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import constant.AppData;
import view.fragment.DiscoverFragment;
import view.fragment.HistoryFragment;
import view.fragment.HomeFragment;
import view.fragment.SettingFragment;
import view.fragment.WalletFragment;


public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private AppData appData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        appData = new AppData(this);


        bottomNavigationView = findViewById(R.id.navigation);
//        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

//        callFragment(new HomeFragment());
        setView();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setView();

    }
public void setView()
{
    String setNoti=appData.getNotiClick();
    switch (setNoti)
    {
        case "0"://default(home)
            appData.setNotiClick("0");
            bottomNavigationView.setSelectedItemId(R.id.home);
            callFragment(new HomeFragment());
            break;
        case "1"://history
            appData.setNotiClick("0");
            bottomNavigationView.setSelectedItemId(R.id.history);
            callFragment(new HistoryFragment());
            break;
    }
}
    private void callFragment(Fragment selectedFragment) {//default
        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;
        switch (menuItem.getItemId()) {
            case R.id.setting:
                selectedFragment = SettingFragment.getInstance();
                callFragment(selectedFragment);
                break;
            case R.id.home:
                selectedFragment = HomeFragment.getInstance();
                callFragment(selectedFragment);
                break;
            case R.id.history:
                selectedFragment = HistoryFragment.getInstance();
                callFragment(selectedFragment);
                break;

            case R.id.discover:
                selectedFragment = DiscoverFragment.getInstance();
                callFragment(selectedFragment);
                break;

            case R.id.wallet:
                selectedFragment = WalletFragment.getInstance();
                callFragment(selectedFragment);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("Are you sure want to Exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
