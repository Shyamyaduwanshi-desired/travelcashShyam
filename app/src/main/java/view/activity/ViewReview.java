package view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import constant.AppData;
import model.UserReview;
import presenter.ProfilePresenter;
import view.adapter.RecentActivityAdapter;
import view.adapter.ViewReviewAdapter;

public class ViewReview extends AppCompatActivity implements ProfilePresenter.Profile {
    private Toolbar toolbar;
    private AppCompatImageView imgBack;
    private List<UserReview> mList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private AppData appData;
    private ProfilePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_review);

        presenter = new ProfilePresenter(this, this);
        appData = new AppData(this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        imgBack = toolbar.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        mList = new ArrayList<>();
        mAdapter = new ViewReviewAdapter(mList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
       // prepareData();

        if(isNetworkConnected()){
            presenter.viewReview(appData.getUserID());
        }else {
            showDialog("Please connect to internet.");
        }
    }

    @Override
    public void success(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("userRecievedReviews");
            for(int count = 0; count<jsonArray.length(); count++){
                JSONObject object = jsonArray.getJSONObject(count);
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
                String date = object.getString("review_date_time");
                Date date_change = input.parse(date);
                UserReview review = new UserReview(object.getString("agent_fname"), output.format(date_change),
                        object.getString("rating"), object.getString("review"));

                mList.add(review);
                mAdapter.notifyDataSetChanged();
            }
        }catch (ParseException ex) {
            ex.printStackTrace();
            mAdapter.notifyDataSetChanged();
            showDialog("Something went wrong. Please try again.");
        } catch (JSONException e){
            e.fillInStackTrace();
            mAdapter.notifyDataSetChanged();
            showDialog("Something went wrong. Please try again.");
        }
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
                        Animatoo.animateFade(ViewReview.this);
                    }
                }).show();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        finish();
        Animatoo.animateSlideRight(ViewReview.this);
    }
}
