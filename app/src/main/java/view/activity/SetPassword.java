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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import presenter.ForgotPasswordPresenter;
import view.customview.CustomButton;
import view.customview.CustomEditText;

public class SetPassword extends AppCompatActivity implements ForgotPasswordPresenter.Otp, View.OnClickListener {
    private Toolbar toolbar;
    private AppCompatImageView imgBack;
    private CustomButton btnSubmit;
    private ForgotPasswordPresenter presenter;
    private CustomEditText edtPassword, edtConfirmPass;
    private String mobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_password);

        presenter = new ForgotPasswordPresenter(this, this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPass = findViewById(R.id.edtConfirmPass);
        btnSubmit = findViewById(R.id.btnSubmit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        imgBack = toolbar.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        try {
            mobile = getIntent().getStringExtra("mobile");
        } catch (NullPointerException e) {
            e.printStackTrace();
            showDialog("Something went wrong. Pleas try again.");
        }
    }

    @Override
    public void success(String response) {
        startActivity(new Intent(SetPassword.this, LoginActivity.class));
        finish();
        Animatoo.animateSlideLeft(SetPassword.this);
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
        if (v == btnSubmit) {
            String password = edtPassword.getText().toString().trim();
            String confirm_password = edtConfirmPass.getText().toString().trim();

            if (TextUtils.isEmpty(password)) {
                edtPassword.setError("Pleas enter password.");
            } else if (!isValidPassword(password) || password.length() < 8) {
                showDialog("Passwords must contain at least Eight characters, including characters, numbers and special characters and 1st letter must be in capital.");
            } else if (TextUtils.isEmpty(confirm_password)) {
                edtConfirmPass.setError("Please enter confirm password");
            } else if (!password.equals(confirm_password)) {
                edtConfirmPass.setError("Please enter same password and confirm password");
            } else if (isNetworkConnected()) {
                presenter.setPassword(mobile, password);
            } else {
                showDialog("Please connect to internet");
            }
        } else {
            startActivity(new Intent(SetPassword.this, ForgotPassword.class));
            finish();
            Animatoo.animateSlideLeft(SetPassword.this);
        }
    }

    private boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SetPassword.this, ForgotPassOtp.class));
        finish();
        Animatoo.animateSlideLeft(SetPassword.this);
    }
}
