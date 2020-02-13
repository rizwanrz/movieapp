package com.example.popularmovies.rizwan.Database;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
    public interface MovieDBSchema {
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        Long insert(Movies movies);

        @Query("SELECT * FROM Movies ORDER BY movieId desc")
        LiveData<List<Movies>> fetchAllTasks();

        @Query("SELECT * FROM Movies ORDER BY movieId desc")
        List<Movies> fetchAllMovies();

        @Query("SELECT * FROM Movies WHERE favorite =:favorite")
        LiveData<List<Movies>> fetchAllFavoriteMovies(boolean favorite);

        @Query("SELECT * FROM Movies WHERE favorite =:favorite")
        List<Movies> fetchFavoriteMovies(boolean favorite);

        @Query("SELECT * FROM Movies WHERE movieId =:taskId")
        Movies getMovie(long taskId);

        @Update
        void updateTask(Movies movie);

        @Query("UPDATE Movies set favorite=:value  WHERE movieId =:id")
        void updateMovie(long id, boolean value);

        @Delete
        void deleteTask(Movies movie);
}
