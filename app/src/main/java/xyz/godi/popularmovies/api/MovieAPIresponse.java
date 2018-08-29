package xyz.godi.popularmovies.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import xyz.godi.popularmovies.model.Movie;

/**
 * utilities class created to parse Movie JSON received from the movie api
 */
public class MovieAPIresponse {

    @SerializedName("results")
    private List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }

    public List<Movie> getMovies() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public void setMovies(List<Movie> results) {
        this.results = results;
    }

    public MovieAPIresponse() {

    }
}