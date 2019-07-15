package view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.travelcash.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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

            Log.e("","sAmount= "+sAmount);
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

            case R.id.btn_submit:
                if(TextUtils.isEmpty(filePath)||!filePath.contains("."))
                {
                    Toast.makeText(this, "Please select proof id", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (isNetworkConnected()) {


                       String exten= filePath.substring(filePath.lastIndexOf(".") + 1);

                        SubmitTopup bean = new SubmitTopup();
                        if(sAmount.contains(","))
                        {
                            sAmount=sAmount.replaceAll(",","");
                        }

                        bean.setAmount(sAmount);
//                        bean.setFileurl(filePath);
                        bean.setFileurl(uploadBase64);
                        bean.setExtentionfile(exten);
                        if(exten.equals("pdf")||exten.equals("PDF"))
                        {
//                            bean.setPdfExtenstion(filePath);
                            bean.setPdfExtenstion(uploadBase64);
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
//                selectImage();
                selectImage1();
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

//    public void GetFile()
//    {
////        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
////        intent.setType("*/*");
////        startActivityForResult(intent, 7);
//
////        Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
////        intent.putExtra("CONTENT_TYPE", "*/*");
////        startActivityForResult(intent, 7);//not working
//
//
////        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
////        intent.setType("file/*");
////        startActivityForResult(intent,7);
//
//        //only for image
//        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(i, 7);
//    }
//
//
//    private void selectImage() {
//        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Select Photo");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (options[item].equals("Take Photo"))
//                {
////                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
////                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
////                    startActivityForResult(intent, 1);
//
//                    try {
//                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(takePicture, 0);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//
//                }
//                else if (options[item].equals("Choose from Gallery"))
//                {
////                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////                    startActivityForResult(intent, 2);
//
//                    try {
////                        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////                        startActivityForResult(intent, 1);//working
//
////                        new MaterialFilePicker()
////                                .withActivity(AddMoneyTransactionDetail.this)
////                                .withRequestCode(3)
////                                .withHiddenFiles(true)
////                                .withTitle("Sample title")
////                                .start();
////                        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
////                        chooseFile.setType("image/*|pdf/*");
////                        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
////                        startActivityForResult(chooseFile, 1);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.e("","shyam error= "+e);
////                        try {
////                            Intent intent = new Intent();
////                            intent.setType("image/*");
////                            intent.setAction(Intent.ACTION_GET_CONTENT);
////                            startActivityForResult(intent, 1);
////                        } catch (Exception e1) {
////                            e1.printStackTrace();
////                            Log.e("","shyam error1111= "+e);
////                        }
//                    }
//
//
//                }
//                else if (options[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }
    String filePath="",fileNm="";
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//        switch (requestCode) {
////            case 7:
////                if (resultCode == RESULT_OK) {
//////                    filePath = data.getData().getPath();
//////                    File f = new File(filePath);
//////                    fileNm = f.getName();
////////                    Toast.makeText(AddMoneyTransactionDetail.this, PathHolder+" filename= "+filename, Toast.LENGTH_LONG).show();
//////                    tvFileNm.setText(fileNm);
////
////                   //image pickup
////                    Uri selectedImageUri = data.getData();
////                    filePath = getPath(selectedImageUri);
////                    File f = new File(filePath);
////                    fileNm = f.getName();
////                    tvFileNm.setText(fileNm);
////
////                }
////                break;
//
//            case 0://camera
//                if(resultCode == RESULT_OK){
//                  Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
//                    Uri selectedImage = getImageUri(getApplicationContext(), photo);
//
//                      //  Uri selectedImage = imageReturnedIntent.getData();
//                        filePath = getPath(selectedImage);
//                        File f = new File(filePath);
//                        fileNm = f.getName();
//                        tvFileNm.setText(fileNm);
//
//                }
//                break;
//            case 1://gallery
//                if(resultCode == RESULT_OK&& null != imageReturnedIntent){
//                    Uri selectedImage = imageReturnedIntent.getData();
//
//                    filePath = getPath(selectedImage);
//                    File f = new File(filePath);
//                    fileNm = f.getName();
//                    tvFileNm.setText(fileNm);
//
//                }
//
//
//                break;
//        }
//    }

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
                finish();
                Animatoo.animateFade(AddMoneyTransactionDetail.this);


            }
        }).show();
    }


 static  int CAMERA_REQUEST=3,Result_Load_Image=4;
 int MY_CAMERA_PERMISSION_CODE=11;
    private void selectImage1() {
        final CharSequence[] items = {"Take Photo", "Choose from Library","Choose pdf",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddMoneyTransactionDetail.this);
        builder.setTitle("Add File");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");//only for image
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select File"), Result_Load_Image);
                }
                else if (items[item].equals("Choose pdf")) {

                    new MaterialFilePicker()
                            .withActivity(AddMoneyTransactionDetail.this)
                            .withRequestCode(1)
                            .withHiddenFiles(true)
                            .withTitle("Sample title")
                            .start();

                }

                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    String uploadBase64="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Result_Load_Image && data != null && data.getData() != null) {
                Uri filePath2 = data.getData();

                try {
                 Bitmap   bitmap= MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath2);
                    Bitmap bit=Bitmap.createScaledBitmap(bitmap,150,150,false);

                    Uri selectedImage = getImageUri(getApplicationContext(), bit);

                     filePath = getPath(selectedImage);
                     File f = new File(filePath);
                    fileNm = f.getName();
                    tvFileNm.setText(fileNm);
                    uploadBase64=getFileToBase64_1(f);
                    Log.e("","shyam photo size11= "+f.length()+" uploadBase64= "+uploadBase64);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Bitmap bit=Bitmap.createScaledBitmap(photo ,150,150,false);
                Uri selectedImage = getImageUri(getApplicationContext(), bit);
                filePath = getPath(selectedImage);
                File f = new File(filePath);
                fileNm = f.getName();
                tvFileNm.setText(fileNm);
//                uploadBase64=getFileToBase64(filePath);
                uploadBase64=getFileToBase64_1(f);

                Log.e("","shyam file size11= "+f.length()+" uploadBase64= "+uploadBase64);

            }
            else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

                File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                filePath = f.getAbsolutePath();

                File f1 = new File(filePath);
                fileNm = f1.getName();
                tvFileNm.setText(fileNm);
//                uploadBase64=getFileToBase64(filePath);
                uploadBase64=getFileToBase64_1(f);
                Log.e("","shyam file size22= "+f.length()+" uploadBase64= "+uploadBase64);
            }
            else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public  String getFileToBase64(String filePath){
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try{
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return encodeString;
    }
    public String getFileToBase64_1(File f) {
        InputStream inputStream = null;
        String encodedFile= "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile =  output.toString();
        }
        catch (FileNotFoundException e1 ) {
            e1.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }


    private String EncodeImageIntoBase64(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }
}
