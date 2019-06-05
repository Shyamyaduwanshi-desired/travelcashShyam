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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.travelcash.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import constant.AppData;
import constant.SignUpPreference;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import presenter.ProfilePresenter;
import presenter.SignupPresenter;
import view.customview.CustomButton;
import view.customview.CustomEditText;
import view.customview.CustomTextView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, ProfilePresenter.Profile {
    private Toolbar toolbar;
    private AppCompatImageView imgBack;
    private CircleImageView imgProfile;
    private CustomButton btnUpdate;
    private CustomTextView tvReview;
    private CustomEditText edtUsername, edtEmail, edtMobile;
    private AppData appData;
    private ProfilePresenter presenter;
    private File file, compressedImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        presenter = new ProfilePresenter(this, this);
        initView();
    }

    private void initView() {
        appData = new AppData(this);
        toolbar = findViewById(R.id.toolbar);
        imgProfile = findViewById(R.id.imgProfile);
        btnUpdate = findViewById(R.id.btnUpdate);
        tvReview = findViewById(R.id.tvReview);
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtMobile = findViewById(R.id.edtMobile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        imgBack = toolbar.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Animatoo.animateFade(ProfileActivity.this);
            }
        });

        btnUpdate.setOnClickListener(this);
        tvReview.setOnClickListener(this);
        imgProfile.setOnClickListener(this);

        edtUsername.setText(appData.getUsername());
        edtEmail.setText(appData.getEmail());
        edtMobile.setText(appData.getMobile());

        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.persion)
                .skipMemoryCache(true);

        Glide.with(this).load(appData.getProfile())
                .thumbnail(0.5f)
                .apply(requestOptions)
                .into(imgProfile);

        edtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String value = edtMobile.getText().toString();
                if (!isValidMobile(value) || value.length() < 6) {
                    edtMobile.setError("Please enter valid mobile number");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String value = edtEmail.getText().toString();
                if (!isValidMail(value)) {
                    edtEmail.setError("Please enter valid email");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
    }

    private boolean isStoragePermissionGranted() {
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnUpdate) {
            String email = edtEmail.getText().toString().trim();
            String mobile = edtMobile.getText().toString().trim();
            String username = edtUsername.getText().toString().trim();
            if (TextUtils.isEmpty(mobile)) {
                edtMobile.setError("Please enter mobile number");
            } else if (!isValidMobile(mobile) || mobile.length() < 10) {
                edtMobile.setError("Please enter valid mobile number");
            } else if (TextUtils.isEmpty(email)) {
                edtMobile.setError("Please enter email id");
            } else if (!isValidMail(email)) {
                edtMobile.setError("Please enter valid email id");
            } else {
                if(!isNetworkConnected()){
                    showDialog("Please connect to internet.");
                }else {
                    SignUpPreference.getInstance(ProfileActivity.this).setValue("username", username);
                    SignUpPreference.getInstance(ProfileActivity.this).setValue("email", email);
                    SignUpPreference.getInstance(ProfileActivity.this).setValue("mobile", mobile);
                    presenter.updateProfile();
                }
            }

        } else if (v == tvReview) {
            startActivity(new Intent(getApplicationContext(), ViewReview.class));
            Animatoo.animateSlideRight(ProfileActivity.this);
        }else if(v == imgProfile){
            if (Build.VERSION.SDK_INT >= 23) {
                if(isStoragePermissionGranted()){
                    openGallery();
                }else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
            } else {
               openGallery();
            }
        }
    }

    private void openGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,  android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 100);
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    /* Get the real path from the URI */
    private String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    /*Compress image receive from gallery*/
    private void compress() {
        try {
            compressedImage = new Compressor(this)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(50)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .compressToFile(file);
            String filePath = compressedImage.getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            imgProfile.setImageBitmap(bitmap);
            String image = getEncoded64(bitmap);
            SignUpPreference.getInstance(ProfileActivity.this).setValue("image", image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*encode compress image into base64*/
    private String getEncoded64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
           openGallery();
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Please allow permission")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        }
                    }).show();
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == 100 && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            file = new File(getPathFromURI(imageUri));
            compress();
        } else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void success(String response) {
        Toast toast = Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        appData.setEmail(edtEmail.getText().toString().trim());
        appData.setMobile(edtMobile.getText().toString().trim());

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
        Animatoo.animateFade(ProfileActivity.this);
    }

    @Override
    public void error(String response) {
        showDialog(response);
    }

    @Override
    public void fail(String response) {
        showDialog(response);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        finish();
        Animatoo.animateFade(ProfileActivity.this);
    }
}
