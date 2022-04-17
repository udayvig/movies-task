package com.example.inshortstask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ActivityMovieDetails extends AppCompatActivity {

    public TextView isAdultTextView, budgetTextView, genresTextView, homepageTextView,
            originalTitleTextView, overviewTextView, producedByTextView, releaseDateTextView,
            revenueTextView, runtimeTextView, taglineTextView, voteCountTextView, voteAverageTextView;

    public ImageView backdropPathImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        isAdultTextView = findViewById(R.id.adultTextView);
        backdropPathImageView = findViewById(R.id.backdropImageView);
        budgetTextView = findViewById(R.id.budgetTextView);
        genresTextView = findViewById(R.id.genresTextView);
        homepageTextView = findViewById(R.id.homepageTextView);
        originalTitleTextView = findViewById(R.id.titleTextView);
        overviewTextView = findViewById(R.id.overViewTextView);
        producedByTextView = findViewById(R.id.producedByTextView);
        releaseDateTextView = findViewById(R.id.releaseDateTextView);
        revenueTextView = findViewById(R.id.revenueTextView);
        runtimeTextView = findViewById(R.id.runtimeTextView);
        taglineTextView = findViewById(R.id.taglineTextView);
        voteCountTextView = findViewById(R.id.voteCountTextView);
        voteAverageTextView = findViewById(R.id.voteAverageTextView);

        ViewModelMovie movieVM = new ViewModelProvider(this).get(ViewModelMovie.class);

        Intent intent = getIntent();
        int id = -1;

        if(intent != null){
            id = intent.getIntExtra("id", -1);

            Uri uri = intent.getData();
            if(uri != null){
                //here from link
                String url = uri.toString();
                String[] splitUrl = url.split("/");

                String encodedID = splitUrl[splitUrl.length - 1];
                id = decodeID(encodedID);
            }
        }

        if(id != -1){
            movieVM.getMovieDetails(id).observe(this, new Observer<MovieDetails>() {
                @Override
                public void onChanged(MovieDetails movieDetails) {
                    if(movieDetails.isAdult()) {
                        isAdultTextView.setText("Required");
                    }else{
                        isAdultTextView.setText("Not Required");
                    }
                    budgetTextView.setText(movieDetails.getBudget() + " USD");
                    genresTextView.setText(movieDetails.getGenres());
                    homepageTextView.setText(movieDetails.getHomepage());
                    originalTitleTextView.setText(movieDetails.getOriginalTitle());
                    overviewTextView.setText(movieDetails.getOverview());
                    producedByTextView.setText(movieDetails.getProducedBy());
                    releaseDateTextView.setText(movieDetails.getReleaseDate());
                    revenueTextView.setText(movieDetails.getRevenue() + " USD");
                    runtimeTextView.setText(movieDetails.getRuntime() + " Minutes");
                    taglineTextView.setText(movieDetails.getTagline());
                    voteCountTextView.setText(movieDetails.getVoteCount() + " Votes");
                    voteAverageTextView.setText(movieDetails.getVoteAverage() + "/10");

                    RequestOptions options = new RequestOptions()
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.mipmap.ic_launcher_round);

                    Glide.with(ActivityMovieDetails.this)
                            .load("https://image.tmdb.org/t/p/w500" + movieDetails.getBackdropPath())
                            .apply(options)
                            .into(backdropPathImageView);
                }
            });
        }
    }

    public int decodeID(String encodedID) {
        int id = 0;

        for (int i = 0; i < encodedID.length(); i++) {
            if ('a' <= encodedID.charAt(i) && encodedID.charAt(i) <= 'z')
                id = id * 62 + encodedID.charAt(i) - 'a';
            if ('A' <= encodedID.charAt(i) && encodedID.charAt(i) <= 'Z')
                id = id * 62 + encodedID.charAt(i) - 'A' + 26;
            if ('0' <= encodedID.charAt(i) && encodedID.charAt(i) <= '9')
                id = id * 62 + encodedID.charAt(i) - '0' + 52;
        }
        return id;
    }
}