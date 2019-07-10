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
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import constant.AppData;
import presenter.TransferMoneyFriend;
import view.customview.CustomButton;
import view.customview.CustomEditText;

public class ActReferal extends AppCompatActivity implements View.OnClickListener, TransferMoneyFriend.Transfer {
    private Toolbar toolbar;
    private AppCompatImageView imgBack;
    private RelativeLayout rlShare;

    TextView tvReferalEarn;
//    private CustomEditText edtUsername, edtAmount;
private AppData appData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_referal);
        appData = new AppData(this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        rlShare = findViewById(R.id.rl_share);
        imgBack = toolbar.findViewById(R.id.imgBack);
        tvReferalEarn = findViewById(R.id.tv_earn_idr);
        imgBack.setOnClickListener(this);
        rlShare.setOnClickListener(this);

        String text = "<font color=#000000>"+getString(R.string.refer_txt)+"</font> <font color=#13acbe>"+getString(R.string.refer_txt1)+"</font>";
        tvReferalEarn.setText(Html.fromHtml(text));
    }

    @Override
    public void onClick(View v) {
        if (v == rlShare) {
            String code=appData.getReferalCode();
            if(TextUtils.isEmpty(code)||code.equals("0")||code=="0")
            {
                Toast.makeText(this, "You dont have referal code", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, code);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }

        } else {
            finish();
            Animatoo.animateSlideRight(ActReferal.this);
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
        Animatoo.animateFade(ActReferal.this);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void success(String response) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        finish();
        Animatoo.animateSlideRight(ActReferal.this);
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
