package view.activity;

import android.Manifest;
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
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import constant.AppData;
import constant.SignUpPreference;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import presenter.SignupPresenter;
import view.customview.CustomButton;
import view.customview.CustomTextInput;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, SignupPresenter.SignUp {
    private CircleImageView profileImage;
    private CustomTextInput edtUsername, edtPassword, edtConfirmPass, edtMobile, edtEmail, edtPin;
    private CustomButton btnSubmit;
    private SignupPresenter presenter;
    private String picture = "";
    private File file, compressedImage;
    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        presenter = new SignupPresenter(this, SignUpActivity.this);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isStoragePermissionGranted() {
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void initView() {
        profileImage = findViewById(R.id.profile_image);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPass = findViewById(R.id.edtConfirmPass);
        edtMobile = findViewById(R.id.edtMobile);
        edtEmail = findViewById(R.id.edtEmail);
        edtPin = findViewById(R.id.edtPin);
        btnSubmit = findViewById(R.id.btnSubmit);
        profileImage.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        String username = SignUpPreference.getInstance(SignUpActivity.this).getValue("username");
        if (!username.equals("NA")) {
            edtUsername.setText(username);
            edtPassword.setText(SignUpPreference.getInstance(SignUpActivity.this).getValue("password"));
            edtConfirmPass.setText(SignUpPreference.getInstance(SignUpActivity.this).getValue("password"));
            edtMobile.setText(SignUpPreference.getInstance(SignUpActivity.this).getValue("mobile"));
            edtEmail.setText(SignUpPreference.getInstance(SignUpActivity.this).getValue("email"));
            edtPin.setText(SignUpPreference.getInstance(SignUpActivity.this).getValue("pin"));
            String filePath = SignUpPreference.getInstance(SignUpActivity.this).getValue("path");
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            profileImage.setImageBitmap(bitmap);
            picture = SignUpPreference.getInstance(SignUpActivity.this).getValue("picture");
        }

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String value = edtPassword.getText().toString();
                if (!isValidPassword(value)) {
                    edtPassword.setError("Please enter password contain character, number and special character");
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

        edtConfirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String password = edtPassword.getText().toString();
                String value = edtConfirmPass.getText().toString();
                if (!password.equals(value)) {
                    edtConfirmPass.setError("Please enter same password and confirm password");
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

    private void openGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 100);
    }

    @Override
    public void onClick(View v) {
        if (v == profileImage) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (isStoragePermissionGranted()) {
                    openGallery();
                } else {
                    ActivityCompat.requestPermissions(this, permissions, 101);
                }
            } else {
               openGallery();
            }
        } else if (v == btnSubmit) {
            sign_up();
        }
    }

    private void sign_up() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        String confirm_password = edtConfirmPass.getText().toString();
        String mobile = edtMobile.getText().toString();
        String email = edtEmail.getText().toString();
        String pin = edtPin.getText().toString();

        if (TextUtils.isEmpty(username)) {
            edtUsername.setError("Please enter username");
        } else if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Please enter password");
        } else if (password.length() < 8) {
            edtPassword.setError("Please enter minimum 8 digit password");
        } else if (!isValidPassword(password)) {
            showDialog("Please enter password contain character, number and special character");
        } else if (TextUtils.isEmpty(confirm_password)) {
            edtConfirmPass.setError("Please enter confirm password");
        } else if (!password.equals(confirm_password)) {
            showDialog("Please enter same password and confirm password");
        } else if (TextUtils.isEmpty(mobile)) {
            edtMobile.setError("Please enter mobile number");
        } else if (!isValidMobile(mobile) || mobile.length() < 6) {
            edtMobile.setError("Please enter valid mobile number");
        } else if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Please enter email id");
        } else if (!isValidMail(email)) {
            edtEmail.setError("Please enter valid email id");
        } else if (TextUtils.isEmpty(pin)) {
            edtPin.setError("Please enter pin");
        } else if (pin.length() < 4) {
            edtPin.setError("Please enter 4 digit pin");
        } else {
            if (isNetworkConnected()) {
                SignUpPreference.getInstance(SignUpActivity.this).setValue("username", username);
                SignUpPreference.getInstance(SignUpActivity.this).setValue("password", password);
                SignUpPreference.getInstance(SignUpActivity.this).setValue("mobile", mobile);
                SignUpPreference.getInstance(SignUpActivity.this).setValue("email", email);
                SignUpPreference.getInstance(SignUpActivity.this).setValue("pin", pin);
                SignUpPreference.getInstance(SignUpActivity.this).setValue("picture", picture);
                presenter.sentRequest(mobile);
            } else {
                showDialog("Please connect to internet.");
            }
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

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public void success(String response) {
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage(response)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Intent intent = new Intent(SignUpActivity.this, VerifyOtp.class);
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideUp(SignUpActivity.this);
                    }
                }).show();
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Please allow permission to open camera")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(SignUpActivity.this, permissions, 101);
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
            profileImage.setImageBitmap(bitmap);
            picture = getEncoded64(bitmap);
            SignUpPreference.getInstance(SignUpActivity.this).setValue("path", filePath);
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
    public void onBackPressed() {
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
        Animatoo.animateSlideDown(SignUpActivity.this);
    }

    /*check internet connection*/
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
