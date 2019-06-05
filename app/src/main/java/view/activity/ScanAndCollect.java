package view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.travelcash.R;
import java.util.ArrayList;
import java.util.List;
import constant.OrderData;
import view.customview.CustomButton;
import view.customview.CustomTextViewBold;
import view.fragment.CollectFragment;
import view.fragment.PurchaseFragment;

public class ScanAndCollect extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AppCompatImageView imgBack;
    private CustomButton btnConfirm;
    private CustomTextViewBold tvTitle;
    private OrderData orderData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_cashpoint);

        orderData = new OrderData(this);
        initView();
    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar);
        imgBack = toolbar.findViewById(R.id.imgBack);
        tvTitle = toolbar.findViewById(R.id.tvTitle);
        btnConfirm = findViewById(R.id.btnConfirm);
        tvTitle.setText("Goto Cashpoint");
        btnConfirm.setText("Scan & Collect");
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScanAndCollect.this, ScanActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
