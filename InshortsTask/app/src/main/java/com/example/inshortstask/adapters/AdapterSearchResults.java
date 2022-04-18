package com.example.inshortstask.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.inshortstask.entities.Movie;
import com.example.inshortstask.R;

import java.util.List;

public class AdapterSearchResults extends RecyclerView.Adapter<AdapterSearchResults.SearchResultViewHolder>{
    Context context;
    List<Movie> movieList;
    AdapterSearchResults.CommunicateSearch communicate;

    public AdapterSearchResults(Context context, List<Movie> movieList,
                                AdapterSearchResults.CommunicateSearch communicate){
        this.context = context;
        this.movieList = movieList;
        this.communicate = communicate;
    }

    @NonNull
    @Override
    public AdapterSearchResults.SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_movie, parent, false);

        return new AdapterSearchResults.SearchResultViewHolder(itemView, communicate);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSearchResults.SearchResultViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.releaseDateTextView.setText("Released On: " + movie.getReleaseDate());
        holder.titleTextView.setText(movie.getTitle());
        holder.adultTextView.setText(movie.isAdult() + "");
        holder.numberVotesTextView.setText(movie.getVoteCount() + "");
        holder.averageVoteTextView.setText(movie.getVoteAverage() + "");

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                .apply(options)
                .into(holder.backgroundImageView);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class SearchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView backgroundImageView;
        public TextView averageVoteTextView, numberVotesTextView, adultTextView, titleTextView, releaseDateTextView;
        public AdapterSearchResults.CommunicateSearch communicate;

        public SearchResultViewHolder(@NonNull View itemView, AdapterSearchResults.CommunicateSearch communicate) {
            super(itemView);
            this.communicate = communicate;

            backgroundImageView = itemView.findViewById(R.id.imageViewBackground);
            averageVoteTextView = itemView.findViewById(R.id.avgVoteTextView);
            numberVotesTextView = itemView.findViewById(R.id.numVotesTextView);
            adultTextView = itemView.findViewById(R.id.adultTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            releaseDateTextView = itemView.findViewById(R.id.releaseDateTextView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.e("TAG", "onClick: ");
            communicate.onOpenMovieSearch(getAdapterPosition());
        }
    }

    public interface CommunicateSearch{
        void onOpenMovieSearch(int position);
    }
}
