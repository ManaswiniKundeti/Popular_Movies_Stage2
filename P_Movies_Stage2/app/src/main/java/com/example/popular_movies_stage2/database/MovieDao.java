package com.example.popular_movies_stage2.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM FAVMOVIES")
    List<Movie> loadAllMovies();

    @Query("SELECT * FROM FAVMOVIES where movieId == :movieId")
    List<Movie> getMovie(String movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovieDetails(Movie movieDetailsEntry);

    @Update
    void updateMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

}
