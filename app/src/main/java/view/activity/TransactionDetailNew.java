package view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import model.TransactionModel;
import presenter.TransactionDetailPresenter;
import view.customview.CustomButton;
import view.customview.CustomTextView;

public class TransactionDetailNew extends AppCompatActivity implements View.OnClickListener , TransactionDetailPresenter.Transaction{
    private Toolbar toolbar;
    private AppCompatImageView imageView;
    private FloatingActionButton fab;
    private CustomTextView tvReview, transactionID, bankTransactionID, refundID, tvDate, amountRequested, amountDebited, tvAddress;
    private CustomButton btnRepeat;
    private String  transactionId,date, status,amount, agentID;
    private TransactionDetailPresenter detailPresenter;
    LinearLayout lyLoc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transection_detail_new);

        detailPresenter = new TransactionDetailPresenter(this, this);
        initView();
    }

    private void initView() {
        try {
            date = getIntent().getStringExtra("date");
            transactionId = getIntent().getStringExtra("transactionId");
            status = getIntent().getStringExtra("status");
            amount = getIntent().getStringExtra("amount");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            showDialog("something went wrong ");
        }

        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        tvReview = findViewById(R.id.tvReview);
        btnRepeat = findViewById(R.id.btnRepeat);
        imageView = toolbar.findViewById(R.id.imgBack);
        transactionID = findViewById(R.id.transactionID);
        bankTransactionID = findViewById(R.id.bankTransactionID);
        refundID = findViewById(R.id.refundID);
        tvDate = findViewById(R.id.tvDate);
        amountRequested = findViewById(R.id.amountRequested);
        amountDebited = findViewById(R.id.amountDebited);
        lyLoc = findViewById(R.id.ly_location);
        tvAddress = findViewById(R.id.tvAddress);
        imageView.setOnClickListener(this);
        fab.setOnClickListener(this);
//        fab.setVisibility(View.GONE);
        tvReview.setOnClickListener(this);
        btnRepeat.setOnClickListener(this);
      if(TextUtils.isEmpty(transactionId))
      {
          transactionID.setText("N/A");
          tvDate.setText(date);
          amountRequested.setText("IDR " +amount);
          amountDebited.setText("IDR "+amount);
          tvAddress.setText("N/A");
          lyLoc.setVisibility(View.GONE);
      }
      else
      {
          if (isNetworkConnected()) {
              getCall(status);
          } else {
              showDialog("Please connect to internet");
          }
      }

//        tvDate.setText(date);
//        amountRequested.setText("IDR " +amount);
//        amountDebited.setText("IDR "+amount);
//        tvAddress.setText("N/A");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvReview:
                Intent intent = new Intent(TransactionDetailNew.this, WriteReview.class);
                intent.putExtra("agentID", agentID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Animatoo.animateSlideRight(TransactionDetailNew.this);
                break;

            case R.id.fab:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

            case R.id.btnRepeat:
                finish();
                Animatoo.animateSlideRight(TransactionDetailNew.this);
                break;

            case R.id.imgBack:
                finish();
                Animatoo.animateSlideRight(TransactionDetailNew.this);
                break;
        }
    }

    @Override
    public void success(TransactionModel response) {
        agentID = response.getAgentID();
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
            amountRequested.setText("IDR "+response.getRequestAmount());

        if (response.getDebitAmount().isEmpty())
            amountDebited.setText("N/A");
        else
            amountDebited.setText("IDR "+response.getDebitAmount());

        if (response.getAddress() == null)
            tvAddress.setText("N/A");
        else
            tvAddress.setText(response.getAddress());
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

    private void getCall(String s) {
        switch (s) {
            case "Total Cash Withdraw":
                detailPresenter.showWithdrawTransactionInformation(transactionId);
                tvReview.setVisibility(View.VISIBLE);
                break;

            case "Total Transfer To Bank":
                detailPresenter.showTransferToBankTransaction(transactionId);
                break;

            case "Total Transfer To Friends":
                detailPresenter.showTransferToFriendTransaction(transactionId);
                break;

            case "Total Donated":
                detailPresenter.showDonatedTransaction(transactionId);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        Animatoo.animateSlideRight(TransactionDetailNew.this);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
