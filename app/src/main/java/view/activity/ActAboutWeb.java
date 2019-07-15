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
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import java.util.ArrayList;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import model.aboutBean;
import presenter.AboutPresenter;
import presenter.TransferMoneyFriend;
import view.customview.CustomButton;
import view.customview.CustomEditText;
import view.customview.CustomTextViewBold;

public class ActAboutWeb extends AppCompatActivity implements View.OnClickListener, AboutPresenter.AboutInfo {
    private Toolbar toolbar;
    private AppCompatImageView imgBack;
    CustomTextViewBold tvTitle;
    WebView wv;
    private AboutPresenter aboutPresenter;
    CardView cvMain;
    int diff=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_about_web);
        aboutPresenter = new AboutPresenter(this, this);
        initView();
        GetIntentData();
    }


    public void  GetIntentData() {
        diff=getIntent().getIntExtra("diff_",1);
        switch (diff)
        {
            case 1://about
                if(isNetworkConnected()){
                    aboutPresenter.getAboutInfo();
                }else {
                    showAlert("Please connect to internet", R.style.DialogAnimation);
                }

                break;
            case 2://help
                if(isNetworkConnected()){
                    aboutPresenter.getHelpInfo();
                }else {
                    showAlert("Please connect to internet", R.style.DialogAnimation);
                }

                break;
            case 3://privacy policy
                if(isNetworkConnected()){
                    aboutPresenter.getPrivacyInfo();
                }else {
                    showAlert("Please connect to internet", R.style.DialogAnimation);
                }

                break;
        }

    }

    private void initView() {
        wv = findViewById(R.id.wv_about_web);
        toolbar = findViewById(R.id.toolbar);
        imgBack = toolbar.findViewById(R.id.imgBack);
        tvTitle = toolbar.findViewById(R.id.tv_title);
        cvMain = findViewById(R.id.cv_pic);
        imgBack.setOnClickListener(this);
        wv.setWebViewClient(new WebViewClient());
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
    }

    @Override
    public void onClick(View v) {
        if (v == imgBack) {
            finish();
            Animatoo.animateSlideRight(ActAboutWeb.this);
        }
        else {

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
        Animatoo.animateFade(ActAboutWeb.this);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

ArrayList<aboutBean> arListData=new ArrayList<>();
//aboutus
    @Override
    public void success(ArrayList<aboutBean> response) {
        arListData.clear();
        arListData=response;
        if(arListData.size()>0) {
            tvTitle.setText(Html.fromHtml(arListData.get(0).getStatic_page_title()));
            String shorttitle="",full="";
            if(!TextUtils.isEmpty(arListData.get(0).getShort_description()))
              shorttitle=arListData.get(0).getShort_description()+"<br/>";

            if(!TextUtils.isEmpty(arListData.get(0).getDescription()))
              full=arListData.get(0).getDescription()+"<br/>";

            wv.loadDataWithBaseURL("", shorttitle+full, "text/html", "utf-8", "");
            cvMain.setVisibility(View.VISIBLE);
        }
        else
        {
            Toast.makeText(this, "Data not found", Toast.LENGTH_SHORT).show();
            switch (diff)
            {
                case 1://about
                    tvTitle.setText("About Us");
                    break;
                case 2://help
                    tvTitle.setText("Help");
                    break;
                case 3://privacy policy
                    tvTitle.setText("Privacy Policy");
                    break;
            }
        }
    }

    @Override
    public void error(String response) {
        showDialog(response);
    }

    @Override
    public void fail(String response) {
        showDialog(response);
    }

    private void showAlert(String message, int animationSource){
        final PrettyDialog prettyDialog = new PrettyDialog(this);
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
        prettyDialog.getWindow().getAttributes().windowAnimations = animationSource;
        prettyDialog.addButton("Ok", R.color.black, R.color.white, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                prettyDialog.dismiss();
            }
        }).show();
    }
}
