package view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.travelcash.R;

import java.util.List;

import constant.AppData;
import de.hdodenhof.circleimageview.CircleImageView;
import model.NotificationBean;
import view.activity.TransactionDetailNew;
import view.customview.CustomTextView;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.MyViewHolder> {
    private List<NotificationBean> mList;
    private Activity activity;
    AppData appData;
    public NotiAdapter(Activity activity, List<NotificationBean> mList) {
        this.activity = activity;
        this.mList = mList;
        appData=new AppData(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_notification, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final NotificationBean history = mList.get(position);

        holder.tvNm.setText(history.getAgent_name());
        holder.tvMsg.setText(history.getUser_message());
        holder.tvTime.setText(appData.ConvertTime(history.getDate()));
//        holder.tvAmount.setText("IDR " +history.getAmount());
if(TextUtils.isEmpty(mList.get(position).getAgent_image()))
{
    holder.ivShopPic.setImageResource(R.drawable.persion);
}
else
{
    Glide.with(activity).load(mList.get(position).getAgent_image()).thumbnail(0.5f)
            .into(holder.ivShopPic);
}
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(activity, TransactionDetailNew.class);
////                intent.putExtra("flagPurchase", "1");
//                intent.putExtra("transactionId", history.getID());
//                intent.putExtra("date", history.getDate());
//                intent.putExtra("amount", history.getAmount());
//                intent.putExtra("status", history.getMode());
//                activity.startActivity(intent);
//                Animatoo.animateCard(activity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView /*tvAmount,*/ tvNm, tvMsg, tvTime;
        CircleImageView ivShopPic;
        public MyViewHolder(View view) {
            super(view);
            tvNm = (CustomTextView) view.findViewById(R.id.tv_name);
            tvMsg = (CustomTextView) view.findViewById(R.id.tv_msg);
            tvTime = (CustomTextView) view.findViewById(R.id.tv_time);
            ivShopPic = view.findViewById(R.id.img_pic);
//            tvAmount = (CustomTextView) view.findViewById(R.id.tv_amount);
        }
    }
}
