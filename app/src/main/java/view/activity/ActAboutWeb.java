package view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import presenter.TransferMoneyFriend;
import view.customview.CustomButton;
import view.customview.CustomEditText;
import view.customview.CustomTextViewBold;

public class ActAboutWeb extends AppCompatActivity implements View.OnClickListener, TransferMoneyFriend.Transfer {
    private Toolbar toolbar;
    private AppCompatImageView imgBack;
    CustomTextViewBold tvTitle;
    WebView wv;
int diff=0;
//    private TransferMoneyFriend transferMoneyFriend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_about_web);

//        transferMoneyFriend = new TransferMoneyFriend(this, this);
        GetIntentData();
        initView();
        LoadData();
    }


    public void  GetIntentData() {
        diff=getIntent().getIntExtra("diff_",1);

    }

    private void LoadData() {
        wv.setWebViewClient(new WebViewClient());
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        wv.loadUrl("https://www.google.com");
        switch (diff)
        {
            case 1://about
                tvTitle.setText("About");
                break;
            case 2://help
                tvTitle.setText("Help");
                break;
            case 3://privacy policy
                tvTitle.setText("Privacy Policy");
                break;
        }

    }

    private void initView() {
        wv = findViewById(R.id.wv_about_web);
        toolbar = findViewById(R.id.toolbar);
        imgBack = toolbar.findViewById(R.id.imgBack);
        tvTitle = toolbar.findViewById(R.id.tv_title);

        imgBack.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        if (v == imgBack) {
            finish();
            Animatoo.animateSlideRight(ActAboutWeb.this);
        }
        else {

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
    public void onBackPressed() {
        finish();
        Animatoo.animateFade(ActAboutWeb.this);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void success(String response) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        finish();
        Animatoo.animateSlideRight(ActAboutWeb.this);
    }

    @Override
    public void error(String response) {
        showDialog(response);
    }

    @Override
    public void fail(String response) {
        showDialog(response);
    }
}
