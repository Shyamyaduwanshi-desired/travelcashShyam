package view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.travelcash.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.CashPoint;
import model.Vendor;
import presenter.VendorListPresenter;
import view.activity.ConfirmCashpoint;
import view.customview.CustomTextView;

public class CashPointAdapter extends RecyclerView.Adapter<CashPointAdapter.MyViewHolder> {
    private List<Vendor> mList;
    private Activity activity;
    private Clickable clickable;

    public CashPointAdapter(Activity activity, List<Vendor> mList, Clickable clickable) {
        this.activity = activity;
        this.mList = mList;
        this.clickable = clickable;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cash_point_new, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if(mList.get(position).getShopName().length()>40)
        {
            holder.tvName.setText(mList.get(position).getShopName().substring(0,40)+"...");
        }
        else
        {
            holder.tvName.setText(mList.get(position).getShopName());
        }

//        holder.tvName.setText(mList.get(position).getShopName());
        holder.tvLocation.setText(mList.get(position).getDistance() + " KM");
        holder.tvAddress.setText(mList.get(position).getShopAddress());
//        Glide.with(activity).load(mList.get(position).getShopImage()) .thumbnail(0.5f)
//               /* .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .error(R.drawable.persion)*/
//                .into(holder.imgShop);
       Glide.with(activity).load(mList.get(position).getShopImage()).thumbnail(0.5f)
               /* .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.persion)*/
                .into(holder.ivShopPic);

//        holder.imgBanner.setVisibility(View.VISIBLE);
        if (position != 0 && position % 3 == 0) {
            holder.imgBanner.setVisibility(View.VISIBLE);
        } else {
            holder.imgBanner.setVisibility(View.GONE);
        }

        String isPromo = mList.get(position).getIsPromo();
        String isPurchase = mList.get(position).getIsPurchase();

//        holder.tvPromotion.setVisibility(View.GONE);
        if (isPromo.equals("1")) {
            holder.tvPromotion.setVisibility(View.VISIBLE);
        }else {
            holder.tvPromotion.setVisibility(View.GONE);
        }
        Log.e("","isPurchase= "+isPurchase);
        holder.tvPurchase.setVisibility(View.GONE);
//        if (isPurchase.equals("1")) {
//            holder.tvPurchase.setVisibility(View.VISIBLE);
//        }else {
//            holder.tvPurchase.setVisibility(View.GONE);
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickable.onClick(position);
            }
        });

        holder.imgBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView tvName, tvLocation, tvAddress, tvPromotion, tvPurchase;
        private AppCompatImageView  imgBanner;//imgShop,
        CircleImageView ivShopPic;
        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvLocation = view.findViewById(R.id.tvLocation);
            tvAddress = view.findViewById(R.id.tvAddress);
//            imgShop = view.findViewById(R.id.imgShop);
            ivShopPic = view.findViewById(R.id.img_pic);
            tvPromotion = view.findViewById(R.id.tvPromotion);
            tvPurchase = view.findViewById(R.id.tvPurchase);
            imgBanner = view.findViewById(R.id.imgBanner);
        }
    }

    public interface Clickable {
        void onClick(int position);
    }
}
