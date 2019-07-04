package view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import java.util.ArrayList;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import model.GetCancelOrderBean;
import model.HistoryModel;
import presenter.CancelOrderPresenter;
import presenter.GetCancelOrderDataPresenter;
import presenter.HistoryPresenter;
import view.activity.WriteReview;
import view.adapter.CancelledAdapter;
import view.adapter.CancelledAdapterNew;
import view.adapter.CompletedAdapter;
import view.adapter.CompletedAdapterNew;
import view.adapter.OngoingAdapter;
import view.adapter.OngoingAdapterNew;

public class DynamicFragment extends Fragment implements HistoryPresenter.History,OngoingAdapterNew.Clickable, CancelOrderPresenter.CancelInfo {
    //, GetCancelOrderDataPresenter.CancelHistoryInfo
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private HistoryPresenter presenter;
    private CancelOrderPresenter cancelOrderPresenter;
    private static DynamicFragment tabFragment;
    private static int count;
    public static int  refreshCancle=0;

    public static DynamicFragment newInstance() {
        tabFragment = new DynamicFragment();
        return tabFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        presenter = new HistoryPresenter(getContext(), DynamicFragment.this);
        cancelOrderPresenter = new CancelOrderPresenter(getContext(), DynamicFragment.this);
        recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        count = 1;
        refreshCancle=0;
//        CallAPI();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(refreshCancle==1)
        {
            presenter.getCanceledHistoryNew();
            refreshCancle=0;
        }
        CallAPI();
    }

    public  void CallAPI()
    {
        if (isNetworkConnected()) {
            if (HistoryFragment.position == 0) {
                presenter.getOngoingHistory();
            }
            else if (HistoryFragment.position == 1)
                presenter.getCompletedHistory();
            else if (HistoryFragment.position == 2)
//                presenter.getCanceledHistory();
                presenter.getCanceledHistoryNew();

//            presenter.getCanceledHistoryNew(); //for refresh

        } else {
            showDialog("Please connect to internet.");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Override
    public void success(ArrayList<HistoryModel> response) {
        if (HistoryFragment.position == 0)
            mAdapter = new OngoingAdapterNew(getActivity(),response,DynamicFragment.this);
//            mAdapter = new OngoingAdapter(response);
        else if (HistoryFragment.position == 1)

        mAdapter = new CompletedAdapterNew(getActivity(), response);
//            mAdapter = new CompletedAdapter(getActivity(), response);
        else if (HistoryFragment.position == 2)
            mAdapter = new CancelledAdapterNew(getActivity(), response);

        recyclerView.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();
        runLayoutAnimation(recyclerView);
    }

////for  cancelled tab
//    @Override
//    public void successCancelled(ArrayList<GetCancelOrderBean> response) {
////         if (HistoryFragment.position == 2)
////            mAdapter = new CancelledAdapterNew(getActivity(), response);
////
////           recyclerView.setAdapter(mAdapter);
//////        mAdapter.notifyDataSetChanged();
////        runLayoutAnimation(recyclerView);
//    }

    @Override
    public void error(String response) {
        if (count == 1) {
            showDialog(response);
            count++;
        }
    }

    @Override
    public void fail(String response) {
        if (count == 1) {
            showDialog(response);
            count++;
        }
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(getContext())
                .setTitle("")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).show();
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

//for ongoingadapter click
    @Override
    public void onClick(String  requstid) {
//        Toast.makeText(getActivity(), ""+requstid, Toast.LENGTH_SHORT).show();
        ShowNewAlert(getActivity(),"Do you want to proceed cancel order?",requstid);

    }
    //for cancel order at ongoing
    @Override
    public void success(String response) {
        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
        presenter.getCanceledHistoryNew();
        CallAPI();

    }
    PrettyDialog prettyDialog=null;
    private void ShowNewAlert(Context context,String message,String requstid) {
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
        prettyDialog.addButton("No", R.color.black, R.color.white, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                prettyDialog.dismiss();
            }
        }).show();
        prettyDialog.addButton("Yes", R.color.black, R.color.white, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                prettyDialog.dismiss();
                if (isNetworkConnected()) {
                    cancelOrderPresenter.CancelOrderMethod(requstid);
                }
                else {
                    showDialog("Please connect to internet");
                }
            }
        }).show();
    }

}
