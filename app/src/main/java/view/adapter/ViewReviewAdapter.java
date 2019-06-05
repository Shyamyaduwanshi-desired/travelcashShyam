package view.adapter;

import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.travelcash.R;

import java.util.List;

import model.UserReview;
import view.customview.CustomTextView;

public class ViewReviewAdapter extends RecyclerView.Adapter<ViewReviewAdapter.MyViewHolder> {
    private List<UserReview> mList;

    public ViewReviewAdapter(List<UserReview> mList) {
        this.mList = mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_review, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        UserReview review = mList.get(position);
        holder.tvName.setText(review.getName());
        holder.tvDate.setText(review.getDate());
        holder.tvCount.setText("(" + review.getRating() + ")");
        holder.tvReview.setText(review.getMessage());
        holder.ratingBar.setRating(Float.parseFloat(review.getRating()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView tvName, tvDate, tvCount, tvReview;
        private AppCompatRatingBar ratingBar;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvDate = view.findViewById(R.id.tvDate);
            tvCount = view.findViewById(R.id.tvCount);
            tvReview = view.findViewById(R.id.tvReview);
            ratingBar = view.findViewById(R.id.ratingBar);
        }
    }
}
