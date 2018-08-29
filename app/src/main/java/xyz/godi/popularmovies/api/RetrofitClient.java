package xyz.godi.popularmovies.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL =
            "https://api.themoviedb.org/3/";
    // Some useful constants
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        // if our retrofit object is null
        // build one
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        // then return it
        return retrofit;
    }
}