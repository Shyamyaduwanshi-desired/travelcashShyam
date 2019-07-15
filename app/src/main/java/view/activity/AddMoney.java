package view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import constant.AppData;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import model.AddMoneyModel;
import model.GraphModel;
import model.Vendor;
import presenter.AddMoneyPresenter;
import presenter.GetBankDetailsPresenter;
import presenter.GraphPresenter;
import view.customview.CustomButton;
import view.customview.CustomTextView;
import view.customview.CustomTextViewBold;

public class AddMoney extends AppCompatActivity implements View.OnClickListener, GetBankDetailsPresenter.BankDetals, GraphPresenter.GraphInfo {
    private Toolbar toolbar;
    private AppCompatImageView imageView, imgPlus, imgMinus;
    private CustomButton btnProceed;
    private CustomTextViewBold tvAmount, tvOne, tvFive, tvTwo;
//    private AddMoneyPresenter moneyPresenter;
    private GetBankDetailsPresenter bankDetailsPresenter;
    private GraphPresenter graphPresenter;
    AppData appData;
    LineChartView lineChartView;
    CustomTextView tvRate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        appData=new AppData(this);
        arGraphData=new ArrayList<>();
//        moneyPresenter = new AddMoneyPresenter(this, this);

        graphPresenter = new GraphPresenter(this, this);
        bankDetailsPresenter = new GetBankDetailsPresenter(this, this);
                    if (isNetworkConnected()) {
                        getCurrentExchangeRate();
                        bankDetailsPresenter.GetBankDetail();
                        graphPresenter.getGraphInfo();
                    }
               else {
                        showDialog("Please connect to internet");
                    }
        initView();
//        ShowChart();
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
        tvRate = findViewById(R.id.tv_rate);
        imageView = toolbar.findViewById(R.id.imgBack);
        imgPlus.setOnClickListener(this);
        imageView.setOnClickListener(this);
        imgMinus.setOnClickListener(this);
        btnProceed.setOnClickListener(this);
        tvOne.setOnClickListener(this);
        tvFive.setOnClickListener(this);
        tvTwo.setOnClickListener(this);

        lineChartView = findViewById(R.id.chart);
        lineChartView.setClickable(false);
        lineChartView.setOnTouchListener(null);
        lineChartView.setZoomEnabled(false);
        lineChartView.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                Animatoo.animateFade(AddMoney.this);
                break;

            case R.id.btnProceed:
                ProceedDetails();

//                proceedPayment();
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




    public void ProceedDetails()
    {
        String amount = tvAmount.getText().toString().trim();
        amount = amount.replaceAll(",", "");
        int amt = Integer.parseInt(amount);

        if(amt>0)
        {
            if (sBankdetail.equals("error") || sBankdetail.equals("fail") || TextUtils.isEmpty(sBankdetail)) {
                Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            } else {
                ShowBankDetails();
            }
        }
        else {
            showDialog();
        }
    }

    private void add(int value) {
        String amount = tvAmount.getText().toString().trim();
        amount = amount.replaceAll(",", "");
        int amt = Integer.parseInt(amount);
        if (amt == 0 || amt < 2000)//10000
        {
            int val = amt + value;
            if (val <= 2000)//10000
            {
                DecimalFormat df = new DecimalFormat("#,###,###,###");
                double dd = Double.parseDouble("" + val);
                tvAmount.setText("" + df.format(dd));
            }
            else
                showDialog();
        }
        else {
            showDialog();
        }
    }

    private void add() {
        String amount = tvAmount.getText().toString().trim();
        amount = amount.replaceAll(",", "");
        int amt = Integer.parseInt(amount);
        if (amt == 0 || amt < 2000)//10000
        {
            int val = amt + 50;//100
            if (val <= 2000)//10000
            {
                DecimalFormat df = new DecimalFormat("#,###,###,###");
                double dd = Double.parseDouble("" + val);
                tvAmount.setText("" + df.format(dd));
            }
            else
                showDialog();
        }
        else {
            showDialog();
        }
    }

    private void minus() {
        String amount = tvAmount.getText().toString().trim();
        amount = amount.replaceAll(",", "");
        int amt = Integer.parseInt(amount);
        if (amt > 50) {//100
            int val = amt - 50;//100
            DecimalFormat df = new DecimalFormat("#,###,###,###");
            double dd = Double.parseDouble("" + val);
            tvAmount.setText("" + df.format(dd));
        }
    }

//    private void proceedPayment() {
//        double amount = Double.parseDouble(tvAmount.getText().toString());
//        if (amount != 0.0) {
//            AddMoneyModel addMoneyModel = new AddMoneyModel();
//            addMoneyModel.setAmount(tvAmount.getText().toString());
//            addMoneyModel.setBank_name("ICIC Bank");
//            addMoneyModel.setBank_txnID("TNX" + System.currentTimeMillis());
//            addMoneyModel.setAcc_num("" + System.currentTimeMillis());
//            addMoneyModel.setPayment_status("1");
//            addMoneyModel.setPayment_tnxID("" + System.currentTimeMillis());
//            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//            String date_time = s.format(new Date());
//            addMoneyModel.setDate_time(date_time);
//        } else {
//            Toast toast = Toast.makeText(getApplicationContext(), "Please Select Amount.", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        }
//    }

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
                .setMessage("Please enter an amount lower than maximum transaction of $2,000.")//10,000
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).show();
    }
String sBankdetail="";
    @Override
    public void success(String response) {
        sBankdetail=response;
//        ShowNewAlert(AddMoney.this,response);


    }
    private ArrayList<GraphModel> arGraphData=new ArrayList<>();
//for graph data
    @Override
    public void success(ArrayList<GraphModel> response) {
        this.arGraphData.clear();
        this.arGraphData = response;
        ShowChart();
    }

    @Override
    public void error(String response) {
        sBankdetail="error";//fail
    }

    @Override
    public void fail(String response) {
        sBankdetail="fail";
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

    PrettyDialog prettyDialog=null;
    private void ShowNewAlert(Context context,String message) {
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
        prettyDialog.addButton("OK", R.color.black, R.color.white, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                prettyDialog.dismiss();
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
                Animatoo.animateFade(AddMoney.this);


            }
        }).show();
    }

    private void ShowBankDetails(){

        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(AddMoney.this);
        LayoutInflater inflater = AddMoney.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.bank_details_dialog, null);
        dialogBuilder.setView(dialogView);
         android.app.AlertDialog bankDetailDlg = dialogBuilder.create();

         TextView tvCloseDlg = dialogView.findViewById(R.id.tv_close);
//         TextView tvBankAccnt = dialogView.findViewById(R.id.tv_bank_no);
         WebView
                 webViewBank = dialogView.findViewById(R.id.wv_bank_no);
//         TextView tvBankNm = dialogView.findViewById(R.id.tv_bank_name);
        CustomButton btProcee = dialogView.findViewById(R.id.btn_proceed);

//        tvBankAccnt.setText(Html.fromHtml(sBankdetail));
Log.e("","sBankdetail= "+sBankdetail);
        webViewBank.setWebViewClient(new WebViewClient());
        webViewBank.getSettings().setJavaScriptEnabled(true);
        webViewBank.getSettings().setDomStorageEnabled(true);
        webViewBank.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webViewBank.loadDataWithBaseURL("", sBankdetail, "text/html", "utf-8", "");

        tvCloseDlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankDetailDlg.dismiss();

            }
        });
        btProcee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AddMoney.this, AddMoneyTransactionDetail.class).putExtra("amount",tvAmount.getText().toString()));
                Animatoo.animateSlideRight(AddMoney.this);
                bankDetailDlg.dismiss();
                tvAmount.setText("0");

            }
        });
        bankDetailDlg.show();
    }
public void ShowChart()
{
    List yAxisValues = new ArrayList();
    List axisValues = new ArrayList();
    Line line = new Line(yAxisValues).setColor(Color.parseColor("#13acbe"));
    for (int i = 0; i < arGraphData.size(); i++) {
        axisValues.add(i, new AxisValue(i).setLabel(appData.ConvertDate3(arGraphData.get(i).getDate())));
    }

    for (int i = 0; i < arGraphData.size(); i++) {
        float value=Float.parseFloat(arGraphData.get(i).getRate());
        int value1=Math.round(Math.round(value));
        Log.e("","value= "+value+" value1= "+value1);
        yAxisValues.add(new PointValue(i, value1));
    }
    List lines = new ArrayList();
    lines.add(line);
    LineChartData data = new LineChartData();
    data.setLines(lines);
    Axis axis = new Axis();
    axis.setValues(axisValues);
    axis.setTextSize(12);
    axis.setTextColor(Color.parseColor("#000000"));
    Axis yAxis = new Axis();
    yAxis.setTextSize(12);
    yAxis.setTextColor(Color.parseColor("#000000"));
    data.setAxisXBottom(axis);
    data.setAxisYLeft(yAxis);
    lineChartView.setLineChartData(data);

}
//public void ShowChart11()
//{
//    List yAxisValues = new ArrayList();
//    List axisValues = new ArrayList();
//    Line line = new Line(yAxisValues).setColor(Color.parseColor("#13acbe"));
//    for (int i = 0; i < arGraphData.size(); i++) {
//        axisValues.add(i, new AxisValue(i).setLabel(appData.ConvertDate3(arGraphData.get(i).getDate())));
//    }
//
//    for (int i = 0; i < arGraphData.size(); i++) {
//        float value=Float.parseFloat(arGraphData.get(i).getRate());
//        int value1=Math.round(Math.round(value));
//        Log.e("","value= "+value+" value1= "+value1);
//        yAxisValues.add(new PointValue(i, value1));
//    }
//    List lines = new ArrayList();
//    lines.add(line);
//    LineChartData data = new LineChartData();
//    data.setLines(lines);
//    Axis axis = new Axis();
//    axis.setValues(axisValues);
//    axis.setTextSize(12);
//    axis.setTextColor(Color.parseColor("#000000"));
//    data.setAxisXBottom(axis);
//    Axis yAxis = new Axis();
//    yAxis.setTextSize(12);
//    yAxis.setTextColor(Color.parseColor("#000000"));
//    data.setAxisYLeft(yAxis);
//    lineChartView.setLineChartData(data);
//}


    public void getCurrentExchangeRate() {

        StringRequest postRequest = new StringRequest(Request.Method.GET, AppData.url + "getCurrentExchangeRate", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject reader = new JSONObject(response);
                            String rate_data = reader.getString("IDR");
//                    tvRate.setText("$1 To IDR "+rate_data);
                    rate_data = rate_data.replaceAll(",", "");
                    DecimalFormat df = new DecimalFormat("#,###,###,###.00");
                    double dd = Double.parseDouble(rate_data);
                    tvRate.setText("AUD $1 To IDR "+df.format(dd));

//                    tvRate.setText("$1 To IDR "+String.format("%.2f", rate_data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(AddMoney.this);
        queue.add(postRequest);
    }

}
