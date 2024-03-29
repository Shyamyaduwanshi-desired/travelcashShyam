package view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
/*import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;*/
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.travelcash.R;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import presenter.DeviceTokenPresenter;
import presenter.LoginPresenter;
import view.customview.CustomButton;
import view.customview.CustomEditText;
import view.customview.CustomTextViewBold;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginPresenter.Login, DeviceTokenPresenter.SaveDeviceToken {
    private CustomTextViewBold tvForgotPassword, tvSignUp;
    private CustomButton btnLogin;
    private CustomEditText edtUsername, edtPassword;
    private LoginPresenter presenter;
    private DeviceTokenPresenter DeviceTokenPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        presenter = new LoginPresenter(LoginActivity.this, LoginActivity.this);
        DeviceTokenPresenter = new DeviceTokenPresenter(LoginActivity.this, LoginActivity.this);

        initView();
    }

    private void initView() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);

        btnLogin.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            login();
        } else if (v == tvForgotPassword) {
            Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else if (v == tvSignUp) {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            finish();
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }

    private void login() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (username.isEmpty()) {
           showAlert("Please enter email/mobile number", R.style.DialogAnimation);
        }else if(password.isEmpty()){
            showAlert("Please enter password", R.style.DialogAnimation);
        }else {
            if(isNetworkConnected()){
               presenter.sentRequest(username, password);
            }else {
                showAlert("Please connect to internet", R.style.DialogAnimation);
            }
        }
    }

    private void showAlert(String message, int animationSource){
        final PrettyDialog prettyDialog = new PrettyDialog(this);
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
        prettyDialog.getWindow().getAttributes().windowAnimations = animationSource;
        prettyDialog.addButton("Ok", R.color.black, R.color.white, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                prettyDialog.dismiss();
            }
        }).show();
    }

    @Override
    public void success(String response) {
        GetFCMToken();
//        Toast toast = Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();
//
//        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//        startActivity(intent);
//        finish();
//        Animatoo.animateSlideUp(LoginActivity.this);

    }
//for device token save success
    @Override
    public void success(String response, String check) {

//        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
        Animatoo.animateSlideUp(LoginActivity.this);
    }
    //for device token error
    @Override
    public void error(String response, String check) {//optional
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
        Animatoo.animateSlideUp(LoginActivity.this);

    }
    //for device token fail
    @Override
    public void fail(String response, String check) {//optional
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
        Animatoo.animateSlideUp(LoginActivity.this);

    }

    @Override
    public void error(String response) {
        showAlert(response, R.style.DialogAnimation);
    }

    @Override
    public void fail(String response) {
        showAlert(response, R.style.DialogAnimation);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginActivity.this, StartActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public void GetFCMToken()
    {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

// Get the Instance ID token//
                        String token = task.getResult().getToken();
//                        String msg = getString(R.string.fcm_token, token);
                        Log.d("shyam Token ", "shyam fcm token= "+token);

                        DeviceTokenPresenter.SaveToken(token);

                    }
                });

    }
}
