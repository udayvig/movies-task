package com.example.inshortstask;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterBookmarkedMovie extends RecyclerView.Adapter<AdapterBookmarkedMovie.BookmarkedMovieViewHolder> {

    List<MovieDetails> movieList;
    CommunicateBookmarks communicate;

    public AdapterBookmarkedMovie(List<MovieDetails> movieList, CommunicateBookmarks communicate){
        this.movieList = movieList;
        this.communicate = communicate;
    }

    public MovieDetails getMovieAt(int pos){
        return movieList.get(pos);
    }

    @NonNull
    @Override
    public BookmarkedMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_bookmarked_movie, parent, false);

        return new AdapterBookmarkedMovie.BookmarkedMovieViewHolder(itemView, communicate);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkedMovieViewHolder holder, int position) {
        holder.titleTextView.setText(movieList.get(position).getOriginalTitle());
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

    public class BookmarkedMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView titleTextView;
        public AdapterBookmarkedMovie.CommunicateBookmarks communicate;

        public BookmarkedMovieViewHolder(@NonNull View itemView, CommunicateBookmarks communicate) {
            super(itemView);
            this.communicate = communicate;

            titleTextView = itemView.findViewById(R.id.bookmarkedTitleTextView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            communicate.onOpen(getAdapterPosition());
        }
    }

    public interface CommunicateBookmarks{
        void onOpen(int position);
    }
}
