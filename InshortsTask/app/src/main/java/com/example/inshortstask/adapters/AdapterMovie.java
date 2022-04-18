package com.example.inshortstask.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.inshortstask.entities.Movie;
import com.example.inshortstask.R;

import java.util.List;

public class AdapterMovie extends RecyclerView.Adapter<AdapterMovie.MovieViewHolder> {
    Context context;
    List<Movie> movieList;
    int type;
    Communicate communicate;

    public AdapterMovie(Context context, List<Movie> movieList, int type, Communicate communicate){
        this.context = context;
        this.movieList = movieList;
        this.communicate = communicate;
        this.type = type;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_movie, parent, false);

        return new AdapterMovie.MovieViewHolder(itemView, communicate);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.releaseDateTextView.setText("Released On: " + movie.getReleaseDate());
        holder.titleTextView.setText(movie.getTitle());
        if(movie.isAdult()){
            holder.adultTextView.setText("A");
        }else{
            holder.adultTextView.setText("U/A");
        }
        holder.numberVotesTextView.setText(movie.getVoteCount() + " Votes");
        holder.averageVoteTextView.setText(movie.getVoteAverage() + " Avg");

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                .apply(options)
                .into(holder.backgroundImageView);

        if(communicate.checkIfBookmarked(movieList.get(position).getId())){
            holder.bookmarkButton.setImageResource(R.drawable.ic_baseline_bookmark_24);
        }else{
            holder.bookmarkButton.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
        }

        holder.bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = (String) holder.bookmarkButton.getTag();
                if(tag == null || tag.equals("not_bookmarked")){
                    holder.bookmarkButton.setTag("bookmarked");
                    holder.bookmarkButton.setImageResource(R.drawable.ic_baseline_bookmark_24);
                    communicate.onBookmarkMovie(holder.getAdapterPosition(), type);
                }else if(tag.equals("bookmarked")){
                    holder.bookmarkButton.setTag("not_bookmarked");
                    holder.bookmarkButton.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
                    communicate.onDeleteBookmarkedMovie(holder.getAdapterPosition(), type);
                }
            }
        });

        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = movie.getId();
                String link = convertID(id);

                String url = "https://www.example.com/" + link;

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                context.startActivity(shareIntent);
            }
        });
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

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView backgroundImageView;
        public TextView averageVoteTextView, numberVotesTextView, adultTextView, titleTextView, releaseDateTextView;
        public ImageButton bookmarkButton, shareButton;
        public Communicate communicate;

        public MovieViewHolder(@NonNull View itemView, Communicate communicate) {
            super(itemView);
            this.communicate = communicate;

            backgroundImageView = itemView.findViewById(R.id.imageViewBackground);
            averageVoteTextView = itemView.findViewById(R.id.avgVoteTextView);
            numberVotesTextView = itemView.findViewById(R.id.numVotesTextView);
            adultTextView = itemView.findViewById(R.id.adultTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            releaseDateTextView = itemView.findViewById(R.id.releaseDateTextView);
            bookmarkButton = itemView.findViewById(R.id.bookmarkMovieButton);
            shareButton = itemView.findViewById(R.id.shareButton);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.e("TAG", "onClick: ");
            communicate.onOpenMovie(getAdapterPosition(), type);
        }
    }

    public String convertID(int n) {
        char[] map = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

        StringBuilder shorturl = new StringBuilder();

        while (n > 0) {
            shorturl.append(map[n % 62]);
            n = n / 62;
        }

        return shorturl.reverse().toString();
    }

    public interface Communicate{
        void onOpenMovie(int position, int type);
        void onBookmarkMovie(int position, int type);
        void onDeleteBookmarkedMovie(int position, int type);
        boolean checkIfBookmarked(int id);
    }
}
