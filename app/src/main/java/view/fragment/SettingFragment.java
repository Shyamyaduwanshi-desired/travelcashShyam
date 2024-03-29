package view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.kcode.bottomlib.BottomDialog;
import com.travelcash.R;

import java.util.ArrayList;
import java.util.List;

import constant.AppData;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import model.Setting;
import view.activity.ActAbout;
import view.activity.ActAboutWeb;
import view.activity.ActDonate;
import view.activity.ActReferal;
import view.activity.ChangePassword;
import view.activity.ChangePin;
import view.activity.LoginActivity;
import view.activity.QRActivity;
import view.activity.TransferToBank;
import view.activity.TransferToFriend;
import view.adapter.SettingAdapter;

public class SettingFragment extends Fragment implements SettingAdapter.ItemClick, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static SettingFragment fragment;
    private ProgressDialog progress;
    private List<Setting> mList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private AppCompatImageView imgQR;
    private Toolbar toolbar;
    private GoogleApiClient mGoogleApiClient;

    public static SettingFragment getInstance() {
        fragment = new SettingFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        imgQR = toolbar.findViewById(R.id.imgQR);
        imgQR.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.recyclerView);
        mList = new ArrayList<>();
        mAdapter = new SettingAdapter(mList, SettingFragment.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(mAdapter);
        progress = new ProgressDialog(getContext());
        progress.setMessage("Logout Please Wait..");

       try {
           GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                   .requestEmail()
                   .build();
           mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                   .enableAutoManage(getActivity(), this)
                   .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                   .build();
       }catch (Exception e){
           e.printStackTrace();
       }

        prepareData();

    }

    private void prepareData() {
        mList.clear();
        Setting setting = new Setting(R.drawable.ic_refer, "Refer a Friend");//0
        mList.add(setting);
        setting = new Setting(R.drawable.ic_bank, "Transfer to Bank");//1
        mList.add(setting);
        setting = new Setting(R.drawable.ic_people, "Transfer to Friend");//2
        mList.add(setting);

//        setting = new Setting(R.drawable.ic_donate, "Donate");//3
//        mList.add(setting);
//        setting = new Setting(R.drawable.ic_promotions, "Promotion");//4
//        mList.add(setting);

        setting = new Setting(R.drawable.ic_about, "About");//3
        mList.add(setting);
        setting = new Setting(R.drawable.ic_format, "Help");//4
        mList.add(setting);
        setting = new Setting(R.drawable.ic_lock, "Privacy Policy");//5
        mList.add(setting);
        setting = new Setting(R.drawable.ic_kay, "Change Password");//6
        mList.add(setting);
        setting = new Setting(R.drawable.ic_eye, "Change Pin");//7
        mList.add(setting);
        setting = new Setting(R.drawable.ic_exit, "Logout");//8
        mList.add(setting);

        mAdapter.notifyDataSetChanged();
    }
    int diff=0;
    @Override
    public void itemClick(int position) {
        switch (position) {
            case 0://refer to friend
                startActivity(new Intent(getContext(), ActReferal.class));
                Animatoo.animateSwipeRight(getContext());

                break;

            case 1://Transfer to Bank

//                final BottomDialog dialog1 = BottomDialog.newInstance("Select one of the option to transfer", "Cancel", new String[]{"Using QR Code", "Using Username"});
//                dialog1.show(getFragmentManager(), "dialog");
//                dialog1.setCancelable(false);
//                //add item click listener
//                dialog1.setListener(new BottomDialog.OnClickListener() {
//                    @Override
//                    public void click(int position) {
//                        if (position == 0) {
//                            Intent intent = new Intent(getContext(), TransferToBank.class);
//                            intent.putExtra("flagPurchase", "qr");
//                            startActivity(intent);
//                            dialog1.dismiss();
//                            Animatoo.animateSwipeRight(getContext());
//                        } else {
//                            Intent intent = new Intent(getContext(), TransferToBank.class);
//                            intent.putExtra("flagPurchase", "username");
//                            startActivity(intent);
//                            dialog1.dismiss();
//                            Animatoo.animateSwipeRight(getContext());
//                        }
//                    }
//                });
                break;

                case 2://Transfer to Friend
                final BottomDialog dialog = BottomDialog.newInstance("Select one of the option to transfer", "Cancel", new String[]{"Using QR Code", "Using Username"});
                dialog.show(getFragmentManager(), "dialog");
                dialog.setCancelable(false);
                //add item click listener
                dialog.setListener(new BottomDialog.OnClickListener() {
                    @Override
                    public void click(int position) {
                        if (position == 0) {
                            Intent intent = new Intent(getContext(), TransferToFriend.class);
                            intent.putExtra("flagPurchase", "qr");
                            startActivity(intent);
                            dialog.dismiss();
                            Animatoo.animateSwipeRight(getContext());
                        } else {
                            Intent intent = new Intent(getContext(), TransferToFriend.class);
                            intent.putExtra("flagPurchase", "username");
                            startActivity(intent);
                            dialog.dismiss();
                            Animatoo.animateSwipeRight(getContext());
                        }
                    }
                });
                break;

//            case 3://donate
//                startActivity(new Intent(getContext(), ActDonate.class));
//
//                Animatoo.animateSwipeRight(getContext());
//                break;
           case 3://about
               diff=1;
               NaviAbout(diff);
               break;
           case 4://Help
               diff=2;
               NaviAbout(diff);
               break;
           case 5://privacy policy
               diff=3;
               NaviAbout(diff);
               break;
          case 6://Change Password
                startActivity(new Intent(getContext(), ChangePassword.class));
                Animatoo.animateSwipeRight(getContext());
                break;

            case 7://Change Pin
                startActivity(new Intent(getContext(), ChangePin.class));
                Animatoo.animateSplit(getContext());
                break;

            case 8://Logout
                showAlert("Are you sure want to logout?");
                break;
        }
    }
    public void NaviAbout(int diff_1)
    {
//        startActivity(new Intent(getContext(), ActAbout.class).putExtra("diff_",diff_1));
        startActivity(new Intent(getContext(), ActAboutWeb.class).putExtra("diff_",diff_1));
        Animatoo.animateSwipeRight(getContext());
    }

    private void showAlert(String message) {
        final PrettyDialog prettyDialog = new PrettyDialog(getContext());
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
        prettyDialog.addButton("YES", R.color.black, R.color.white, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                prettyDialog.dismiss();
                if(isNetworkConnected()){
                    progress.show();
                    logout();
                }else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("")
                            .setMessage("Please connect to internet.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        }).show();

        prettyDialog.addButton("NO", R.color.black, R.color.white, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                prettyDialog.dismiss();
            }
        }).show();
    }

    private void logout() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppData appData = new AppData(getContext());
                String id = appData.getGoogleID();
                if (!id.equals("NA")) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    appData.clearData();
                                    progress.dismiss();
                                    startActivity(new Intent(getContext(), LoginActivity.class));
                                    getActivity().finish();
                                    Animatoo.animateSlideDown(getContext());
                                }
                            });
                } else {
                    appData.clearData();
                    progress.dismiss();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    getActivity().finish();
                    Animatoo.animateSlideDown(getContext());
                }
            }
        }, 3000);
    }



    @Override
    public void onClick(View v) {
        startActivity(new Intent(getContext(), QRActivity.class));
        Animatoo.animateSplit(getContext());
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish();
        Animatoo.animateSlideDown(getContext());
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onPause() {
        super.onPause();
       // mGoogleApiClient.stopAutoManage(getActivity());
        //mGoogleApiClient.disconnect();
    }
}
