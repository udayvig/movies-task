package com.example.inshortstask.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.inshortstask.adapters.AdapterBookmarkedMovie;
import com.example.inshortstask.entities.MovieDetails;
import com.example.inshortstask.R;
import com.example.inshortstask.viewmodels.ViewModelMovie;

import java.util.List;

public class ActivityBookmarkedMovies extends AppCompatActivity implements AdapterBookmarkedMovie.CommunicateBookmarks {

    private RecyclerView bookmarkedRecyclerView;
    private LinearLayout emptyBookmarkedLinearLayout;

    private ViewModelMovie movieVM;
    private AdapterBookmarkedMovie adapterBookmarkedMovie;

    private Context context;

    private AdapterBookmarkedMovie.CommunicateBookmarks comm;

    private List<MovieDetails> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarked_movies);

        context = this;

        comm = this;

        bookmarkedRecyclerView = findViewById(R.id.recyclerViewBookmarked);
        emptyBookmarkedLinearLayout = findViewById(R.id.emptyBookmarked);

        movieVM = new ViewModelProvider(this).get(ViewModelMovie.class);

        movieVM.getAllBookmarkedMovies().observe(this, new Observer<List<MovieDetails>>() {
            @Override
            public void onChanged(List<MovieDetails> movieDetails) {
                if(movieDetails.size() == 0){
                    emptyBookmarkedLinearLayout.setVisibility(View.VISIBLE);
                    bookmarkedRecyclerView.setVisibility(View.GONE);
                }else{
                    emptyBookmarkedLinearLayout.setVisibility(View.GONE);
                    bookmarkedRecyclerView.setVisibility(View.VISIBLE);
                }

                movies = movieDetails;
                adapterBookmarkedMovie = new AdapterBookmarkedMovie(movieDetails, comm);
                adapterBookmarkedMovie.setHasStableIds(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                bookmarkedRecyclerView.setLayoutManager(mLayoutManager);
                bookmarkedRecyclerView.setItemAnimator(new DefaultItemAnimator());
                bookmarkedRecyclerView.setAdapter(adapterBookmarkedMovie);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                movieVM.deleteMovie(adapterBookmarkedMovie.getMovieAt(viewHolder.getAdapterPosition()));
                Toast.makeText(context, "Bookmark Removed", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(bookmarkedRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bookmarked, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete_all_bookmark:
                movieVM.deleteAllMovies();
                Toast.makeText(context, "All bookmarks removed", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOpen(int position) {
        Intent intent = new Intent(ActivityBookmarkedMovies.this, ActivityMovieDetails.class);
        intent.putExtra("id", movies.get(position).getId());
        startActivity(intent);
    }
}