package view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import java.util.List;

import constant.AppData;
import model.HistoryModel;
import view.activity.TransactionDetail;
import view.activity.TransactionDetailNew;
import view.customview.CustomTextView;

public class CancelledAdapterNew extends RecyclerView.Adapter<CancelledAdapterNew.MyViewHolder> {
    private List<HistoryModel> mList;
    private Activity activity;
    AppData appData;
    public CancelledAdapterNew(Activity activity, List<HistoryModel> mList) {
        this.activity = activity;
        this.mList = mList;
        appData=new AppData(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cancel_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final HistoryModel history = mList.get(position);

        holder.tvDate.setText(appData.ConvertDate4(history.getDate()));
        holder.tvStatus.setText(history.getMode());
//        if(history.getTime().equals("0000-00-00 00:00:00")||history.getTime().equals("0000-00-00 00:00:00"))
//        {
//            holder.tvRejectedDate.setText("no date");
//        }
//        else {
//            holder.tvRejectedDate.setText(appData.ConvertDate4(history.getTime()));
//
//        }
        holder.tvRejectedDate.setText(appData.ConvertTime(history.getDate()));
        holder.tvAmount.setText("IDR " +history.getAmount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(activity, TransactionDetail.class);
//                intent.putExtra("flagPurchase", "0");
//                intent.putExtra("transactionId", history.getID());
//                intent.putExtra("mode", history.getMode());
//                activity.startActivity(intent);
//                Animatoo.animateCard(activity);

                Intent intent = new Intent(activity, TransactionDetailNew.class);
//                intent.putExtra("flagPurchase", "1");
                intent.putExtra("transactionId", history.getID());
                intent.putExtra("date", history.getDate());
                intent.putExtra("amount", history.getAmount());
                intent.putExtra("status", history.getMode());
                activity.startActivity(intent);
                Animatoo.animateCard(activity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView tvAmount, tvDate, tvStatus, tvRejectedDate;

        public MyViewHolder(View view) {
            super(view);
            tvDate = (CustomTextView) view.findViewById(R.id.tv_date);
            tvStatus = (CustomTextView) view.findViewById(R.id.tv_status);
            tvRejectedDate = (CustomTextView) view.findViewById(R.id.tv_rejected_date);
            tvAmount = (CustomTextView) view.findViewById(R.id.tv_amount);
        }
    }
}
