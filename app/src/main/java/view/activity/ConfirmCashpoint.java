package view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import java.util.ArrayList;
import java.util.List;

import constant.OrderData;
import presenter.ConfirmCashpointPresenter;
import view.customview.CustomButton;
import view.fragment.CollectFragment;
import view.fragment.PurchaseFragment;

public class ConfirmCashpoint extends AppCompatActivity implements ConfirmCashpointPresenter.Cashpoint {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AppCompatImageView imgBack;
    private CustomButton btnConfirm;
    private OrderData orderData;
    private ConfirmCashpointPresenter confirmCashpointPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_cashpoint);

        orderData = new OrderData(this);
        confirmCashpointPresenter = new ConfirmCashpointPresenter(this, this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        imgBack = toolbar.findViewById(R.id.imgBack);
        btnConfirm = findViewById(R.id.btnConfirm);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Animatoo.animateFade(ConfirmCashpoint.this);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(isNetworkConnected())
                   confirmCashpointPresenter.sendRequest();
               else
                   showDialog("Please connect to internet");
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        int flagPurchase = orderData.flagPurchase();
        int flagPromos = orderData.flagPromos();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CollectFragment(), "Collect From");

        if (flagPurchase == 1) {
            adapter.addFragment(new PurchaseFragment(), "Purchase Required");
        }

        if (flagPromos == 0 && flagPurchase == 0) {
            adapter.addFragment(new PurchaseFragment(), "No Purchase Required");
        }

        if (flagPromos == 2) {
            adapter.addFragment(new PurchaseFragment(), "Promos");
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public void success(String response) {
        String amount  = orderData.requestAmount();
        orderData.clearData();
        Toast toast = Toast.makeText(this, response, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        Intent intent = new Intent(ConfirmCashpoint.this, ScanActivity.class);
        intent.putExtra("amount", amount);
        startActivity(intent);
        finish();
        Animatoo.animateFade(ConfirmCashpoint.this);
    }

    @Override
    public void error(String response) {
        showDialog(response);
    }

    @Override
    public void fail(String response) {
        showDialog(response);
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateFade(ConfirmCashpoint.this);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
