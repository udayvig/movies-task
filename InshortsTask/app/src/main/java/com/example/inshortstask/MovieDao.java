package com.example.inshortstask;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bookmarkMovie(MovieDetails movie);

    @Delete
    void deleteBookmarkedMovie(MovieDetails movie);

    @Query("DELETE FROM bookmark_table")
    void deleteAllMovies();

    @Query("SELECT * FROM bookmark_table")
    LiveData<List<MovieDetails>> getBookmarkedMovies();

    @Query("SELECT * FROM movie_table WHERE type = 0 ORDER BY popularity DESC")
    LiveData<List<Movie>> getAllPopularMovies();

    @Query("SELECT * FROM movie_table WHERE type = 1 ORDER BY popularity DESC")
    LiveData<List<Movie>> getAllNowPlayingMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllMovies(List<Movie> allMovies);
}
