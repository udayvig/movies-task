package com.example.inshortstask;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("movie/popular")
    Call<String> getPopularMovies(@Query("api_key") String key);

    @GET("movie/now_playing")
    Call<String> getNowPlayingMovies(@Query("api_key") String key);

    @GET("movie/{movie_id}")
    Call<String> getDetailsForMovie(@Path("movie_id") int id, @Query("api_key") String key);

    @GET("search/movie")
    Call<String> getSearchResults(@Query("api_key") String key, @Query("query") String query);
}
