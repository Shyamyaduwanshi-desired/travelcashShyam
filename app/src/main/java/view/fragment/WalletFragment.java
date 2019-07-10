package view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import constant.AppData;
import model.RecentActivity;
import model.ShowWalletInfoModel;
import presenter.RecentActivityPresenter;
import presenter.ShowWalletInfoPresenter;
import view.activity.AddMoney;
import view.adapter.RecentActivityAdapter;
import view.adapter.ShowWalletInfoAdapter;
import view.customview.CustomTextViewBold;

public class WalletFragment extends Fragment implements ShowWalletInfoPresenter.WalletInfo {
    private static WalletFragment walletFragment;
    private CustomTextViewBold tvTopUp, tvAmount;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.Adapter mAdapter;
//    private RecentActivityPresenter presenter;

    private ShowWalletInfoPresenter showwalletPresenter;

    private AppData appData;

    public static WalletFragment getInstance(){
        walletFragment = new WalletFragment();

        return walletFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

//        presenter = new RecentActivityPresenter(getContext(), WalletFragment.this);
        showwalletPresenter=new ShowWalletInfoPresenter(getContext(), WalletFragment.this);
        appData = new AppData(getContext());
        initView(view);


        return view;
    }

    private void initView(View view){
        tvTopUp = view.findViewById(R.id.tvTopUp);
        tvTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), AddMoney.class), 100);
                Animatoo.animateSlideRight(getContext());
            }
        });

        tvAmount = view.findViewById(R.id.tvAmount);
        recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if(isNetworkConnected()){
//            presenter.recentActivity(appData.getUserID());
            showwalletPresenter.ShowWalletInfo(appData.getUserID());
        }else {
            showDialog("Please connect to internet");
        }

    }

    @Override
    public void success(ArrayList<ShowWalletInfoModel> arrayList, String walletAmount) {
//        DecimalFormat df = new DecimalFormat( "#,###,###,###.00" );
        DecimalFormat df = new DecimalFormat( "#,###,###,###" );
//        DecimalFormat df = new DecimalFormat( "#,###,###,###.00" );
        double dd = Double.parseDouble(walletAmount);
        tvAmount.setText("" + df.format(dd));

//        mAdapter = new RecentActivityAdapter(getActivity(), arrayList);
        mAdapter = new ShowWalletInfoAdapter(getActivity(), arrayList);
        recyclerView.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();
        runLayoutAnimation(recyclerView);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
//               presenter.recentActivity(appData.getUserID());
                showwalletPresenter.ShowWalletInfo(appData.getUserID());
            }else {
                showDialog("Failed to add money to wallet.");
            }
        }
    }

    private void showDialog(String message) {
        try {
            new AlertDialog.Builder(getActivity())
                    .setTitle("")
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    private void runLayoutAnimation(final RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}

