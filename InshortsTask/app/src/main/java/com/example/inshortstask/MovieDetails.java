package com.example.inshortstask;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "bookmark_table")
public class MovieDetails {

    @PrimaryKey
    private int id;

    private boolean isAdult;

    private String backdropPath;

    private long budget;

    private String genres;

    private String homepage;

    private String originalTitle;

    private String overview;

    private String producedBy;

    private String releaseDate;

    private long revenue;

    private int runtime;

    private String tagline;

    private int voteCount;

    private double voteAverage;

    public MovieDetails(int id, boolean isAdult, String backdropPath, long budget,
                        String genres, String homepage, String originalTitle,
                        String overview, String producedBy, String releaseDate,
                        long revenue, int runtime, String tagline, int voteCount,
                        double voteAverage) {
        this.id = id;
        this.isAdult = isAdult;
        this.backdropPath = backdropPath;
        this.budget = budget;
        this.genres = genres;
        this.homepage = homepage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.producedBy = producedBy;
        this.releaseDate = releaseDate;
        this.revenue = revenue;
        this.runtime = runtime;
        this.tagline = tagline;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
    }

    public int getId(){
        return id;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public long getBudget() {
        return budget;
    }

    public String getGenres() {
        return genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getProducedBy() {
        return producedBy;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public long getRevenue() {
        return revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getTagline() {
        return tagline;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    @Override
    public String toString() {
        return "MovieDetails{" +
                "id=" + id +
                ", isAdult=" + isAdult +
                ", backdropPath='" + backdropPath + '\'' +
                ", budget=" + budget +
                ", genres=" + genres +
                ", homepage='" + homepage + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", overview='" + overview + '\'' +
                ", producedBy=" + producedBy +
                ", releaseDate='" + releaseDate + '\'' +
                ", revenue=" + revenue +
                ", runtime=" + runtime +
                ", tagline='" + tagline + '\'' +
                ", voteCount=" + voteCount +
                ", voteAverage=" + voteAverage +
                '}';
    }
}
