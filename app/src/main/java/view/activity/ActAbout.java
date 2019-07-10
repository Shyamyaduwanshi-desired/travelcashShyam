package view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.travelcash.R;

import java.util.ArrayList;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import model.aboutBean;
import presenter.AboutPresenter;
import presenter.TransferMoneyFriend;
import view.customview.CustomTextViewBold;

public class ActAbout extends AppCompatActivity implements View.OnClickListener, AboutPresenter.AboutInfo {
    private Toolbar toolbar;
    private AppCompatImageView imgBack,ivBanner,ivPic;
    CustomTextViewBold tvTitle;
    TextView tvShortDsc,tvFull;
//    WebView wv;
int diff=0;
//    private TransferMoneyFriend transferMoneyFriend;
    private AboutPresenter aboutPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_about);

//        transferMoneyFriend = new TransferMoneyFriend(this, this);
        aboutPresenter = new AboutPresenter(this, this);
        GetIntentData();
        initView();
        LoadData();
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

    private void LoadData() {

//        wv.setWebViewClient(new WebViewClient());
//        wv.getSettings().setJavaScriptEnabled(true);
//        wv.getSettings().setDomStorageEnabled(true);
//        wv.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
//        wv.loadUrl("https://www.google.com");
        switch (diff)
        {
            case 1://about
                tvTitle.setText("About");
                break;
            case 2://help
                tvTitle.setText("Help");
                break;
            case 3://privacy policy
                tvTitle.setText("Privacy Policy");
                break;
        }

    }



    @Override
    public void onClick(View v) {
        if (v == imgBack) {
            finish();
            Animatoo.animateSlideRight(ActAbout.this);
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
        Animatoo.animateFade(ActAbout.this);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

//    @Override
//    public void success(String response) {
//        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
//        finish();
//        Animatoo.animateSlideRight(ActAbout.this);
//    }


    private void initView() {
//        wv = findViewById(R.id.wv_about_web);
        tvShortDsc = findViewById(R.id.tv_short_dsc);
        tvFull = findViewById(R.id.tv_full_dsc);
        ivBanner = findViewById(R.id.iv_banner);
        ivPic= findViewById(R.id.iv_image);

        toolbar = findViewById(R.id.toolbar);
        imgBack = toolbar.findViewById(R.id.imgBack);
        tvTitle = toolbar.findViewById(R.id.tv_title);
        imgBack.setOnClickListener(this);


    }
    //aboutus,help,privacy policy
    ArrayList<aboutBean> arListData=new ArrayList<>();
    @Override
    public void success(ArrayList<aboutBean> response) {
        arListData.clear();
        arListData=response;
if(arListData.size()>0)
{

    tvTitle.setText(Html.fromHtml(arListData.get(0).getStatic_page_title()));

    if(TextUtils.isEmpty(arListData.get(0).getShort_description()))
    {
        tvShortDsc.setVisibility(View.GONE);
    }
    else
    {
        tvShortDsc.setText(Html.fromHtml(arListData.get(0).getShort_description()));
    }

    if(TextUtils.isEmpty(arListData.get(0).getDescription()))
    {
        tvFull.setVisibility(View.GONE);
    }
    else
    {
        tvFull.setText(Html.fromHtml(arListData.get(0).getDescription()));
    }

//    ivBanner.setVisibility(View.VISIBLE);
//    ivPic.setVisibility(View.VISIBLE);

    if(TextUtils.isEmpty(arListData.get(0).getBanner_image()))
    {
        ivBanner.setVisibility(View.GONE);
    }
    else
    {
        ivBanner.setVisibility(View.VISIBLE);
        Glide.with(this).load(arListData.get(0).getBanner_image()).thumbnail(0.5f)
                /* .crossFade()
                 .diskCacheStrategy(DiskCacheStrategy.NONE)
                 .skipMemoryCache(true)
                 .error(R.drawable.persion)*/
                .into(ivBanner);

    }
    if(TextUtils.isEmpty(arListData.get(0).getImageUrl()))
    {
        ivPic.setVisibility(View.GONE);
    }
    else
    {
        ivPic.setVisibility(View.VISIBLE);
        Glide.with(this).load(arListData.get(0).getBanner_image()).thumbnail(0.5f)
                .into(ivPic);
    }

}
else
{
    Toast.makeText(this, "data not found", Toast.LENGTH_SHORT).show();
}

//        switch (diff)
//        {
//            case 1://about
//                tvTitle.setText("About");
//                break;
//            case 2://help
//                tvTitle.setText("Help");
//                break;
//            case 3://privacy policy
//                tvTitle.setText("Privacy Policy");
//                break;
//        }
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
