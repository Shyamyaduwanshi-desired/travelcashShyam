package view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.AddMoneyModel;
import presenter.AddMoneyPresenter;
import view.customview.CustomButton;
import view.customview.CustomTextViewBold;

public class AddMoney extends AppCompatActivity implements View.OnClickListener, AddMoneyPresenter.Money {
    private Toolbar toolbar;
    private AppCompatImageView imageView, imgPlus, imgMinus;
    private CustomButton btnProceed;
    private CustomTextViewBold tvAmount, tvOne, tvFive, tvTwo;
    private AddMoneyPresenter moneyPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);

        moneyPresenter = new AddMoneyPresenter(this, this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        btnProceed = findViewById(R.id.btnProceed);
        tvAmount = findViewById(R.id.tvAmount);
        imgPlus = findViewById(R.id.imgPlus);
        imgMinus = findViewById(R.id.imgMinus);
        tvOne = findViewById(R.id.tvOne);
        tvFive = findViewById(R.id.tvFive);
        tvTwo = findViewById(R.id.tvTwo);
        imageView = toolbar.findViewById(R.id.imgBack);
        imgPlus.setOnClickListener(this);
        imageView.setOnClickListener(this);
        imgMinus.setOnClickListener(this);
        btnProceed.setOnClickListener(this);
        tvOne.setOnClickListener(this);
        tvFive.setOnClickListener(this);
        tvTwo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                Animatoo.animateFade(AddMoney.this);
                break;

            case R.id.btnProceed:
                proceedPayment();
                break;

            case R.id.imgMinus:
                minus();
                break;

            case R.id.imgPlus:
                add();
                break;

            case R.id.tvOne:
                add(100);
                break;

            case R.id.tvFive:
                add(500);
                break;

            case R.id.tvTwo:
                add(2000);
                break;
        }
    }

    private void add(int value) {
        String amount = tvAmount.getText().toString().trim();
        amount = amount.replaceAll(",", "");
        int amt = Integer.parseInt(amount);
        if (amt == 0 || amt < 10000) {
            int val = amt + value;
            if (val <= 10000) {
                DecimalFormat df = new DecimalFormat("#,###,###,###");
                double dd = Double.parseDouble("" + val);
                tvAmount.setText("" + df.format(dd));
            } else
                showDialog();
        } else {
            showDialog();
        }
    }

    private void add() {
        String amount = tvAmount.getText().toString().trim();
        amount = amount.replaceAll(",", "");
        int amt = Integer.parseInt(amount);
        if (amt == 0 || amt < 10000) {
            int val = amt + 100;
            if (val <= 10000) {
                DecimalFormat df = new DecimalFormat("#,###,###,###");
                double dd = Double.parseDouble("" + val);
                tvAmount.setText("" + df.format(dd));
            } else
                showDialog();
        } else {
            showDialog();
        }
    }

    private void minus() {
        String amount = tvAmount.getText().toString().trim();
        amount = amount.replaceAll(",", "");
        int amt = Integer.parseInt(amount);
        if (amt > 100) {
            int val = amt - 100;
            DecimalFormat df = new DecimalFormat("#,###,###,###");
            double dd = Double.parseDouble("" + val);
            tvAmount.setText("" + df.format(dd));
        }
    }

    private void proceedPayment() {
        double amount = Double.parseDouble(tvAmount.getText().toString());
        if (amount != 0.0) {
            AddMoneyModel addMoneyModel = new AddMoneyModel();
            addMoneyModel.setAmount(tvAmount.getText().toString());
            addMoneyModel.setBank_name("ICIC Bank");
            addMoneyModel.setBank_txnID("TNX" + System.currentTimeMillis());
            addMoneyModel.setAcc_num("" + System.currentTimeMillis());
            addMoneyModel.setPayment_status("1");
            addMoneyModel.setPayment_tnxID("" + System.currentTimeMillis());
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date_time = s.format(new Date());
            addMoneyModel.setDate_time(date_time);
            if (isNetworkConnected())
                moneyPresenter.proceedPayment(addMoneyModel);
            else
                showDialog("Please connect to internet");
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Please Select Amount.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_CANCELED, intent);
                        finish();
                        Animatoo.animateFade(AddMoney.this);
                    }
                }).show();
    }

    private void showDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Please enter an amount lower than maximum transaction of $10,000.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void success(String response) {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
        Animatoo.animateFade(AddMoney.this);
    }

    @Override
    public void error(String response) {
        showDialog(response);
    }

    @Override
    public void fail(String response) {
        showDialog(response);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSlideRight(AddMoney.this);
    }
}
