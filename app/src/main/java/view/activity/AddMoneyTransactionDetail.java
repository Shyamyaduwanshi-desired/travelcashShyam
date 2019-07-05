package view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import model.SubmitTopup;
import model.TransactionModel;
import presenter.SubmitTopupPresenter;
import presenter.TransactionDetailPresenter;
import view.customview.CustomButton;
import view.customview.CustomTextView;
import view.customview.CustomTextViewBold;

public class AddMoneyTransactionDetail extends AppCompatActivity implements View.OnClickListener, SubmitTopupPresenter.SubmitTopup {
    private Toolbar toolbar;
    private AppCompatImageView imageView;
//    private FloatingActionButton fab;
    private CustomTextViewBold tvAmount, tvUploadFile,tvFileNm;
    private CustomButton btnSubmit;
    private String sAmount, mode, agentID;
    private SubmitTopupPresenter Submittopup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_money_transection_detail);
        Submittopup = new SubmitTopupPresenter(this, this);
        initView();
        Listener();
    }

    private void Listener() {
        imageView.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        tvUploadFile.setOnClickListener(this);
    }


    private void initView() {
        try {
            sAmount = getIntent().getStringExtra("amount");
//            sAmount = "500";
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            showDialog("something went wrong ");
        }

        toolbar = findViewById(R.id.toolbar);
        imageView = toolbar.findViewById(R.id.imgBack);
        tvAmount = findViewById(R.id.tv_amount);
        tvUploadFile = findViewById(R.id.tv_upload_file);
        tvFileNm = findViewById(R.id.tvfile_nm);
        btnSubmit = findViewById(R.id.btn_submit);

        tvAmount.setText("$"+sAmount);

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
            case R.id.btn_submit:
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//                sendIntent.setType("text/plain");
//                startActivity(sendIntent);
                if(TextUtils.isEmpty(filePath)||!filePath.contains("."))
                {
                    Toast.makeText(this, "Please select proof id", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (isNetworkConnected()) {


                       String exten= filePath.substring(filePath.lastIndexOf(".") + 1);

                        SubmitTopup bean = new SubmitTopup();
                        bean.setAmount(sAmount);
                        bean.setFileurl(filePath);
                        bean.setExtentionfile(exten);
                        if(exten.equals("pdf")||exten.equals("PDF"))
                        {
                            bean.setPdfExtenstion(filePath);
                        }
                        else {
                            bean.setPdfExtenstion("");
                        }

                        Submittopup.SubmitTopupRequest(bean);


                    } else {
                        showDialog("Please connect to internet");
                    }
                }


                break;

            case R.id.tv_upload_file:
//                GetFile();
                selectImage();
                break;

            case R.id.imgBack:
                finish();
                Animatoo.animateSlideRight(AddMoneyTransactionDetail.this);
                break;
        }
    }

    @Override
    public void success(String response) {
//        Toast.makeText(this, "success= "+response, Toast.LENGTH_SHORT).show();
        ShowNewAlert(AddMoneyTransactionDetail.this,response);

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
                    }
                }).show();
    }

    private void getCall(String s) {
        switch (s) {
//            case "Total Cash Withdraw":
//                Submittopup.showWithdrawTransactionInformation(tvUploadFile);
//                tvAmount.setVisibility(View.VISIBLE);
//                break;
//
//            case "Total Transfer To Bank":
//                Submittopup.showTransferToBankTransaction(tvUploadFile);
//                break;
//
//            case "Total Transfer To Friends":
//                Submittopup.showTransferToFriendTransaction(tvUploadFile);
//                break;
//
//            case "Total Donated":
//                Submittopup.showDonatedTransaction(tvUploadFile);
//                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        Animatoo.animateSlideRight(AddMoneyTransactionDetail.this);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void GetFile()
    {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//        startActivityForResult(intent, 7);

//        Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
//        intent.putExtra("CONTENT_TYPE", "*/*");
//        startActivityForResult(intent, 7);//not working


//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("file/*");
//        startActivityForResult(intent,7);

        //only for image
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 7);
    }


    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                    startActivityForResult(intent, 1);

                    try {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, 0);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
                else if (options[item].equals("Choose from Gallery"))
                {
//                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, 2);

                    try {
//                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        startActivityForResult(pickPhoto , 1);//one can be replaced with any action code

//                        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        startActivityForResult(i, 1);

                        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("","shyam error= "+e);
//                        try {
//                            Intent intent = new Intent();
//                            intent.setType("image/*");
//                            intent.setAction(Intent.ACTION_GET_CONTENT);
//                            startActivityForResult(intent, 1);
//                        } catch (Exception e1) {
//                            e1.printStackTrace();
//                            Log.e("","shyam error1111= "+e);
//                        }
                    }
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    String filePath="",fileNm="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
//            case 7:
//                if (resultCode == RESULT_OK) {
////                    filePath = data.getData().getPath();
////                    File f = new File(filePath);
////                    fileNm = f.getName();
//////                    Toast.makeText(AddMoneyTransactionDetail.this, PathHolder+" filename= "+filename, Toast.LENGTH_LONG).show();
////                    tvFileNm.setText(fileNm);
//
//                   //image pickup
//                    Uri selectedImageUri = data.getData();
//                    filePath = getPath(selectedImageUri);
//                    File f = new File(filePath);
//                    fileNm = f.getName();
//                    tvFileNm.setText(fileNm);
//
//                }
//                break;

            case 0://camera
                if(resultCode == RESULT_OK){
                  Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    Uri selectedImage = getImageUri(getApplicationContext(), photo);

                      //  Uri selectedImage = imageReturnedIntent.getData();
                        filePath = getPath(selectedImage);
                        File f = new File(filePath);
                        fileNm = f.getName();
                        tvFileNm.setText(fileNm);

                }
                break;
            case 1://gallery
                if(resultCode == RESULT_OK&& null != imageReturnedIntent){
                    Uri selectedImage = imageReturnedIntent.getData();

                    filePath = getPath(selectedImage);
                    File f = new File(filePath);
                    fileNm = f.getName();
                    tvFileNm.setText(fileNm);

                }


                break;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
//                Intent intent = new Intent();
//                setResult(Activity.RESULT_OK, intent);
                finish();
                Animatoo.animateFade(AddMoneyTransactionDetail.this);


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


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
//                    viewImage.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gallery......******************.........", picturePath+"");
                viewImage.setImageBitmap(thumbnail);
            }
        }
    }*/
}
