package view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.barcode.Barcode;
import com.travelcash.R;

import java.util.List;

import presenter.TransferMoneyFriend;
import view.customview.CustomButton;
import view.customview.CustomTextViewBold;
import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;


public class ScanQR extends AppCompatActivity implements View.OnClickListener, BarcodeRetriever, TransferMoneyFriend.Transfer {
    private Toolbar toolbar;
    private AppCompatImageView imageView;
    private CustomButton btnCancel;
    private BarcodeCapture barcodeCapture;
    private CustomTextViewBold tvAmount;
    private TransferMoneyFriend transferMoneyFriend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_qr);

        transferMoneyFriend = new TransferMoneyFriend(this, this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        imageView = toolbar.findViewById(R.id.imgBack);
        btnCancel = findViewById(R.id.btnCancel);
        tvAmount = findViewById(R.id.tvAmount);
        imageView.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        barcodeCapture = (BarcodeCapture) getSupportFragmentManager().findFragmentById(R.id.barcode);
        barcodeCapture.setRetrieval(this);
        barcodeCapture.setShowDrawRect(true);

        tvAmount.setText("Amount to transfer : " + getIntent().getStringExtra("amount") + " IDR");
    }

    @Override
    protected void onDestroy() {
        barcodeCapture.stopScanning();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == btnCancel) {
            finish();
            Animatoo.animateSlideRight(ScanQR.this);
        } else if (v == imageView) {
            finish();
            Animatoo.animateSlideRight(ScanQR.this);
        }
    }

    @Override
    public void onRetrieved(final Barcode barcode) {
        barcodeCapture.stopScanning();
        //Log.d(TAG, "Barcode read: " + barcode.displayValue);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[] arr = barcode.displayValue.split("_");
                transferMoneyFriend.usingQR(arr[1], getIntent().getStringExtra("amount"));
            }
        });
    }

    @Override
    public void onRetrievedMultiple(final Barcode barcode, final List<BarcodeGraphic> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message = "Code selected : " + barcode.displayValue + "\n\nother " +
                        "codes in frame include : \n";
                for (int index = 0; index < list.size(); index++) {
                    Barcode barcode = list.get(index).getBarcode();
                    message += (index + 1) + ". " + barcode.displayValue + "\n";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(ScanQR.this)
                        .setTitle("code retrieved")
                        .setMessage(message);
                builder.show();
            }
        });
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onRetrievedFailed(String s) {

    }

    @Override
    public void onPermissionRequestDenied() {
        new AlertDialog.Builder(ScanQR.this)
                .setTitle("Alert")
                .setMessage("Please accept permission to scan QR code.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSlideRight(ScanQR.this);
    }

    @Override
    public void success(String response) {
        showDialog(response);
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
                        finish();
                        Animatoo.animateSlideRight(ScanQR.this);
                    }
                }).show();
    }
}
