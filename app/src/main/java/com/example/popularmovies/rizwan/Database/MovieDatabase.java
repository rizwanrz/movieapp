package com.example.popularmovies.rizwan.Database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movies.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
        private static MovieDatabase sInstance;
        private static final Object LOCK = new Object();
        public static MovieDatabase getInstance(Context context) {
                if (sInstance == null) {
                        synchronized (LOCK) {
                                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                                        MovieDatabase.class, MovieRepository.DB_NAME)
                                        .build();
                        }
                }
                return sInstance;
        }
        public abstract MovieDBSchema daoAccess();
}
