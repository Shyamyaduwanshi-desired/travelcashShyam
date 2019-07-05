package view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travelcash.R;

import java.util.List;

import constant.AppData;
import model.HistoryModel;
import view.activity.ScanActivity;
import view.customview.CustomTextView;
import view.customview.CustomTextViewBold;

public class OngoingAdapterNew1 extends RecyclerView.Adapter<OngoingAdapterNew1.MyViewHolder> {
    private List<HistoryModel> mList;
    private Activity activity;
    AppData appData;
    private OngoingAdapterNew1.Clickable clickable;
    public OngoingAdapterNew1(Activity activity, List<HistoryModel> mList, OngoingAdapterNew1.Clickable clickable) {
        this.activity = activity;
        this.mList = mList;
        appData=new AppData(activity);
        this.clickable = clickable;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ongoing_history_new, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final HistoryModel history = mList.get(position);

        if(history.getTime().equals("Pending")||history.getTime()=="Pending")
        {
            holder.lyProceed.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.lyProceed.setVisibility(View.VISIBLE);
        }

        holder.tvDate.setText(appData.ConvertDate4(history.getDate())+", "+appData.ConvertTime(history.getDate()));
        holder.tvNm.setText(history.getMode());
        holder.tvStatus.setText(history.getTime());
        holder.tvAmount.setText("IDR " + history.getAmount());

        holder.tvStatus.setTypeface(null, Typeface.ITALIC);

//        holder.tvTime.setText(history.getDate());
//        holder.tvTime.setText(appData.ConvertTime(history.getDate()));

        holder.lyCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickable.onClick(history.getID());
            }
        });
        holder.lyProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ScanActivity.class);
                intent.putExtra("amount", history.getAmount());
                intent.putExtra("agent_recieved_request_id", history.getID());
                intent.putExtra("agent_id", history.getAgentId());
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
        public TextView tvAmount, tvDate, tvStatus, tvNm;//, tvTime
//        CustomTextViewBold tvCancel, tvProceed;
LinearLayout lyProceed,lyCancel,lyNavigate;
        public MyViewHolder(View view) {
            super(view);
            tvDate = (TextView) view.findViewById(R.id.tv_date);
            tvAmount = (TextView) view.findViewById(R.id.tv_amount);
            tvNm = (TextView) view.findViewById(R.id.tv_agent_nm);
            tvStatus = (TextView) view.findViewById(R.id.tv_status);

            lyProceed = (LinearLayout) view.findViewById(R.id.ly_proceed);
            lyCancel = (LinearLayout) view.findViewById(R.id.ly_cancel);
            lyNavigate = (LinearLayout) view.findViewById(R.id.ly_navigate);

//            tvTime = (CustomTextView) view.findViewById(R.id.tv_time);
//            tvCancel = (CustomTextViewBold) view.findViewById(R.id.tv_cancel);
//            tvProceed = (CustomTextViewBold) view.findViewById(R.id.tv_proceed);
        }
    }

    public interface Clickable {
        void onClick(String position);
    }
}
