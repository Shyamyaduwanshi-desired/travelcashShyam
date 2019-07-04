package view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import constant.AppData;

public class ActNotification extends AppCompatActivity implements View.OnClickListener/*, SubmitTopupPresenter.SubmitTopup*/ {
    private Toolbar toolbar;
    private AppCompatImageView imageView;

//    private SubmitTopupPresenter Submittopup;
private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private AppData appData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_notification);
//        Submittopup = new SubmitTopupPresenter(this, this);
        appData = new AppData(this);

        initView();
        Listener();
    }

    private void Listener() {
        imageView.setOnClickListener(this);

    }


    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        imageView = toolbar.findViewById(R.id.imgBack);
        recyclerView = findViewById(R.id.rv_noiti);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tvAmount:
//                Intent intent = new Intent(AddMoneyTransactionDetail.this, WriteReview.class);
//                intent.putExtra("agentID", agentID);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                Animatoo.animateSlideRight(AddMoneyTransactionDetail.this);
//                break;
//
//            case R.id.btn_submit:
////                Intent sendIntent = new Intent();
////                sendIntent.setAction(Intent.ACTION_SEND);
////                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
////                sendIntent.setType("text/plain");
////                startActivity(sendIntent);
//                if(TextUtils.isEmpty(filePath)||!filePath.contains("."))
//                {
//                    Toast.makeText(this, "Please select proof id", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    if (isNetworkConnected()) {
//
//
//                       String exten= filePath.substring(filePath.lastIndexOf(".") + 1);
//
//                        SubmitTopup bean = new SubmitTopup();
//                        bean.setAmount(sAmount);
//                        bean.setFileurl(filePath);
//                        bean.setExtentionfile(exten);
//                        if(exten.equals("pdf")||exten.equals("PDF"))
//                        {
//                            bean.setPdfExtenstion(filePath);
//                        }
//                        else {
//                            bean.setPdfExtenstion("");
//                        }
//
////                        Submittopup.SubmitTopupRequest(bean);
//
//
//                    } else {
//                        showDialog("Please connect to internet");
//                    }
//                }
//
//
//                break;
//
//            case R.id.tv_upload_file:
//                GetFile();
//                break;

            case R.id.imgBack:
                finish();
                Animatoo.animateSlideRight(ActNotification.this);
                break;
        }
    }

//    @Override
//    public void success(String response) {
////        Toast.makeText(this, "success= "+response, Toast.LENGTH_SHORT).show();
//        ShowNewAlert(ActNotification.this,response);
//
//    }
//
//    @Override
//    public void error(String response) {
//        showDialog(response);
//    }
//
//    @Override
//    public void fail(String response) {
//        showDialog(response);
//    }

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
        Animatoo.animateSlideRight(ActNotification.this);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
