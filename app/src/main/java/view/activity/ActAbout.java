package view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.travelcash.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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
    CardView cvMain;
//    WebView wv;
int diff=0;
//    private TransferMoneyFriend transferMoneyFriend;
    private AboutPresenter aboutPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_about);
        aboutPresenter = new AboutPresenter(this, this);
        GetIntentData();
        initView();
        LoadData();
    }


    public void  GetIntentData() {
//        diff=getIntent().getIntExtra("diff_",1);
        diff=1;
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
        cvMain= findViewById(R.id.cv_pic);

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
    cvMain.setVisibility(View.VISIBLE);
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

//        String source = "this is a test of <b>ImageGetter</b> it contains " +
//                "two images: <br/>" +
//                "<img src=\"https://omsoftware.org/cashapp/uploads/agents/Sanket_R_471.jpeg\"><br/>and<br/>" +
//                "<img src=\"https://omsoftware.org/cashapp/uploads/agents/sonali1234 lohakare_46.jpeg\">";
//        tvFull.setText(Html.fromHtml(source, new ImageGetter(), null));


//       String stest= "<p>Text before image</p>\r\n<p><img style=\"max-width: 100%;\" src=\"data:image/png;base64,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/></p>";
//
//            PicassoImageGetter imageGetter = new PicassoImageGetter(tvFull);
//    Spannable html;
//if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//        html = (Spannable) Html.
//                fromHtml(stest, Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
//    } else {
//        html = (Spannable) Html.
//                fromHtml(stest, imageGetter, null);
//    }
//        tvFull.setText(html);

        String stest= "<p>Text before image</p>\r\n<p><img style=\"max-width: 100%;\" src=\"data:image/png;base64,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\"\"/></p>";

//        tvFull.setText(stest);
    }


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
    cvMain.setVisibility(View.GONE);
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

    public class ImageGetter implements Html.ImageGetter {

        public Drawable getDrawable(String source) {
//            int id;
//
//            id = getResources().getIdentifier(source, "drawable", getPackageName());
//
//            if (id == 0) {
//                // the drawable resource wasn't found in our package, maybe it is a stock android drawable?
//                id = getResources().getIdentifier(source, "drawable", "android");
//            }
//
//            if (id == 0) {
//                // prevent a crash if the resource still can't be found
//                return null;
//            }
//            else {
//                Drawable d = getResources().getDrawable(id);
//                d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
//                return d;
//            }


            LevelListDrawable d = new LevelListDrawable();
            Drawable empty = getResources().getDrawable(R.drawable.ic_about_test);
            d.addLevel(0, 0, empty);
            d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

            new LoadImage().execute(source, d);

            return d;
        }


    }
    String TAG="Load image";
    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            Log.d(TAG, "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d(TAG, "onPostExecute drawable " + mDrawable);
            Log.d(TAG, "onPostExecute bitmap " + bitmap);
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                CharSequence t = tvFull.getText();
                tvFull.setText(t);
            }
        }
    }


//    PicassoImageGetter imageGetter = new PicassoImageGetter(mytextView);
//    Spannable html;
//if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//        html = (Spannable) Html.
//                fromHtml(content, Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
//    } else {
//        html = (Spannable) Html.
//                fromHtml(content, imageGetter, null);
//    }
//mytextView.setText(html);

    public class PicassoImageGetter implements Html.ImageGetter {

        private TextView textView = null;

        public PicassoImageGetter() {

        }

        public PicassoImageGetter(TextView target) {
            textView = target;
        }

        @Override
        public Drawable getDrawable(String source) {
            BitmapDrawablePlaceHolder drawable = new BitmapDrawablePlaceHolder();
            Picasso.with(ActAbout.this)
                    .load(source)
                    .placeholder(R.drawable.ic_about_test)
                    .into(drawable);
            return drawable;
        }

        private class BitmapDrawablePlaceHolder extends BitmapDrawable implements Target {

            protected Drawable drawable;

            @Override
            public void draw(final Canvas canvas) {
                if (drawable != null) {
                    drawable.draw(canvas);
                }
            }

            public void setDrawable(Drawable drawable) {
                this.drawable = drawable;
                int width = drawable.getIntrinsicWidth();
                int height = drawable.getIntrinsicHeight();
                drawable.setBounds(0, 0, width, height);
                setBounds(0, 0, width, height);
                if (textView != null) {
                    textView.setText(textView.getText());
                }
            }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                setDrawable(new BitmapDrawable(ActAbout.this.getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }

        }
    }
}
