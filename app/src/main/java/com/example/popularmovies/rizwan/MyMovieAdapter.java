package com.example.popularmovies.rizwan;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyMovieAdapter extends RecyclerView.Adapter<MyMovieAdapter.MyMovieList> {

    private static List<MyMovie> myMovies;
    private static final String TAG = MyMovieAdapter.class.getName();
    private static MyMovieAdapterOnClickHandler mClickHandler;

    public interface MyMovieAdapterOnClickHandler {
        void onClick(MyMovie movie);
    }

    public MyMovieAdapter(List<MyMovie> movies) {
        myMovies = movies;
    }

    public MyMovieAdapter(List<MyMovie> movies, MyMovieAdapterOnClickHandler clickHandler) {
        myMovies = movies;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MyMovieList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View eachItem = layoutInflater.inflate(R.layout.each_item, parent, false);
        return new MyMovieList(eachItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMovieList holder, int position) {
        if (myMovies != null) {
            MyMovie myMovie = myMovies.get(position);
            //If there is no URL in poster path, set the default image(No Image Available)
            if (myMovie.getPoster_path().isEmpty()) {
                Log.d(TAG, "Poster is not available!!");
                Picasso.get().load(R.drawable.images).into(holder.imageView);
            } else {
                Log.d(TAG, myMovie.poster_path);
                String imageURL = "http://image.tmdb.org/t/p/w500//".concat(myMovie.poster_path);
                Log.d(TAG, imageURL);
                Picasso.get().load(imageURL).into(holder.imageView);
            }
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount called!!");
        if(myMovies!=null)
            return myMovies.size();
        else
            return 0;
    }

    public static class MyMovieList extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView imageView;
        private final LinearLayout linearLayout;
        private MyMovieList(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movie_imageView);
            linearLayout = itemView.findViewById(R.id.imageView_layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Log.d(TAG, getAdapterPosition()+"");
            MyMovie movie = myMovies.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }

    public void setMyMovies(List<MyMovie> movies) {
        myMovies= movies;
        notifyDataSetChanged();
    }
}

