package view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import presenter.ChangePinPresenter;
import view.customview.CustomButton;
import view.customview.CustomEditText;

public class ChangePin extends AppCompatActivity implements ChangePinPresenter.Pin, View.OnClickListener {
    private CustomEditText edtOldPin, edtNewPin, edtConfirmPin;
    private CustomButton btnSubmit;
    private AppCompatImageView imgBack;
    private ChangePinPresenter pinPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pin);

        pinPresenter = new ChangePinPresenter(this, this);
        imgBack = findViewById(R.id.imgBack);
        edtOldPin = findViewById(R.id.edtOldPin);
        edtNewPin = findViewById(R.id.edtNewPin);
        edtConfirmPin = findViewById(R.id.edtConfirmPin);
        btnSubmit = findViewById(R.id.btnSubmit);
        imgBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
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
    public void success(String response) {
        Toast toast = Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        finish();
        Animatoo.animateFade(ChangePin.this);
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
    public void onClick(View v) {
        if(v == imgBack){
            finish();
            Animatoo.animateFade(ChangePin.this);
        }else if(v == btnSubmit){
            String oldPin = edtOldPin.getText().toString().trim();
            String newPin = edtNewPin.getText().toString().trim();
            String confPin = edtConfirmPin.getText().toString().trim();
            if(TextUtils.isEmpty(oldPin)){
                edtOldPin.setError("Please enter pin");
            }else if(oldPin.length()<4){
                edtOldPin.setError("Please enter valid pin");
            }else if(TextUtils.isEmpty(newPin)){
                edtNewPin.setError("Please enter new pin");
            }else if(newPin.length()<4){
                edtOldPin.setError("Please enter valid 4 digit pin");
            }else if(TextUtils.isEmpty(confPin)){
                edtConfirmPin.setError("Please enter confirm pin");
            }else if(confPin.length()<4){
                edtConfirmPin.setError("Please enter valid 4 digit confirm pin");
            }else if(!newPin.equals(confPin)){
                showDialog("Confirm Pin doesn't match with New Pin");
            }else {
                if(isNetworkConnected())
                    pinPresenter.changePin(oldPin, newPin);
                else
                    showDialog("Please connect to internet.");
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        finish();
        Animatoo.animateFade(ChangePin.this);
    }
}
