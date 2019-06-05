package view.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.rd.PageIndicatorView;
import com.travelcash.R;
import java.util.ArrayList;
import java.util.List;
import view.customview.CustomTextViewBold;
import view.fragment.FragmentOne;
import view.fragment.FragmentThree;
import view.fragment.FragmentTwo;

public class WelcomeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private PageIndicatorView pageIndicatorView;
    private CustomTextViewBold tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initViews();
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        tvSkip = findViewById(R.id.tvSkip);
        pageIndicatorView = findViewById(R.id.pageIndicatorView);
        setupViewPager(viewPager);
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentOne());
        adapter.addFragment(new FragmentTwo());
        adapter.addFragment(new FragmentThree());
        viewPager.setAdapter(adapter);
        pageIndicatorView.setCount(adapter.getCount());
        pageIndicatorView.setSelection(0);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

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

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }
}
