package view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import model.TransactionModel;
import presenter.TransactionDetailPresenter;
import view.customview.CustomButton;
import view.customview.CustomTextView;

public class WalletTransactionDetail extends AppCompatActivity implements View.OnClickListener, TransactionDetailPresenter.Transaction {
    private Toolbar toolbar;
    private AppCompatImageView imageView;
    private CustomButton btnRepeat;
    private TransactionDetailPresenter presenter;
    private CustomTextView transactionID, bankTransactionID, refundID, tvDate, amountRequested, amountDebited;
    private String flag, transactionId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_transection_detail);

        presenter = new TransactionDetailPresenter(this, this);
        initView();
    }

    private void initView() {
        try {
            flag = getIntent().getStringExtra("flagPurchase");
            transactionId = getIntent().getStringExtra("transactionId");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            showDialog("something went wrong ");
        }

        toolbar = findViewById(R.id.toolbar);
        btnRepeat = findViewById(R.id.btnRepeat);
        imageView = toolbar.findViewById(R.id.imgBack);
        transactionID = findViewById(R.id.transactionID);
        bankTransactionID = findViewById(R.id.bankTransactionID);
        refundID = findViewById(R.id.refundID);
        tvDate = findViewById(R.id.tvDate);
        amountRequested = findViewById(R.id.amountRequested);
        amountDebited = findViewById(R.id.amountDebited);
        imageView.setOnClickListener(this);
        btnRepeat.setOnClickListener(this);

        if (flag.equals("1")) {
            btnRepeat.setText("Repeat Payment");
        } else if (flag.equals("0")) {
            btnRepeat.setText("Retry Payment");
        }

        if (isNetworkConnected()) {
            presenter.showWalletTransaction(transactionId);
        } else {
            showDialog("Please connect to internet");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                Animatoo.animateSlideRight(WalletTransactionDetail.this);
                break;

            case R.id.btnRepeat:
                finish();
                Animatoo.animateSlideRight(WalletTransactionDetail.this);
                break;
        }
    }

    @Override
    public void success(TransactionModel response) {
        if (response.getTransactionID().isEmpty())
            transactionID.setText("N/A");
        else
            transactionID.setText(response.getTransactionID());

        if (response.getBankTransactionID().isEmpty())
            bankTransactionID.setText("N/A");
        else
            bankTransactionID.setText(response.getBankTransactionID());

        if (response.getRefundID().isEmpty())
            refundID.setText("N/A");
        else
            refundID.setText(response.getRefundID());

        if (response.getDate().isEmpty())
            tvDate.setText("N/A");
        else
            tvDate.setText(response.getDate());

        if (response.getRequestAmount().isEmpty())
            amountRequested.setText("N/A");
        else
            amountRequested.setText(response.getRequestAmount());

        if (response.getDebitAmount().isEmpty())
            amountDebited.setText("N/A");
        else
            amountDebited.setText(response.getDebitAmount());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSlideRight(WalletTransactionDetail.this);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
