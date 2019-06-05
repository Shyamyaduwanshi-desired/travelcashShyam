package view.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.travelcash.R;

import java.util.List;

import model.Setting;
import view.customview.CustomTextViewBold;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.MyViewHolder> {
    private List<Setting> mList;
    private ItemClick itemClick;

    public SettingAdapter(List<Setting> mList, ItemClick itemClick) {
        this.mList = mList;
        this.itemClick = itemClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_setting, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.imageView.setImageResource(mList.get(position).getImage());
        holder.tvTitle.setText(mList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.itemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextViewBold tvTitle;
        private AppCompatImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            tvTitle = view.findViewById(R.id.tvTitle);
        }
    }

    public interface ItemClick{
        void itemClick(int position);
    }
}
