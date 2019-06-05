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

import view.fragment.DiscoverFragment;
import view.fragment.HistoryFragment;
import view.fragment.HomeFragment;
import view.fragment.SettingFragment;
import view.fragment.WalletFragment;


public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        callFragment(new HomeFragment());
    }

    private void callFragment(Fragment selectedFragment) {
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
