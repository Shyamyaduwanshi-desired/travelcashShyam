package view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import presenter.WriteReviewPresenter;
import view.customview.CustomButton;
import view.customview.CustomEditText;

public class WriteReview extends AppCompatActivity implements View.OnClickListener, WriteReviewPresenter.Review {
    private Toolbar toolbar;
    private AppCompatImageView imageView;
    private CustomButton btnSubmit;
    private AppCompatRatingBar ratingBar;
    private CustomEditText edtComment;
    private float ratingText;
    protected WriteReviewPresenter reviewPresenter;
    private String agentID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_review);

        reviewPresenter = new WriteReviewPresenter(this, this);
        try {
            agentID = getIntent().getStringExtra("agentID");
        }catch (NullPointerException ex){
            ex.printStackTrace();
            showDialog("Something went wrong. Pleas try again.");
        }

        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        imageView = toolbar.findViewById(R.id.imgBack);
        btnSubmit = findViewById(R.id.btnSubmit);
        ratingBar = findViewById(R.id.ratingBar);
        edtComment = findViewById(R.id.edtComment);
        imageView.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingText = rating;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            String comment = edtComment.getText().toString().trim();
            if (TextUtils.isEmpty(comment)) {
                showDialog("Please enter comment.");
            } else if (ratingText == 0) {
                showDialog("Please tap to rating.");
            } else if (isNetworkConnected()) {
                reviewPresenter.sendReview(agentID, "" + ratingText, comment);
            } else {
                showDialog("Please connect to internet.");
            }
        } else {
            finish();
            Animatoo.animateFade(WriteReview.this);
        }
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
    public void success(String response) {
//        new AlertDialog.Builder(this)
//                .setTitle("")
//                .setMessage(response)
//                .setCancelable(false)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.dismiss();
//                        finish();
//                        Animatoo.animateFade(WriteReview.this);
//                    }
//                }).show();
        ShowNewAlert(WriteReview.this,response);
    }

    @Override
    public void error(String response) {
        showDialog(response);
    }

    @Override
    public void fail(String response) {
        showDialog(response);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSlideRight(WriteReview.this);
    }

    PrettyDialog prettyDialog=null;
    private void ShowNewAlert(Context context,String message) {
        if(prettyDialog!=null)
        {
            prettyDialog.dismiss();
        }
        prettyDialog = new PrettyDialog(context);
        prettyDialog.setCanceledOnTouchOutside(false);
        TextView title = (TextView) prettyDialog.findViewById(libs.mjn.prettydialog.R.id.tv_title);
        TextView tvmessage = (TextView) prettyDialog.findViewById(libs.mjn.prettydialog.R.id.tv_message);
        title.setTextSize(15);
        tvmessage.setTextSize(15);
        prettyDialog.setIconTint(R.color.colorPrimary);
        prettyDialog.setIcon(R.drawable.pdlg_icon_info);
        prettyDialog.setTitle("");
        prettyDialog.setMessage(message);
        prettyDialog.setAnimationEnabled(false);
        prettyDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        prettyDialog.addButton("OK", R.color.black, R.color.white, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                prettyDialog.dismiss();
                finish();
                Animatoo.animateFade(WriteReview.this);


            }
        }).show();
    }
}
