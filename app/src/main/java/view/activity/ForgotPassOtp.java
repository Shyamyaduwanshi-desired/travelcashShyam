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
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.arvind.otpview.OTPView;
import com.arvind.otpview.OnCompleteListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.goodiebag.pinview.Pinview;
import com.travelcash.R;

import constant.AppData;
import presenter.ForgotPasswordPresenter;
import presenter.OtpPresenter;
import view.customview.CustomButton;
import view.customview.CustomTextView;

public class ForgotPassOtp extends AppCompatActivity implements ForgotPasswordPresenter.Otp {
    private Toolbar toolbar;
    private AppCompatImageView imageView;
    private CustomButton btnContinue;
    //private OTPView otpView;
    private String otpString, mobile;
    private CustomTextView tvLabel, tvResend;
    private ForgotPasswordPresenter presenter;
    private Pinview otpView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_otp);

        presenter = new ForgotPasswordPresenter(ForgotPassOtp.this, ForgotPassOtp.this);
        initView();
    }

    private void initView() {
        try {
            mobile = getIntent().getStringExtra("mobile");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        toolbar = findViewById(R.id.toolbar);
        btnContinue = findViewById(R.id.btnContinue);
        tvLabel = findViewById(R.id.tvLabel);
        tvResend = findViewById(R.id.tvResend);
        imageView = toolbar.findViewById(R.id.imgBack);
        otpView = findViewById(R.id.otp_view);

        tvLabel.setText("Enter OTP sent to: +91-" + mobile);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassOtp.this, LoginActivity.class));
                finish();
                Animatoo.animateSlideRight(ForgotPassOtp.this);
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpString = otpView.getValue();
                if(TextUtils.isEmpty(otpString)){
                    showDialog("Please enter otp");
                }else {
                    if(isNetworkConnected()){
                        presenter.verifyOtp(mobile, otpString);
                    }else {
                        showDialog("Please connect to internet");
                    }
                }
            }
        });

        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0;i < otpView.getPinLength();i++) {
                    otpView.onKey(otpView.getFocusedChild(), KeyEvent.KEYCODE_DEL, new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_DEL));
                }
                if(isNetworkConnected()){
                    presenter.sentOtp(mobile);
                }else {
                    showDialog("Please connect to internet");
                }
            }
        });

    }

    @Override
    public void success(String response) {
       showDialog(response);
    }

    @Override
    public void verifyOTP(String response){
        Toast toast = Toast.makeText(ForgotPassOtp.this, response, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        Intent intent = new Intent(ForgotPassOtp.this, SetPassword.class);
        intent.putExtra("mobile", mobile);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Animatoo.animateSlideRight(ForgotPassOtp.this);
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
        super.onBackPressed();
        startActivity(new Intent(ForgotPassOtp.this, SignUpActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
