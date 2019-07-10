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
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import presenter.TransferMoneyFriend;
import presenter.TransferMoneyToBank;
import view.customview.CustomButton;
import view.customview.CustomEditText;

public class TransferToBank extends AppCompatActivity implements View.OnClickListener, TransferMoneyToBank.TransferToBank {
    private Toolbar toolbar;
    private AppCompatImageView imgBack;
    private CustomButton btnContinue;
    private TextInputLayout usernameLayout;
    private CustomEditText edtUsername, edtAmount;
    private String flag;
//    private TransferMoneyFriend transferMoneyFriend;
    private TransferMoneyToBank transferMoneyToBank;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_to_bank);

//        transferMoneyFriend = new TransferMoneyFriend(this, this);
        transferMoneyToBank = new TransferMoneyToBank(this, this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        edtAmount = findViewById(R.id.edtAmount);
        edtUsername = findViewById(R.id.edtUsername);
        btnContinue = findViewById(R.id.btnContinue);
        usernameLayout = findViewById(R.id.usernameLayout);
        imgBack = toolbar.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        btnContinue.setOnClickListener(this);

        try {
            flag = getIntent().getStringExtra("flagPurchase");
            if (flag.equals("qr")) {
                usernameLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnContinue) {
            String username = edtUsername.getText().toString();
            String amount = edtAmount.getText().toString();
            if (flag.equals("qr")) {
                if (amount.isEmpty())
                    edtAmount.setError("Please enter amount to transfer.");
                else {
//                    Intent intent = new Intent(TransferToBank.this, ScanQR.class);
//                    intent.putExtra("amount", amount);
//                    startActivity(intent);
//                    finish();
//                    Animatoo.animateSlideLeft(TransferToBank.this);
                }
            } else {
                if (username.isEmpty())
                    edtUsername.setError("Please enter username.");
                else if (amount.isEmpty())
                    edtAmount.setError("Please enter amount to transfer.");
//                else if (isNetworkConnected())
//////                    transferMoneyFriend.usingQRToBank(username, amount);
//                    transferMoneyToBank.usingQRToBank(username, amount);
//                else
//                    showDialog("Please connect to internet.");
            }
        } else {
            finish();
            Animatoo.animateSlideRight(TransferToBank.this);
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
        Animatoo.animateFade(TransferToBank.this);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void success(String response) {
//        Toast toast = Toast.makeText(TransferToBank.this, response, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();

        finish();
        Animatoo.animateSlideRight(TransferToBank.this);
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
