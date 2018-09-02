package com.naijaplanet.magosla.android.moviesplanet.data.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


@Dao
public interface FavoriteMoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     void addMovies(FavoriteMovies... movies);

    @Update
    void updateMovies(FavoriteMovies... movies);

    @Delete
    void deleteMovies(FavoriteMovies... movies);

    @Query("SELECT * FROM favorite_movies")
    List<FavoriteMovies> findAll();

    @Query("SELECT * FROM favorite_movies LIMIT :offset, :length")
    List<FavoriteMovies> findWithLimits(int offset, int length);

    @Query("SELECT id FROM favorite_movies WHERE id = :movieId LIMIT 1")
     LiveData<Favorite> isFavorite(int movieId);
}
