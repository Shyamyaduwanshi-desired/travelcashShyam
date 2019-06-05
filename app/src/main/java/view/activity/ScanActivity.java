package view.activity;

import android.app.ProgressDialog;
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
import android.util.SparseArray;
import android.view.SurfaceView;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.barcode.Barcode;
import com.travelcash.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import constant.AppData;
import model.AddMoneyModel;
import presenter.ScanActivityPresenter;
import view.customview.CustomButton;
import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;


public class ScanActivity extends AppCompatActivity implements View.OnClickListener, BarcodeRetriever, ScanActivityPresenter.Scan {
    private Toolbar toolbar;
    private AppCompatImageView imageView;
    private CustomButton btnCancel;
    private BarcodeCapture barcodeCapture;
    private AppCompatTextView tvRN, tvAddress;
    private ScanActivityPresenter scanActivityPresenter;
    private String amount = "", request_id = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        scanActivityPresenter = new ScanActivityPresenter(this, this);
        amount = getIntent().getStringExtra("amount");
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        tvRN = findViewById(R.id.tvRN);
        tvAddress = findViewById(R.id.tvAddress);
        imageView = toolbar.findViewById(R.id.imgBack);
        btnCancel = findViewById(R.id.btnCancel);
        imageView.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        barcodeCapture = (BarcodeCapture) getSupportFragmentManager().findFragmentById(R.id.barcode);
        barcodeCapture.setRetrieval(this);
        barcodeCapture.setShowDrawRect(true);

        if (isNetworkConnected())
            getRequestNumber();
        else
            showDialog("Please connect to internet.");
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
            Animatoo.animateSlideRight(ScanActivity.this);
        } else if (v == imageView) {
            finish();
            Animatoo.animateSlideRight(ScanActivity.this);
        }
    }

    @Override
    public void onRetrieved(final Barcode barcode) {
        //barcodeCapture.stopScanning();
        amount = amount.replaceAll(",", "");
        scanActivityPresenter.verifyAgentQrCode(barcode.displayValue, amount, request_id);

     /*
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this)
                        .setTitle("Code Retrieved")
                        .setMessage(barcode.displayValue)
                        //.setMessage("Transaction Successful.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                builder.show();
            }
        });
       */

    }

    @Override
    public void onRetrievedMultiple(final Barcode barcode, final List<BarcodeGraphic> list) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onRetrievedFailed(String s) {

    }

    @Override
    public void onPermissionRequestDenied() {
        showDialog("Please accept permission to scan QR code.");
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

    public void getRequestNumber() {
        final ProgressDialog progress = new ProgressDialog(ScanActivity.this);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "giveRequestNumber", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if (status == 1) {
                        JSONObject jsonObject = reader.getJSONObject("data");
                        request_id = jsonObject.getString("request_id");
                        tvRN.setText("Request Number : " + jsonObject.getString("request_id"));
                        tvAddress.setText(jsonObject.getString("agent_address"));
                    } else if (status == 0) {
                        showDialog(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showDialog("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                showDialog("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("agent_id", AppData.agentID);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(ScanActivity.this);
        queue.add(postRequest);
    }

    @Override
    public void success(String response, String user_withdraw_id) {
        Intent intent = new Intent(getApplicationContext(), TransactionDetail.class);
        intent.putExtra("flagPurchase", "1");
        intent.putExtra("transactionId", user_withdraw_id);
        intent.putExtra("mode", "Total Cash Withdraw");
        startActivity(intent);
        finish();
        Animatoo.animateSplit(ScanActivity.this);
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSlideRight(ScanActivity.this);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
