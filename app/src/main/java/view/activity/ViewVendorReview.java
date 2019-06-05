package view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.travelcash.R;

import java.util.ArrayList;
import java.util.List;

import constant.AppData;
import model.UserReview;
import presenter.VendorReviewPresenter;
import view.adapter.ViewReviewAdapter;
import view.customview.CustomTextView;

public class ViewVendorReview extends AppCompatActivity implements VendorReviewPresenter.Review, View.OnClickListener {
    private Toolbar toolbar;
    private AppCompatImageView imgBack, imgProfile, tvNext;
    private CustomTextView tvName, tvCount, tvReview;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private VendorReviewPresenter reviewPresenter;
    private AppCompatRatingBar ratingBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_vendor_review);

        reviewPresenter = new VendorReviewPresenter(this, this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        imgBack = toolbar.findViewById(R.id.imgBack);
        tvNext = toolbar.findViewById(R.id.tvNext);
        imgBack.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        imgProfile = findViewById(R.id.imgProfile);
        tvName = findViewById(R.id.tvName);
        tvCount = findViewById(R.id.tvCount);
        tvReview = findViewById(R.id.tvReview);
        ratingBar = findViewById(R.id.ratingBar);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        try {
            String flag = getIntent().getStringExtra("flagPurchase");
            if (flag.equals("dis")) {
                tvNext.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(isNetworkConnected()){
            try {
                String agentID = getIntent().getStringExtra("agentID");
                reviewPresenter.getReview(agentID);
            }catch (NullPointerException ex){
                ex.printStackTrace();
                showDialog("Something went wrong, Pleas try again.");
            }
        }else {
            showDialog("Please connect to internet.");
        }
    }

    @Override
    public void success(String agentFname, String agentImage, String ratingAvrg, String reviewCount, ArrayList<UserReview> userReviews) {
        tvName.setText(agentFname);
        tvReview.setText(reviewCount + " Reviews");
        tvCount.setText("( " + ratingAvrg + " )");
        Glide.with(this).load( agentImage)
                .thumbnail(0.5f)
              /*  .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.persion)*/
                .into(imgProfile);
        ratingBar.setRating(Float.parseFloat(ratingAvrg));
        mAdapter = new ViewReviewAdapter(userReviews);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
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

    @Override
    public void onClick(View v) {
        if(v == imgBack){
            finish();
            Animatoo.animateSlideRight(ViewVendorReview.this);
        }

        if(v == tvNext){
            startActivity(new Intent(getApplicationContext(), ConfirmCashpoint.class));
            finish();
            Animatoo.animateSlideRight(ViewVendorReview.this);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        finish();
        Animatoo.animateSlideRight(ViewVendorReview.this);
    }
}
