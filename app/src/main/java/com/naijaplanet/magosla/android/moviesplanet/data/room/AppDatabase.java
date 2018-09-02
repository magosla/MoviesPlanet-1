package com.naijaplanet.magosla.android.moviesplanet.data.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {FavoriteMovies.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavoriteMoviesDao favoriteMoviesDao();
}
