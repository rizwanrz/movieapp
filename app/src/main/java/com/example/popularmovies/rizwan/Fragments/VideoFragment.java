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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.popularmovies.rizwan.EachVideo;
import com.example.popularmovies.rizwan.R;
import com.example.popularmovies.rizwan.VideoAdapter;

import java.util.List;

public class VideoFragment extends Fragment {
    private final static String YOUTUBE_PATH = "https://www.youtube.com/watch?v=";
    private static String TAG = VideoFragment.class.getName();

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view;
       List<EachVideo> eachVideos;
       RecyclerView recyclerView;
       TextView textViewError;

       Log.d(TAG, "loading Details Fragment");
       view = inflater.inflate(R.layout.review_video_fragment, container, false);

       recyclerView = view.findViewById(R.id.videoReviewsList);
       textViewError = view.findViewById(R.id.error_video_review);
       Bundle bundle = this.getArguments();

       eachVideos = (List<EachVideo>) bundle.getSerializable("videos");

       if(eachVideos != null && eachVideos.size() > 0) {
           recyclerView.setVisibility(View.VISIBLE);
           textViewError.setVisibility(View.GONE);
           recyclerView.setHasFixedSize(true);
           recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
           VideoAdapter videoAdapter = new VideoAdapter(eachVideos, new VideoAdapter.VideoAdapterClickHandler() {
                @Override
                public void onClick(EachVideo video) { Log.d(TAG, "Video Item Clicked");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    intent.setData(Uri.parse(YOUTUBE_PATH.concat(video.getVideoKey())));
                    startActivity(intent);
                }
                }
            });
           videoAdapter.setMyVideos(eachVideos);
           recyclerView.setAdapter(videoAdapter);
        } else {
           recyclerView.setVisibility(View.GONE);
           textViewError.setVisibility(View.VISIBLE);
           textViewError.setText(R.string.no_review);
        }
        return view;
    }
}