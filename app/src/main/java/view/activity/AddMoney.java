package view.activity;

import android.app.Activity;
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
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import presenter.AddMoneyPresenter;
import presenter.GetBankDetailsPresenter;
import view.customview.CustomButton;
import view.customview.CustomTextViewBold;

public class AddMoney extends AppCompatActivity implements View.OnClickListener, GetBankDetailsPresenter.BankDetals {
    private Toolbar toolbar;
    private AppCompatImageView imageView, imgPlus, imgMinus;
    private CustomButton btnProceed;
    private CustomTextViewBold tvAmount, tvOne, tvFive, tvTwo;
//    private AddMoneyPresenter moneyPresenter;
    private GetBankDetailsPresenter bankDetailsPresenter;

    LineChartView lineChartView;
    String[] axisData = { "Apr", "May", "June", "July"};
    int[] yAxisData = {50, 20, 15, 30};


//    String[] axisData = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept","Oct", "Nov", "Dec"};
//    int[] yAxisData = {50, 20, 15, 30, 20, 60, 15, 40, 45, 10, 90, 18};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);

//        moneyPresenter = new AddMoneyPresenter(this, this);
        bankDetailsPresenter = new GetBankDetailsPresenter(this, this);
                    if (isNetworkConnected())
                        bankDetailsPresenter.GetBankDetail();
            else
                showDialog("Please connect to internet");
        initView();
        ShowChart();
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
//            if (isNetworkConnected())
////                moneyPresenter.proceedPayment(addMoneyModel);
//            else
//                showDialog("Please connect to internet");
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
String sBankdetail="";
    @Override
    public void success(String response) {
        sBankdetail=response;
//        ShowNewAlert(AddMoney.this,response);


    }

    @Override
    public void error(String response) {
        sBankdetail="error";//fail
//        showDialog(response);
    }

    @Override
    public void fail(String response) {
        sBankdetail="fail";
//        showDialog(response);
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

//        prettyDialog.addButton("Search again", R.color.black, R.color.white, new PrettyDialogCallback() {
//            @Override
//            public void onClick() {
//                prettyDialog.dismiss();
//
//            }
//        }).show();
    }

    private void ShowBankDetails(){

        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(AddMoney.this);
        LayoutInflater inflater = AddMoney.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.bank_details_dialog, null);
        dialogBuilder.setView(dialogView);
         android.app.AlertDialog bankDetailDlg = dialogBuilder.create();

         TextView tvCloseDlg = dialogView.findViewById(R.id.tv_close);
         TextView tvBankAccnt = dialogView.findViewById(R.id.tv_bank_no);
//         TextView tvBankNm = dialogView.findViewById(R.id.tv_bank_name);
        CustomButton btProcee = dialogView.findViewById(R.id.btn_proceed);

        tvBankAccnt.setText(sBankdetail);

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

            }
        });
        bankDetailDlg.show();
    }
public void ShowChart()
{
    List yAxisValues = new ArrayList();
    List axisValues = new ArrayList();


    Line line = new Line(yAxisValues).setColor(Color.parseColor("#13acbe"));

    for (int i = 0; i < axisData.length; i++) {
        axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
    }

    for (int i = 0; i < yAxisData.length; i++) {
        yAxisValues.add(new PointValue(i, yAxisData[i]));
    }

    List lines = new ArrayList();
    lines.add(line);

    LineChartData data = new LineChartData();
    data.setLines(lines);

    Axis axis = new Axis();
    axis.setValues(axisValues);
    axis.setTextSize(12);
    axis.setName("Months");//Sales in millions
    axis.setTextColor(Color.parseColor("#000000"));
    data.setAxisXBottom(axis);

    Axis yAxis = new Axis();
    yAxis.setName("Currency rate");//Sales in millions
    yAxis.setTextColor(Color.parseColor("#000000"));
    yAxis.setTextSize(12);
    data.setAxisYLeft(yAxis);

    lineChartView.setLineChartData(data);
    Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
//    viewport.top = 110;
    lineChartView.setMaximumViewport(viewport);
    lineChartView.setCurrentViewport(viewport);
}

//    public void makeJsonObject(final String apiUrl) {
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONObject mainObject = jsonObject.getJSONObject("main");
//                    String temp = mainObject.getString("temp");
//                    Long tempVal = Math.round(Math.floor(Double.parseDouble(temp)));
//                    String weatherTemp = String.valueOf(tempVal) + "°C,";
//                    tv_temp.setText(weatherTemp);
//                    tv_city.setText(jsonObject.getString("name"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        RequestQueue queue = Volley.newRequestQueue(getContext());
//        queue.add(stringRequest);
//    }
}
