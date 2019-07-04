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
import view.activity.ScanActivity;
import view.customview.CustomTextView;
import view.customview.CustomTextViewBold;

public class OngoingAdapterNew extends RecyclerView.Adapter<OngoingAdapterNew.MyViewHolder> {
    private List<HistoryModel> mList;
    private Activity activity;
    AppData appData;
    private OngoingAdapterNew.Clickable clickable;
    public OngoingAdapterNew(Activity activity, List<HistoryModel> mList, OngoingAdapterNew.Clickable clickable) {
        this.activity = activity;
        this.mList = mList;
        appData=new AppData(activity);
        this.clickable = clickable;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ongoing_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final HistoryModel history = mList.get(position);

//        if (position > 0 && position < mList.size()) {
//            String currentDate = mList.get(position).getDate();
//            String prevDate = mList.get(position - 1).getDate();
//            if (currentDate.equals(prevDate)) {
//                holder.tvDate.setVisibility(View.GONE);
//            } else {
//                holder.tvDate.setVisibility(View.VISIBLE);
//            }
//        }
        if(history.getTime().equals("Pending")||history.getTime()=="Pending")
        {
            holder.tvProceed.setVisibility(View.GONE);
        }
        else
        {
            holder.tvProceed.setVisibility(View.VISIBLE);
        }

        holder.tvDate.setText(appData.ConvertDate4(history.getDate()));
        holder.tvNm.setText(history.getMode());
        holder.tvStatus.setText("Status:"+history.getTime());
        holder.tvAmount.setText("IDR " + history.getAmount());
//        holder.tvTime.setText(history.getDate());
        holder.tvTime.setText(appData.ConvertTime(history.getDate()));
        holder.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickable.onClick(history.getID());
            }
        });
        holder.tvProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ScanActivity.class);
                intent.putExtra("amount", history.getAmount());
                intent.putExtra("agent_recieved_request_id", history.getID());
                activity.startActivity(intent);
//        finish();
//        Animatoo.animateFade(ConfirmCashpoint.this);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView tvAmount, tvDate, tvNm, tvStatus, tvTime;
        CustomTextViewBold tvCancel, tvProceed;

        public MyViewHolder(View view) {
            super(view);
            tvDate = (CustomTextView) view.findViewById(R.id.tv_date);
            tvNm = (CustomTextView) view.findViewById(R.id.tv_nm);
            tvStatus = (CustomTextView) view.findViewById(R.id.tv_status);
            tvTime = (CustomTextView) view.findViewById(R.id.tv_time);
            tvAmount = (CustomTextView) view.findViewById(R.id.tv_amount);
            tvCancel = (CustomTextViewBold) view.findViewById(R.id.tv_cancel);
            tvProceed = (CustomTextViewBold) view.findViewById(R.id.tv_proceed);
        }
    }

    public interface Clickable {
        void onClick(String  position);
    }
}
