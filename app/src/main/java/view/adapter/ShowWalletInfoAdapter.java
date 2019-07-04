package view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.travelcash.R;

import java.util.ArrayList;
import java.util.List;

import constant.AppData;
import constant.OrderData;
import model.ShowWalletInfoModel;
import view.activity.ShowWalletTransactionDetail;
import view.activity.SplashActivity;
import view.activity.WalletTransactionDetail;
import view.customview.CustomTextView;

public class ShowWalletInfoAdapter extends RecyclerView.Adapter<ShowWalletInfoAdapter.MyViewHolder> {
    private List<ShowWalletInfoModel> mList;
    private Activity activity;
    AppData appData;

    public ShowWalletInfoAdapter(Activity activity, List<ShowWalletInfoModel> mList) {
        this.activity = activity;
        this.mList = mList;
        appData=new AppData(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_show_wallet_info, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvDate.setText(appData.ConvertDate(mList.get(position).getRequest_date()));
//        holder.tvDate.setText(mList.get(position).getRequest_date());
        holder.tvStatus.setText(mList.get(position).getStatus());
        final String flag = mList.get(position).getRequest_status();
        if(flag.equals("1")){
            holder.tvAmount.setTextColor(activity.getResources().getColor(R.color.green));
        }else {
            holder.tvAmount.setTextColor(activity.getResources().getColor(R.color.light_red));
        }
        holder.tvAmount.setText("$ " + mList.get(position).getAmount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Gson gson = new Gson();

//                String listString = gson.toJson(
//                        mList.get(position),
//                        new TypeToken<ArrayList<ShowWalletInfoModel>>() {}.getType());

                Intent intent = new Intent(activity, ShowWalletTransactionDetail.class);
                intent.putExtra("tran_id", mList.get(position).getTransaction_id());
                intent.putExtra("tran_date", mList.get(position).getRequest_date());
                intent.putExtra("tran_amount", mList.get(position).getAmount());
                intent.putExtra("request_status", mList.get(position).getStatus());
                activity.startActivity(intent);
                Animatoo.animateSlideRight(activity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView tvAmount, tvDate, tvStatus;

        public MyViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tv_date);
            tvStatus = view.findViewById(R.id.tv_status);
            tvAmount = view.findViewById(R.id.tv_amount);
        }
    }
}
