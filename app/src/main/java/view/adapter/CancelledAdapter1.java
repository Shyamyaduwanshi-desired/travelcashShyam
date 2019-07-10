package view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.travelcash.R;

import java.text.DecimalFormat;
import java.util.List;

import constant.AppData;
import model.HistoryModel;
import view.activity.TransactionDetailNew;
import view.customview.CustomTextView;

public class CancelledAdapter1 extends RecyclerView.Adapter<CancelledAdapter1.MyViewHolder> {
    private List<HistoryModel> mList;
    private Activity activity;
    AppData appData;
    public CancelledAdapter1(Activity activity, List<HistoryModel> mList) {
        this.activity = activity;
        this.mList = mList;
        appData=new AppData(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cancelled_history_01, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final HistoryModel history = mList.get(position);

        holder.tvDate.setText(appData.ConvertDate4(history.getDate())+", "+appData.ConvertTime(history.getDate()));
        holder.tvStatus.setText(history.getMode());

        if(history.getAmount().contains(",")) {
            holder.tvAmount.setText("IDR " + history.getAmount());
        }
        else
        {
            DecimalFormat df = new DecimalFormat("#,###,###,###");
            double dd = Double.parseDouble(history.getAmount());
            holder.tvAmount.setText("IDR " + df.format(dd));
        }

        holder.tvStatus.setTypeface(null, Typeface.ITALIC);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, TransactionDetailNew.class);
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
        public TextView tvAmount, tvDate, tvStatus,tvAgentNm;//, tvRejectedDate

        public MyViewHolder(View view) {
            super(view);
            tvDate =  view.findViewById(R.id.tv_date);
            tvAmount = view.findViewById(R.id.tv_amount);
            tvAgentNm = view.findViewById(R.id.tv_agent_nm);
            tvStatus =  view.findViewById(R.id.tv_status);
//            tvRejectedDate = view.findViewById(R.id.tv_rejected_date);

        }
    }
}
