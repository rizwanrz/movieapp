package com.example.popularmovies.rizwan.Fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.rizwan.Database.MovieRepository;
import com.example.popularmovies.rizwan.Database.Movies;
import com.example.popularmovies.rizwan.MyMovie;
import com.example.popularmovies.rizwan.R;
import com.squareup.picasso.Picasso;

public class MovieDetailsFragment extends Fragment {
    private View view;
    private static Button isFavorite;
    private MyMovie movie;
    public MovieRepository movieRepository;
    private static Movies movies = new Movies();
    private String TAG = MovieDetailsFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "loading Details Fragment");
        view = inflater.inflate(R.layout.movie_details, container, false);

        isFavorite = view.findViewById(R.id.addFav);
        movieRepository = new MovieRepository(getActivity().getApplication());

        Intent myIntent = getActivity().getIntent();
        movie = (MyMovie) myIntent.getSerializableExtra("movie");
        new LoadOneMovie().execute(movie);

        isFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFavorite.getText().equals(getString(R.string.add_to_favorite))){
                    movie.setFavorite(true);
                    isFavorite.setText(R.string.remove_favorite);
                } else {
                    movie.setFavorite(false);
                    isFavorite.setText(R.string.add_to_favorite);
                }

                new UpdateMovies().execute(String.valueOf(movie.getId()), String.valueOf(movie.isFavorite()));
                movies.setPoster_path(movie.getPoster_path());
                movies.setBackdrop_path(movie.getBackdrop_path());
                movies.setRelease_date(movie.getRelease_date());
                movies.setRating(movie.getRating());
                movies.setFavorite(movie.isFavorite());
                movies.setOverview(movie.getOverview());
                movies.setTitle(movie.getTitle());
                movies.setFavorite(movie.isFavorite());
                movies.setVoteCount(movie.getVoteCount());
                movies.setMovieId(movie.getId());
            }
        });

        if(movie != null) {
            setMovieUI(movie);
        } else {
            Log.d(TAG, "Nothing available in movie Object");
        }

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setMovieUI(MyMovie movie){
        TextView title = view.findViewById(R.id.movie_title);
        TextView rating =  view.findViewById(R.id.rating);
        TextView overview = view.findViewById(R.id.description);
        TextView yearOfRelease = view.findViewById(R.id.movie_year);
        ImageView movieImage =  view.findViewById(R.id.movie_image);
        isFavorite = view.findViewById(R.id.addFav);

        if(movie.getTitle()!=null)
            title.setText(movie.getTitle());
        else
            title.setText(R.string.no_title);

        if (movie.getVoteCount()!=null)
            rating.setText(movie.getVoteCount()+"/10");
        else
            rating.setText(R.string.no_rating);

        if (movie.getOverview()!=null)
            overview.setText(movie.getOverview());
        else
            overview.setText(R.string.no_overview);

        if (movie.getRelease_date()!=null)
            yearOfRelease.setText(movie.release_date);
        else
            yearOfRelease.setText(R.string.no_date);

        String imageURL = null;
        if(movie.getPoster_path()!=null) {
            imageURL = "http://image.tmdb.org/t/p/w500//".concat(movie.getPoster_path());
        }

        if (imageURL!= null)
            Picasso.get().load(imageURL).into(movieImage);
        else
            movieImage.setImageResource(R.drawable.images);

        new LoadOneMovie().execute(movie);
    }

    public class LoadOneMovie extends AsyncTask<MyMovie, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isFavorite.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean)
                isFavorite.setText(R.string.remove_favorite);
            else
                isFavorite.setText(R.string.add_to_favorite);

            isFavorite.setEnabled(true);
        }

        @Override
        protected Boolean doInBackground(MyMovie... movies) {
            if(movieRepository.getAllMovies().size()!= 0) {
                Movies movieDb = movieRepository.getMovie(movies[0].getId());
                return movieDb.isFavorite();
            }
            return null;
        }
    }

    public class UpdateMovies extends AsyncTask<String, Void, Void>{

        private String TAG = UpdateMovies.class.getName();

        @Override
        protected Void doInBackground(String... strings) {
            movieRepository.updateMovie(Long.parseLong(strings[0]), Boolean.parseBoolean(strings[1]));
            Log.d(TAG, "Updated DB called");
            return null;
        }
    }
}