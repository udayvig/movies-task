package com.example.inshortstask.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_table")
public class Movie {

    @PrimaryKey
    private int id;

    private boolean isAdult;

    private String backdropPath;

    private String title;

    private String overview;

    private double popularity;

    private String posterPath;

    private String releaseDate;

    private double voteAverage;

    private int voteCount;

    private int type;

    public Movie(int id, boolean isAdult, String backdropPath, String title, String overview,
                 double popularity, String posterPath, String releaseDate, double voteAverage,
                 int voteCount, int type) {
        this.id = id;
        this.isAdult = isAdult;
        this.backdropPath = backdropPath;
        this.title = title;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public int getType(){
        return type;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", isAdult=" + isAdult +
                ", backdropPath='" + backdropPath + '\'' +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", popularity=" + popularity +
                ", posterPath='" + posterPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                ", type='" + type + '\'' +
                '}';
    }
}
