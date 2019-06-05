package view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import java.util.List;

import model.RecentActivity;
import view.activity.WalletTransactionDetail;
import view.customview.CustomTextView;

public class RecentActivityAdapter extends RecyclerView.Adapter<RecentActivityAdapter.MyViewHolder> {
    private List<RecentActivity> mList;
    private Activity activity;

    public RecentActivityAdapter(Activity activity, List<RecentActivity> mList) {
        this.activity = activity;
        this.mList = mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recent_activity, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvShopName.setText(mList.get(position).getName());
        holder.tvDate.setText(mList.get(position).getDate());
        final String flag = mList.get(position).getFlag();
        if(flag.equals("1")){
            holder.tvAmount.setTextColor(activity.getResources().getColor(R.color.green));
        }else {
            holder.tvAmount.setTextColor(activity.getResources().getColor(R.color.light_red));
        }
        holder.tvAmount.setText("$ " + mList.get(position).getAmount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WalletTransactionDetail.class);
                intent.putExtra("flagPurchase", flag);
                intent.putExtra("transactionId", mList.get(position).getID());
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
        public CustomTextView tvAmount, tvShopName, tvDate;

        public MyViewHolder(View view) {
            super(view);
            tvShopName = view.findViewById(R.id.tvShopName);
            tvDate = view.findViewById(R.id.tvDate);
            tvAmount = view.findViewById(R.id.tvAmount);
        }
    }
}
