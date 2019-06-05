package view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travelcash.R;

import view.activity.LoginActivity;
import view.activity.SplashActivity;
import view.activity.StartActivity;
import view.activity.WelcomeActivity;

public class FragmentThree extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               try {
                   Intent intent = new Intent(getActivity(), StartActivity.class);
                   startActivity(intent);
                   getActivity().finish();
                   getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        }, 3000);

        return view;
    }
}
