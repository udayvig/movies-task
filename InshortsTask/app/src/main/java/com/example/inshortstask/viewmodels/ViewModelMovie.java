package com.example.inshortstask.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inshortstask.repository.MovieRepository;
import com.example.inshortstask.entities.Movie;
import com.example.inshortstask.entities.MovieDetails;

import java.util.List;

public class ViewModelMovie extends AndroidViewModel {
    private MovieRepository movieRepository;
    private LiveData<List<Movie>> popularMovies;
    private LiveData<List<Movie>> nowPlayingMovies;
    private LiveData<List<MovieDetails>> bookmarkedMovies;

    public ViewModelMovie(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        popularMovies = movieRepository.getPopularMovies();
        nowPlayingMovies = movieRepository.getNowPlayingMovies();
        bookmarkedMovies = movieRepository.getAllBookmarkedMovies();
    }

    public void insertMovie(MovieDetails movieDetails){
        movieRepository.insert(movieDetails);
    }

    public void deleteMovie(MovieDetails movieDetails){
        movieRepository.delete(movieDetails);
    }

    public void deleteAllMovies(){
        movieRepository.deleteAllMovies();
    }

    public LiveData<List<Movie>> getAllPopularMovies(){
        return popularMovies;
    }

    public LiveData<List<Movie>> getAllNowPlayingMovies(){
        return nowPlayingMovies;
    }

    public LiveData<List<MovieDetails>> getAllBookmarkedMovies(){
        return bookmarkedMovies;
    }

    public MutableLiveData<MovieDetails> getMovieDetails(int id){
        return movieRepository.getMovieDetails(id);
    }

    public MutableLiveData<List<Movie>> getSearchResults(String query){
        return movieRepository.getSearchResults(query);
    }

    public void insertAllMovies(List<Movie> allMovies){
        movieRepository.insertAllMovies(allMovies);
    }
}
