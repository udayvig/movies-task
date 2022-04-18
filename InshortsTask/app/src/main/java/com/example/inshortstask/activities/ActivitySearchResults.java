package com.example.inshortstask.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.inshortstask.adapters.AdapterSearchResults;
import com.example.inshortstask.entities.Movie;
import com.example.inshortstask.R;
import com.example.inshortstask.viewmodels.ViewModelMovie;

import java.util.ArrayList;
import java.util.List;

public class ActivitySearchResults extends AppCompatActivity implements AdapterSearchResults.CommunicateSearch {

    public TextView searchStringTextView;
    public RecyclerView resultsRecyclerView;
    public LinearLayout emptyResultsLinearLayout;

    public ViewModelMovie movieVM;

    public List<Movie> results = new ArrayList<>();

    public AdapterSearchResults adapterSearchResults;

    private Context context;

    private AdapterSearchResults.CommunicateSearch comm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        context = this;

        comm = this;

        searchStringTextView = findViewById(R.id.searchStringTextView);
        resultsRecyclerView = findViewById(R.id.recyclerViewSearchResults);
        emptyResultsLinearLayout = findViewById(R.id.emptySearchResults);

        movieVM = new ViewModelProvider(this).get(ViewModelMovie.class);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search_result);

        final Handler handler = new Handler();

        final androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("TAG", "onQueryTextSubmit: " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("TAG", "onQueryTextChange: " + newText);

                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(newText.length() > 2){
                            movieVM.getSearchResults(newText).observe(ActivitySearchResults.this, new Observer<List<Movie>>() {
                                @Override
                                public void onChanged(List<Movie> movies) {
                                    if(newText.length() <= 3){
                                        searchStringTextView.setVisibility(View.VISIBLE);
                                        resultsRecyclerView.setVisibility(View.GONE);
                                        emptyResultsLinearLayout.setVisibility(View.GONE);
                                    }else if(newText.length() > 3 && movies.size() == 0){
                                        searchStringTextView.setVisibility(View.GONE);
                                        resultsRecyclerView.setVisibility(View.GONE);
                                        emptyResultsLinearLayout.setVisibility(View.VISIBLE);
                                    }else if(newText.length() > 3 && movies.size() > 0){
                                        searchStringTextView.setVisibility(View.GONE);
                                        resultsRecyclerView.setVisibility(View.VISIBLE);
                                        emptyResultsLinearLayout.setVisibility(View.GONE);
                                    }

                                    results.clear();
                                    results.addAll(movies);
                                    adapterSearchResults = new AdapterSearchResults(context, movies, comm);
                                    adapterSearchResults.setHasStableIds(true);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context,
                                            LinearLayoutManager.HORIZONTAL, false);
                                    resultsRecyclerView.setLayoutManager(mLayoutManager);
                                    resultsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                    resultsRecyclerView.setAdapter(adapterSearchResults);
                                }
                            });
                        }
                    }
                }, 400);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onOpenMovieSearch(int position) {
        Intent intent = new Intent(ActivitySearchResults.this, ActivityMovieDetails.class);
        intent.putExtra("id", results.get(position).getId());
        startActivity(intent);
    }
}