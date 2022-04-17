package com.example.inshortstask;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MovieRepository {
    private MovieDao movieDao;

    private LiveData<List<Movie>> popularMovies;
    private LiveData<List<Movie>> nowPlayingMovies;
    private LiveData<List<MovieDetails>> bookmarkedMovies;
    private MutableLiveData<MovieDetails> movieDetails = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> searchResults = new MutableLiveData<>();

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private Application application;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

    APIInterface apiInterface = retrofit.create(APIInterface.class);

    public MovieRepository(Application application){
        this.application = application;

        MovieDatabase movieDB = MovieDatabase.getInstance(application);
        movieDao = movieDB.movieDao();

        popularMovies = movieDao.getAllPopularMovies();
        nowPlayingMovies = movieDao.getAllNowPlayingMovies();
        bookmarkedMovies = movieDao.getBookmarkedMovies();
    }

    public void insert(MovieDetails movieDetails){
        new InsertAsyncTask(movieDao).execute(movieDetails);
    }

    public void delete(MovieDetails movieDetails){
        new DeleteAsyncTask(movieDao).execute(movieDetails);
    }

    public void deleteAllMovies(){
        new DeleteAllAsyncTask(movieDao).execute();
    }

    public LiveData<List<Movie>> getPopularMovies(){
        Call<String> call = apiInterface.getPopularMovies("8ae0da453cfd6d6fd78ffd1cccfca730");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                List<Movie> fetchedMovies = new ArrayList<>();

                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    JsonObject root = gson.fromJson(response.body(), JsonObject.class);

                    JsonArray results = root.getAsJsonArray("results");

                    for(JsonElement ele : results){
                        JsonObject obj = ele.getAsJsonObject();
                        Movie movie;

                        if(!obj.get("poster_path").isJsonNull()){
                            movie = new Movie(obj.get("id").getAsInt(), obj.get("adult").getAsBoolean(),
                                    obj.get("backdrop_path").getAsString(), obj.get("title").getAsString(),
                                    obj.get("overview").getAsString(), obj.get("popularity").getAsDouble(),
                                    obj.get("poster_path").getAsString(), obj.get("release_date").getAsString(),
                                    obj.get("vote_average").getAsDouble(), obj.get("vote_count").getAsInt(), 0);
                        }else{
                            movie = new Movie(obj.get("id").getAsInt(), obj.get("adult").getAsBoolean(),
                                    obj.get("backdrop_path").getAsString(), obj.get("title").getAsString(),
                                    obj.get("overview").getAsString(), obj.get("popularity").getAsDouble(),
                                    "", obj.get("release_date").getAsString(),
                                    obj.get("vote_average").getAsDouble(), obj.get("vote_count").getAsInt(), 0);
                        }

                        fetchedMovies.add(movie);
                    }

                    insertAllMovies(fetchedMovies);
                }else{
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("TAG", "onResponse: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });

        return popularMovies;
    }

    public LiveData<List<Movie>> getNowPlayingMovies(){
        Call<String> call = apiInterface.getNowPlayingMovies("8ae0da453cfd6d6fd78ffd1cccfca730");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                List<Movie> fetchedMovies = new ArrayList<>();

                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    JsonObject root = gson.fromJson(response.body(), JsonObject.class);

                    JsonArray results = root.getAsJsonArray("results");

                    for(JsonElement ele : results){
                        JsonObject obj = ele.getAsJsonObject();

                        Movie movie = new Movie(obj.get("id").getAsInt(), obj.get("adult").getAsBoolean(),
                                obj.get("backdrop_path").getAsString(), obj.get("title").getAsString(),
                                obj.get("overview").getAsString(), obj.get("popularity").getAsDouble(),
                                obj.get("poster_path").getAsString(), obj.get("release_date").getAsString(),
                                obj.get("vote_average").getAsDouble(), obj.get("vote_count").getAsInt(), 1);

                        fetchedMovies.add(movie);
                    }

                    insertAllMovies(fetchedMovies);
                }else{
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("TAG", "onResponse: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });

        return nowPlayingMovies;
    }

    public LiveData<List<MovieDetails>> getAllBookmarkedMovies(){
        return bookmarkedMovies;
    }

    public MutableLiveData<MovieDetails> getMovieDetails(int id){
        Call<String> call = apiInterface.getDetailsForMovie(id,"8ae0da453cfd6d6fd78ffd1cccfca730");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                MovieDetails details;
                List<String> genresList = new ArrayList<>();
                List<String> productionCompaniesList = new ArrayList<>();

                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    JsonObject root = gson.fromJson(response.body(), JsonObject.class);

                    JsonArray genres = root.getAsJsonArray("genres");
                    JsonArray productionCompanies = root.getAsJsonArray("production_companies");

                    for(JsonElement ele : genres){
                        JsonObject obj = ele.getAsJsonObject();

                        genresList.add(obj.get("name").getAsString());
                    }

                    for(JsonElement ele : productionCompanies){
                        JsonObject obj = ele.getAsJsonObject();

                        productionCompaniesList.add(obj.get("name").getAsString());
                    }

                    String genresString = "", producedByString = "";
                    StringBuilder sb = new StringBuilder();
                    for(String str : genresList){
                        sb.append(str);
                        sb.append(", ");
                    }

                    if(sb.length() >= 2){
                        sb.deleteCharAt(sb.length() - 1);
                        sb.deleteCharAt(sb.length() - 2);
                    }

                    genresString = sb.toString();

                    sb = new StringBuilder();
                    for(String producer : productionCompaniesList){
                        sb.append(producer);
                        sb.append(", ");
                    }

                    if(sb.length() >= 2){
                        sb.deleteCharAt(sb.length() - 1);
                        sb.deleteCharAt(sb.length() - 2);
                    }

                    producedByString = sb.toString();

                    details = new MovieDetails(id, root.get("adult").getAsBoolean(),
                            root.get("backdrop_path").getAsString(),
                            root.get("budget").getAsLong(), genresString, root.get("homepage").getAsString(),
                            root.get("original_title").getAsString(), root.get("overview").getAsString(),
                            producedByString, root.get("release_date").getAsString(),
                            root.get("revenue").getAsLong(), root.get("runtime").getAsInt(),
                            root.get("tagline").getAsString(), root.get("vote_count").getAsInt(),
                            root.get("vote_average").getAsDouble());

                    movieDetails.postValue(details);
                }else{
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("TAG", "onResponse: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });

        return movieDetails;
    }

    public MutableLiveData<List<Movie>> getSearchResults(String query){
        Call<String> call = apiInterface.getSearchResults("8ae0da453cfd6d6fd78ffd1cccfca730", query);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                List<Movie> fetchedSearchedResults = new ArrayList<>();

                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    JsonObject root = gson.fromJson(response.body(), JsonObject.class);

                    JsonArray results = root.getAsJsonArray("results");

                    for(JsonElement ele : results){
                        JsonObject obj = ele.getAsJsonObject();
                        Movie movie;

                        Log.e("TAG", "onResponse: " + obj);

                        String backdropPath = "", posterPath = "", releaseDate = "";

                        if(!obj.get("backdrop_path").isJsonNull()){
                            backdropPath = obj.get("backdrop_path").getAsString();
                        }

                        if(!obj.get("poster_path").isJsonNull()){
                            posterPath = obj.get("poster_path").getAsString();
                        }

                        if(obj.has("release_date") && !obj.get("release_date").isJsonNull()){
                            releaseDate = obj.get("release_date").getAsString();
                        }

                        movie = new Movie(obj.get("id").getAsInt(), obj.get("adult").getAsBoolean(),
                                backdropPath, obj.get("title").getAsString(),
                                obj.get("overview").getAsString(), obj.get("popularity").getAsDouble(),
                                posterPath, releaseDate,
                                obj.get("vote_average").getAsDouble(), obj.get("vote_count").getAsInt(), 0);

                        fetchedSearchedResults.add(movie);
                    }

                    searchResults.postValue(fetchedSearchedResults);
                }else{
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("TAG", "onResponse: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        return searchResults;
    }

    public void insertAllMovies(List<Movie> allMovies){
        new InsertAllAsyncTask(movieDao).execute(allMovies);
    }

    private static class InsertAsyncTask extends AsyncTask<MovieDetails, Void, Void> {
        private MovieDao movieDao;

        public InsertAsyncTask(MovieDao movieDao){
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(MovieDetails... moviesDetails) {
            movieDao.bookmarkMovie(moviesDetails[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<MovieDetails, Void, Void> {
        private MovieDao movieDao;

        public DeleteAsyncTask(MovieDao movieDao){
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(MovieDetails... moviesDetails) {
            movieDao.deleteBookmarkedMovie(moviesDetails[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private MovieDao movieDao;

        public DeleteAllAsyncTask(MovieDao movieDao){
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            movieDao.deleteAllMovies();
            return null;
        }
    }

    private static class InsertAllAsyncTask extends AsyncTask<List<Movie>, Void, Void> {
        private MovieDao movieDao;

        public InsertAllAsyncTask(MovieDao movieDao){
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(List<Movie>... lists) {
            movieDao.insertAllMovies(lists[0]);
            return null;
        }
    }
}
