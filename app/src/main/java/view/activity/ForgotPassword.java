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
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import presenter.ForgotPasswordPresenter;
import view.customview.CustomButton;
import view.customview.CustomEditText;

public class ForgotPassword extends AppCompatActivity implements ForgotPasswordPresenter.Otp {
    private CustomButton btnSubmit;
    private CustomEditText edtMobile;
    private ForgotPasswordPresenter presenter;
    private String mobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);

        presenter = new ForgotPasswordPresenter(this, this);
        edtMobile = findViewById(R.id.edtMobile);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = edtMobile.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    edtMobile.setError("Please enter mobile number.");
                } else if (mobile.length() < 6 || !isValidMobile(mobile)) {
                    edtMobile.setError("Please enter valid mobile number.");
                } else {
                    if(isNetworkConnected()){
                        presenter.sentOtp(mobile);
                    }else {
                      showDialog("Please connect to internet");
                    }
                }
            }
        });
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    @Override
    public void success(String response) {
        new AlertDialog.Builder(this)
                .setMessage(response)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), ForgotPassOtp.class);
                        intent.putExtra("mobile", mobile);
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideRight(ForgotPassword.this);
                    }
                }).show();

    }

    @Override
    public void error(String response) {
        showDialog(response);
    }

    @Override
    public void fail(String response) {
        showDialog(response);
    }

    @Override
    public void verifyOTP(String response) {

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
        Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
        startActivity(intent);
        finish();
        Animatoo.animateSlideDown(ForgotPassword.this);
    }

    /*check internet connection*/
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
