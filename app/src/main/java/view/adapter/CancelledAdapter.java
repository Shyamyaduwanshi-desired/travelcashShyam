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

import model.HistoryModel;
import view.activity.TransactionDetail;
import view.customview.CustomTextView;

public class CancelledAdapter extends RecyclerView.Adapter<CancelledAdapter.MyViewHolder> {
    private List<HistoryModel> mList;
    private Activity activity;

    public CancelledAdapter(Activity activity, List<HistoryModel> mList) {
        this.activity = activity;
        this.mList = mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final HistoryModel history = mList.get(position);

        if(position >0 && position < mList.size()){
            String currentDate = mList.get(position).getDate();
            String prevDate = mList.get(position-1).getDate();
            if(currentDate.equals(prevDate)){
                holder.tvDate.setVisibility(View.GONE);
            }else {
                holder.tvDate.setVisibility(View.VISIBLE);
            }
        }

        holder.tvDate.setText(history.getDate());
        holder.tvPaymentMode.setText(history.getMode());
        holder.tvTime.setText(history.getTime());
        holder.tvAmount.setText(history.getAmount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TransactionDetail.class);
                intent.putExtra("flagPurchase", "0");
                intent.putExtra("transactionId", history.getID());
                intent.putExtra("mode", history.getMode());
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
        public CustomTextView tvAmount, tvDate, tvPaymentMode, tvTime;

        public MyViewHolder(View view) {
            super(view);
            tvDate = (CustomTextView) view.findViewById(R.id.tvDate);
            tvPaymentMode = (CustomTextView) view.findViewById(R.id.tvPaymentMode);
            tvTime = (CustomTextView) view.findViewById(R.id.tvTime);
            tvAmount = (CustomTextView) view.findViewById(R.id.tvAmount);
        }
    }
}
