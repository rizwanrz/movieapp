package com.example.popularmovies.rizwan;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

public class ReviewAdapter extends Adapter<ReviewAdapter.ReviewList> {

    private List<EachReview> reviews;
    private static final String TAG = ReviewAdapter.class.getName();
    private ReviewAdapterClickHandler mClickHandler;

    public void setMyReviews(List<EachReview> eachReviews) {
        reviews = eachReviews;
        notifyDataSetChanged();
    }

    public interface ReviewAdapterClickHandler {
        void onClick(EachReview review);
    }

    public ReviewAdapter(List<EachReview> reviews, ReviewAdapterClickHandler clickHandler){
        this.reviews = reviews;
        this.mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ReviewList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View eachItem = layoutInflater.inflate(R.layout.review_fragment, parent, false);
        return new ReviewList(eachItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewList holder, int position) {
        if(reviews != null) {
            holder.authorName.setText(reviews.get(position).getAuthor());
            holder.contentDes.setText(reviews.get(position).getContent());
            holder.url.setText(reviews.get(position).getUrl());
        } else {
            Log.d(TAG, "Reviews Not Available");
        }
    }

    @Override
    public int getItemCount() {
        if(reviews != null)
            return reviews.size();
        return 0;
    }

    public class ReviewList extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView authorName,contentDes,url;

        public ReviewList(@NonNull View itemView) {
            super(itemView);
            authorName = itemView.findViewById(R.id.author_name);
            contentDes = itemView.findViewById(R.id.content_name);
            url = itemView.findViewById(R.id.url_name);
            url.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onClick(reviews.get(getAdapterPosition()));
        }
    }
}
