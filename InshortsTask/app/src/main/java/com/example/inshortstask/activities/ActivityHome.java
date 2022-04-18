package com.example.inshortstask.activities;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.inshortstask.adapters.AdapterMovie;
import com.example.inshortstask.entities.Movie;
import com.example.inshortstask.entities.MovieDetails;
import com.example.inshortstask.R;
import com.example.inshortstask.viewmodels.ViewModelMovie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ActivityHome extends AppCompatActivity implements AdapterMovie.Communicate {

    RecyclerView nowPlayingRecyclerView, popularRecyclerView;
    LinearLayout emptyNowPlayingLinearLayout, emptyPopularLinearLayout;
    AdapterMovie adapterPopular, adapterNowPlaying;
    List<Movie> popularMoviesList = new ArrayList<>();
    List<Movie> nowPlayingMoviesList = new ArrayList<>();
    List<Integer> bookmarkedMovieIDs = new ArrayList<>();
    Context context;
    private AdapterMovie.Communicate comm;

    private ViewModelMovie movieVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = this;

        comm = this;

        nowPlayingRecyclerView = findViewById(R.id.recyclerViewNowPlaying);
        popularRecyclerView = findViewById(R.id.recyclerViewPopular);

        emptyNowPlayingLinearLayout = findViewById(R.id.emptyNowPlaying);
        emptyPopularLinearLayout = findViewById(R.id.emptyPopular);

        movieVM = new ViewModelProvider(this).get(ViewModelMovie.class);

        movieVM.getAllPopularMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                popularMoviesList = movies;
                Log.e("TAG", "onChanged: " + movies.size());
                initAfterList(0);
            }
        });

        movieVM.getAllNowPlayingMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                nowPlayingMoviesList = movies;
                Log.e("TAG", "onChanged: " + movies.size());
                initAfterList(1);
            }
        });

        movieVM.getAllBookmarkedMovies().observe(this, new Observer<List<MovieDetails>>() {
            @Override
            public void onChanged(List<MovieDetails> movieDetails) {
                bookmarkedMovieIDs.clear();
                for(MovieDetails movieDetailsObject : movieDetails){
                    bookmarkedMovieIDs.add(movieDetailsObject.getId());
                }
            }
        });
    }

    public void initAfterList(int type){
        if(type == 0){
            if(popularMoviesList.size() == 0){
                popularRecyclerView.setVisibility(GONE);
                emptyPopularLinearLayout.setVisibility(View.VISIBLE);
            }else{
                popularRecyclerView.setVisibility(View.VISIBLE);
                emptyPopularLinearLayout.setVisibility(View.GONE);
            }

            popularMoviesList.sort(new Comparator<Movie>() {
                @Override
                public int compare(Movie o1, Movie o2) {
                    return (int) (o2.getPopularity() - o1.getPopularity());
                }
            });

            adapterPopular = new AdapterMovie(context, popularMoviesList, 0, comm);
            adapterPopular.setHasStableIds(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL, false);
            popularRecyclerView.setLayoutManager(mLayoutManager);
            popularRecyclerView.setItemAnimator(new DefaultItemAnimator());
            popularRecyclerView.setAdapter(adapterPopular);
        }else if(type == 1){
            if(nowPlayingMoviesList.size() == 0){
                nowPlayingRecyclerView.setVisibility(GONE);
                emptyNowPlayingLinearLayout.setVisibility(View.VISIBLE);
            }else{
                nowPlayingRecyclerView.setVisibility(View.VISIBLE);
                emptyNowPlayingLinearLayout.setVisibility(View.GONE);
            }

            nowPlayingMoviesList.sort(new Comparator<Movie>() {
                @Override
                public int compare(Movie o1, Movie o2) {
                    return (int) (o2.getPopularity() - o1.getPopularity());
                }
            });

            adapterNowPlaying = new AdapterMovie(context, nowPlayingMoviesList, 1, comm);
            adapterNowPlaying.setHasStableIds(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL, false);
            nowPlayingRecyclerView.setLayoutManager(mLayoutManager);
            nowPlayingRecyclerView.setItemAnimator(new DefaultItemAnimator());
            nowPlayingRecyclerView.setAdapter(adapterNowPlaying);
        }
    }

    @Override
    public void onOpenMovie(int position, int type) {
        Intent intent = new Intent(ActivityHome.this, ActivityMovieDetails.class);

        Log.e("TAG", "onOpenMovie: ");
        if(type == 0){
            intent.putExtra("id", popularMoviesList.get(position).getId());
        }else if(type == 1){
            intent.putExtra("id", nowPlayingMoviesList.get(position).getId());
        }

        startActivity(intent);
    }

    @Override
    public void onBookmarkMovie(int position, int type) {
        if(type == 0){
            Log.e("TAG", "onBookmarkMovie: " + popularMoviesList.get(position).toString());
            movieVM.getMovieDetails(popularMoviesList.get(position).getId()).observe(ActivityHome.this, new Observer<MovieDetails>() {
                @Override
                public void onChanged(MovieDetails movieDetails) {
                    movieVM.insertMovie(movieDetails);
                }
            });
        }else if(type == 1){
            Log.e("TAG", "onBookmarkMovie: " + nowPlayingMoviesList.get(position).toString());
            movieVM.getMovieDetails(nowPlayingMoviesList.get(position).getId()).observe(ActivityHome.this, new Observer<MovieDetails>() {
                @Override
                public void onChanged(MovieDetails movieDetails) {
                    movieVM.insertMovie(movieDetails);
                }
            });
        }
    }

    @Override
    public void onDeleteBookmarkedMovie(int position, int type) {
        if(type == 0){
            Log.e("TAG", "onBookmarkMovie: " + popularMoviesList.get(position).toString());
            movieVM.getMovieDetails(popularMoviesList.get(position).getId()).observe(ActivityHome.this, new Observer<MovieDetails>() {
                @Override
                public void onChanged(MovieDetails movieDetails) {
                    movieVM.deleteMovie(movieDetails);
                }
            });
        }else if(type == 1){
            Log.e("TAG", "onBookmarkMovie: " + nowPlayingMoviesList.get(position).toString());
            movieVM.getMovieDetails(nowPlayingMoviesList.get(position).getId()).observe(ActivityHome.this, new Observer<MovieDetails>() {
                @Override
                public void onChanged(MovieDetails movieDetails) {
                    movieVM.deleteMovie(movieDetails);
                }
            });
        }
    }

    @Override
    public boolean checkIfBookmarked(int id) {
        return bookmarkedMovieIDs.contains(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_movie, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                startActivity(new Intent(ActivityHome.this, ActivitySearchResults.class));
                break;

            case R.id.action_to_bookmark:
                startActivity(new Intent(ActivityHome.this, ActivityBookmarkedMovies.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}