package view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import view.customview.CustomButton;

public class CancelTransaction extends AppCompatActivity {
    private Toolbar toolbar;
    private AppCompatImageView imageView;
    private CustomButton btnHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancel_transection);

        toolbar = findViewById(R.id.toolbar);
        imageView = toolbar.findViewById(R.id.imgBack);
        btnHome = findViewById(R.id.btnHome);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSlideRight(CancelTransaction.this);
    }
}
