package com.example.popularmovies.rizwan.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmovies.rizwan.EachReview;
import com.example.popularmovies.rizwan.R;
import com.example.popularmovies.rizwan.ReviewAdapter;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewFragment extends Fragment {
    private static String TAG = VideoFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "loading Details Fragment");
        View view;
        List<EachReview> eachReviews;
        RecyclerView recyclerView;
        view = inflater.inflate(R.layout.review_video_fragment, container, false);

        TextView textViewError = view.findViewById(R.id.error_video_review);
        recyclerView = view.findViewById(R.id.videoReviewsList);
        Bundle bundle = this.getArguments();

        eachReviews = (List<EachReview>) bundle.getSerializable("reviews");
        if(eachReviews != null && eachReviews.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            textViewError.setVisibility(View.GONE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

            ReviewAdapter reviewAdapter = new ReviewAdapter(eachReviews, new ReviewAdapter.ReviewAdapterClickHandler() {
                @Override
                public void onClick(EachReview review) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(review.getUrl()));
                }
            });

            reviewAdapter.setMyReviews(eachReviews);
            recyclerView.setAdapter(reviewAdapter);
        } else{
            recyclerView.setVisibility(View.GONE);
            textViewError.setVisibility(View.VISIBLE);
            textViewError.setText(getString(R.string.no_review));
        }
        return view;
    }
}
