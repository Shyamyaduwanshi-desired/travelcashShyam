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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arvind.otpview.OTPView;
import com.arvind.otpview.OnCompleteListener;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.goodiebag.pinview.Pinview;
import com.travelcash.R;

import constant.AppData;
import constant.SignUpPreference;
import presenter.OtpPresenter;
import view.customview.CustomButton;
import view.customview.CustomTextView;

public class VerifyOtp extends AppCompatActivity implements OtpPresenter.Otp {
    private Toolbar toolbar;
    private AppCompatImageView imageView;
    private CustomButton btnContinue;
    //private OTPView otpView;
    private String otpString;
    private CustomTextView tvLabel, tvResend;
    private OtpPresenter presenter;
    private Pinview otpView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_otp);

        presenter = new OtpPresenter(VerifyOtp.this, VerifyOtp.this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btnContinue = findViewById(R.id.btnContinue);
        tvLabel = findViewById(R.id.tvLabel);
        tvResend = findViewById(R.id.tvResend);
        imageView = toolbar.findViewById(R.id.imgBack);
        otpView = findViewById(R.id.otp_view);
        String mobile = SignUpPreference.getInstance(VerifyOtp.this).getValue("mobile");
        tvLabel.setText("Enter OTP sent to: -" + mobile);//+91
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VerifyOtp.this, SignUpActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
                if(isNetworkConnected()){
                    presenter.resentOtp(mobile);
                }else {
                    showDialog("Please connect to internet");
                }
            }
        });

    }

    @Override
    public void success(String response, String type) {
        if(type.equals("otp")){
            new AlertDialog.Builder(this)
                    .setMessage(response)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            presenter.register();
                        }
                    }).show();
        }else if(type.equals("register")){
            Toast toast = Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            SignUpPreference.getInstance(VerifyOtp.this).clear();
            Intent intent = new Intent(VerifyOtp.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            Animatoo.animateSlideUp(VerifyOtp.this);
        }
    }

    @Override
    public void success(String response) {
        new AlertDialog.Builder(this)
                .setMessage(response)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void error(String response, String type) {
        showDialog(response);
    }

    @Override
    public void fail(String response, String type) {
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
    public void onBackPressed() {
        startActivity(new Intent(VerifyOtp.this, SignUpActivity.class));
        finish();
        Animatoo.animateSlideDown(VerifyOtp.this);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
