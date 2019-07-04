package view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.travelcash.R;

import presenter.QrCodePresenter;
import view.customview.CustomButton;

public class ActScanQR extends AppCompatActivity implements View.OnClickListener, QrCodePresenter.QrCode {
    private Toolbar toolbar;
    private AppCompatImageView imageView, imgQRCode;
    private CustomButton btnCancel;
    private QrCodePresenter qrCodePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        qrCodePresenter = new QrCodePresenter(this, this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        imageView = toolbar.findViewById(R.id.imgBack);
        imgQRCode = findViewById(R.id.imgQRCode);
        btnCancel = findViewById(R.id.btnCancel);
        imageView.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        if(isNetworkConnected())
            qrCodePresenter.sentRequest();
        else
            showDialog("Please connect to internet.");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                Animatoo.animateFade(ActScanQR.this);
                break;

            case R.id.btnCancel:
                finish();
                Animatoo.animateFade(ActScanQR.this);
                break;
        }
    }

    @Override
    public void success(String response) {
        Glide.with(this).load(response)
                .thumbnail(0.5f)
                /*.crossFade()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.persion)*/
                .into(imgQRCode);
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

    /*check internet connection*/
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateFade(ActScanQR.this);
    }
}
