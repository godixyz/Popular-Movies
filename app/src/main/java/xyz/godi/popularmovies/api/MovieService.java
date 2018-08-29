package xyz.godi.popularmovies.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {

    // get a list of popular movies
    @GET("movie/popular")
    Call<MovieAPIresponse> getPopularMovies(@Query("api_key") String api_key);

    // get a list of top rated movies
    @GET("movie/top_rated")
    Call<MovieAPIresponse> getTopRatedMovies(@Query("api_key") String api_key);
}
