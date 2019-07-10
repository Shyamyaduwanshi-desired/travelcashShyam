package view.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import model.AddMoneyModel;
import presenter.AgentLocPresenter;
import presenter.CancelOrderPresenter;
import presenter.ScanActivityPresenter;
import view.customview.CustomButton;
import view.fragment.DynamicFragment;
import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;


public class ScanActivity extends AppCompatActivity implements View.OnClickListener, BarcodeRetriever, ScanActivityPresenter.Scan, CancelOrderPresenter.CancelInfo, AgentLocPresenter.ShowAgentLoc {
    private Toolbar toolbar;
    private AppCompatImageView imageView;
    private CustomButton btnCancel,btnNavi;
    private BarcodeCapture barcodeCapture;
    private AppCompatTextView tvRN, tvAddress,tvName,tvPhone;
    private ScanActivityPresenter scanActivityPresenter;
    private String amount = "", request_id = "", agent_request_id = "", agentId = "";
    private CancelOrderPresenter cancelOrderPresenter;
    private AgentLocPresenter agenlocPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        cancelOrderPresenter = new CancelOrderPresenter(this, this);
        scanActivityPresenter = new ScanActivityPresenter(this, this);
        agenlocPresenter = new AgentLocPresenter(this, this);
        amount = getIntent().getStringExtra("amount");
        agent_request_id = getIntent().getStringExtra("agent_recieved_request_id");
        agentId = getIntent().getStringExtra("agent_id");
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        tvRN = findViewById(R.id.tvRN);
        tvAddress = findViewById(R.id.tvAddress);
        tvName = findViewById(R.id.tv_name);
        tvPhone = findViewById(R.id.tv_phone);


        imageView = toolbar.findViewById(R.id.imgBack);
        btnCancel = findViewById(R.id.btnCancel);
        btnNavi = findViewById(R.id.btn_navi);
        imageView.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnNavi.setOnClickListener(this);

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
//            ShowNewAlert(this,"Do you want to proceed cancel order?",agent_request_id);
            finish();
            Animatoo.animateSlideRight(ScanActivity.this);
        } else if (v == btnNavi) {

            if (isNetworkConnected()) {//navigate
                agenlocPresenter.GetAgentLoc(agentId);
            }
            else {
                showDialog("Please connect to internet");
            }
        }else if (v == imageView) {
            finish();
            Animatoo.animateSlideRight(ScanActivity.this);
        }
    }

    @Override
    public void onRetrieved(final Barcode barcode) {
        barcodeCapture.stopScanning();
        amount = amount.replaceAll(",", "");
        Log.e("","amount= "+amount+" request_id= "+request_id+" barcode.displayValue= "+barcode.displayValue);

        if (isNetworkConnected()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isNetworkConnected())
                        scanActivityPresenter.verifyAgentQrCode(barcode.displayValue, amount, request_id, agentId);
                    else
                        showDialog("Please connect to internet");
//                    scanActivityPresenter.verifyAgentQrCode(barcode.displayValue, amount, request_id, agentId);
                }
            });
        }
        else
            showDialog("Please connect to internet");

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

                        tvName.setText(jsonObject.getString("agent_name"));
                        tvPhone.setText(jsonObject.getString("agent_mobile_number"));


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
                params.put("agent_id", agentId);
//                params.put("agent_id", AppData.agentID);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(ScanActivity.this);
        queue.add(postRequest);
    }
//
    @Override
    public void success(String response, String user_withdraw_id) {

        Log.e("","user_withdraw_id= "+user_withdraw_id+" response= "+response);

//        Intent intent = new Intent(getApplicationContext(), TransactionDetail.class);
//        intent.putExtra("flagPurchase", "1");
//        intent.putExtra("transactionId", user_withdraw_id);
//        intent.putExtra("mode", "Total Cash Withdraw");
//        startActivity(intent);
//
//        finish();
//        Animatoo.animateSplit(ScanActivity.this);

        SuccessMsgAlert(this,response);
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

    //for cancelorder
    @Override
    public void success(String response) {

        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
        DynamicFragment.refreshCancle=1;
             finish();
            Animatoo.animateSlideRight(ScanActivity.this);
    }
    PrettyDialog prettyDialog=null;
    private void ShowNewAlert(Context context,String message,String requstid) {
        if(prettyDialog!=null)
        {
            prettyDialog.dismiss();
        }
        prettyDialog = new PrettyDialog(context);
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
        prettyDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        prettyDialog.addButton("No", R.color.black, R.color.white, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                prettyDialog.dismiss();
                finish();
                Animatoo.animateSlideRight(ScanActivity.this);
            }
        }).show();
        prettyDialog.addButton("Yes", R.color.black, R.color.white, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                prettyDialog.dismiss();
                if (isNetworkConnected()) {

                    cancelOrderPresenter.CancelOrderMethod(requstid);
                }
                else {
                    showDialog("Please connect to internet");
                }
            }
        }).show();
    }

    PrettyDialog SuccessMsgDlg=null;
    private void SuccessMsgAlert(Context context,String message) {
        if(SuccessMsgDlg!=null)
        {
            SuccessMsgDlg.dismiss();
        }
        SuccessMsgDlg = new PrettyDialog(context);
        SuccessMsgDlg.setCanceledOnTouchOutside(false);
        TextView title = (TextView) SuccessMsgDlg.findViewById(libs.mjn.prettydialog.R.id.tv_title);
        TextView tvmessage = (TextView) SuccessMsgDlg.findViewById(libs.mjn.prettydialog.R.id.tv_message);
        title.setTextSize(15);
        tvmessage.setTextSize(15);
        SuccessMsgDlg.setIconTint(R.color.colorPrimary);
        SuccessMsgDlg.setIcon(R.drawable.pdlg_icon_info);
        SuccessMsgDlg.setTitle("");
        SuccessMsgDlg.setMessage(message);
        SuccessMsgDlg.setAnimationEnabled(false);
        SuccessMsgDlg.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        SuccessMsgDlg.addButton("Ok", R.color.black, R.color.white, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                SuccessMsgDlg.dismiss();
                finish();
                Animatoo.animateSlideRight(ScanActivity.this);
            }
        }).show();

    }
    //for navigate presnter
    @Override
    public void success(String response, String lati, String longi) {
        if(TextUtils.isEmpty(lati)||TextUtils.isEmpty(longi)) {
            Toast.makeText(this, "not found latitude and longitude", Toast.LENGTH_SHORT).show();
        }else
        {

            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + lati + "," + longi));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }
    //for navigate presnter
    @Override
    public void error(String response, String check) {

    }
    //for navigate presnter
    @Override
    public void fail(String response, String check) {

    }
}
