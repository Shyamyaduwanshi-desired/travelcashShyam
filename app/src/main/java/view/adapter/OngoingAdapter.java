package view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.travelcash.R;
import java.util.List;
import model.HistoryModel;
import view.customview.CustomTextView;

public class OngoingAdapter extends RecyclerView.Adapter<OngoingAdapter.MyViewHolder> {
    private List<HistoryModel> mList;

    public OngoingAdapter(List<HistoryModel> mList) {
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

        if (position > 0 && position < mList.size()) {
            String currentDate = mList.get(position).getDate();
            String prevDate = mList.get(position - 1).getDate();
            if (currentDate.equals(prevDate)) {
                holder.tvDate.setVisibility(View.GONE);
            } else {
                holder.tvDate.setVisibility(View.VISIBLE);
            }
        }

        holder.tvDate.setText(history.getDate());
        holder.tvPaymentMode.setText(history.getMode());
        holder.tvTime.setText(history.getTime());
        holder.tvAmount.setText("IDR " + history.getAmount());
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
