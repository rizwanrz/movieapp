package com.example.popularmovies.rizwan;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    List<EachVideo> trailers;
    private static final String TAG = VideoAdapter.class.getName();
    private VideoAdapterClickHandler mClickHandler;

    public interface VideoAdapterClickHandler {
        void onClick(EachVideo video);
    }

    public VideoAdapter(List<EachVideo> trailers, VideoAdapterClickHandler clickHandler){
        this.trailers = trailers;
        this.mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View eachItem = layoutInflater.inflate(R.layout.video_layout, parent, false);
        return new VideoViewHolder(eachItem);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {

        if(trailers != null){
            EachVideo eachVideo = trailers.get(position);
            String key = eachVideo.getVideoKey();
            if(key != null){
                holder.movieVideo.setText(eachVideo.getVideoName());
                holder.movieVideo.setVisibility(View.VISIBLE);
                holder.movieVideo.setTooltipText(eachVideo.getVideoName());
            } else {
                Log.d(TAG, "Video Not Available");
            }
        }
    }

    @Override
    public int getItemCount() {
        if(trailers !=null)
            return trailers.size();
        return 0;
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Button movieVideo;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            movieVideo = itemView.findViewById(R.id.video_view);
            movieVideo.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "Adapter Item Clicked");
            mClickHandler.onClick(trailers.get(getAdapterPosition()));
        }
    }

    public void setMyVideos(List<EachVideo> videos) {
        trailers= videos;
        notifyDataSetChanged();
    }
}
