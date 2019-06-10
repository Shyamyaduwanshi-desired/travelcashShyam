package view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import constant.AppData;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import presenter.ChangePasswordPresenter;
import view.customview.CustomButton;
import view.customview.CustomEditText;

public class ChangePassword extends AppCompatActivity implements ChangePasswordPresenter.PasswordChange {
    private Toolbar toolbar;
    private AppCompatImageView imgBack;
    private CustomButton btnSubmit;
    private CustomEditText edtOldPass, edtNewPass, edtConfirmPass;
    private ChangePasswordPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        presenter = new ChangePasswordPresenter(this, this);

        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        edtOldPass = findViewById(R.id.edtOldPass);
        edtNewPass = findViewById(R.id.edtNewPass);
        edtConfirmPass = findViewById(R.id.edtConfirmPass);
        btnSubmit = findViewById(R.id.btnSubmit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        imgBack = toolbar.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Animatoo.animateFade(ChangePassword.this);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = edtOldPass.getText().toString().trim();
                String newPass = edtNewPass.getText().toString().trim();
                String confPass = edtConfirmPass.getText().toString().trim();
                AppData appData = new AppData(ChangePassword.this);

                if (TextUtils.isEmpty(oldPass)) {
                    showAlert("Please enter old password.");
                } else if (TextUtils.isEmpty(newPass)) {
                    showAlert("Please enter new password");
                } else if (!isValidPassword(newPass) || newPass.length() < 8) {
                    showAlert("Your wallet balance is less than transaction amount, Please Add money to Wallet and continue with transaction");
                } else if (TextUtils.isEmpty(confPass)) {
                    showAlert("Please enter confirm password");
                } else if (!newPass.equals(confPass)) {
                    showAlert("Please enter same new password and confirm password");
                } else {
                    if (isNetworkConnected())
                        presenter.sentRequest(appData.getUserID(), oldPass, newPass);
                    else
                        showAlert("Please connect to internet.");
                }
            }
        });
    }

    private void showAlert(String message) {
        final PrettyDialog prettyDialog = new PrettyDialog(this);
        prettyDialog.setCanceledOnTouchOutside(false);
        TextView title = (TextView) prettyDialog.findViewById(libs.mjn.prettydialog.R.id.tv_title);
        TextView tvmessage = (TextView) prettyDialog.findViewById(libs.mjn.prettydialog.R.id.tv_message);
        title.setTextSize(15);
        tvmessage.setTextSize(15);
        prettyDialog.setIconTint(R.color.colorPrimary);
        prettyDialog.setIcon(R.drawable.pdlg_icon_info);
        prettyDialog.setMessage(message);
        prettyDialog.setAnimationEnabled(false);
        prettyDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        prettyDialog.addButton("Ok", R.color.black, R.color.white, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                prettyDialog.dismiss();
            }
        }).show();
    }

    private boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[*@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public void success(String response) {
        Toast toast = Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        AppData appData = new AppData(ChangePassword.this);
        appData.clearData();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        Animatoo.animateFade(this);
    }

    @Override
    public void error(String response) {
        showAlert(response);
    }

    @Override
    public void fail(String response) {
        showAlert(response);
    }

    @Override
    public void onBackPressed() {
        finish();
        Animatoo.animateFade(ChangePassword.this);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
