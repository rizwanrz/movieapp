package com.example.popularmovies.rizwan.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;

import static android.content.ContentValues.TAG;

public class MovieRepository {

        static String DB_NAME = "movie_db";
        private  MovieDatabase movieDatabase;
        public MovieRepository(Context context) {
            movieDatabase = Room.databaseBuilder(context, MovieDatabase.class, DB_NAME).build();
        }

        public void insert(long movieId,
                               String title,
                               String overview,
                               String poster_path,
                               String backdrop_path,
                               String rating,
                               String release_date,
                               String voteCount,
                               boolean favorite) {

            Movies movies = new Movies();
            movies.setMovieId(movieId);
            movies.setTitle(title);
            movies.setRating(rating);
            movies.setOverview(overview);
            movies.setPoster_path(poster_path);
            movies.setBackdrop_path(backdrop_path);
            movies.setRelease_date(release_date);
            movies.setVoteCount(voteCount);
            movies.setFavorite(favorite);
            insert(movies);
        }

        public  void insert(final Movies movies) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    movieDatabase.daoAccess().insert(movies);
                    Log.d(TAG, "Movies Inserted!!");
                    return null;
                }
            }.execute();
        }

        public  void updateTask(final Movies movies) {

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    movieDatabase.daoAccess().updateTask(movies);
                    return null;
                }
            }.execute();
        }

    public  void updateMovie(final long id , final boolean favorite) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                movieDatabase.daoAccess().updateMovie(id, favorite);
                return null;
            }
        }.execute();
    }

    public  Movies getMovie(long id) {
            return movieDatabase.daoAccess().getMovie(id); }

    public  LiveData<List<Movies>> fetchAllFavoriteMovies(boolean favorite){
        if(movieDatabase != null)
            return movieDatabase.daoAccess().fetchAllFavoriteMovies(true);
        else
            return null;
    }

    public  List<Movies> fetchFavoriteMovies(boolean favorite){
        if(movieDatabase != null)
            return movieDatabase.daoAccess().fetchFavoriteMovies(true);
        else
            return null;
    }

    public  List<Movies> getAllMovies() {
        Log.d(TAG, "Fetch is called");
        if (movieDatabase != null)
            return movieDatabase.daoAccess().fetchAllMovies();
        else
            return null;
    }
}
