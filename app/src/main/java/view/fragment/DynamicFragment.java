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
import com.travelcash.R;
import java.util.ArrayList;
import model.History;
import presenter.HistoryPresenter;
import view.adapter.CancelledAdapter;
import view.adapter.CompletedAdapter;

public class DynamicFragment extends Fragment implements HistoryPresenter.History {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private HistoryPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        presenter = new HistoryPresenter(getContext(), DynamicFragment.this);
        recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (isNetworkConnected()) {
            if (HistoryFragment.position == 0)
                presenter.getCompletedHistory();
            else if (HistoryFragment.position == 1)
                presenter.getCanceledHistory();
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
    public void success(ArrayList<History> response) {
        if (HistoryFragment.position == 0)
            mAdapter = new CompletedAdapter(getActivity(), response);
        else if (HistoryFragment.position == 1)
            mAdapter = new CancelledAdapter(getActivity(), response);

        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
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
}
