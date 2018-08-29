package xyz.godi.popularmovies.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.godi.popularmovies.R;
import xyz.godi.popularmovies.api.MovieAPIresponse;
import xyz.godi.popularmovies.api.MovieService;
import xyz.godi.popularmovies.api.RetrofitClient;
import xyz.godi.popularmovies.model.Movie;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String API_KEY = "03ac1bb2cf9b74661d41fbca8087307b";

    // Shared Preferences to save sort settings
    SharedPreferences mSharedPref;

    // Bind views using ButterKnife
    @BindView(R.id.movie_rv)
    RecyclerView movie_recycler;
    @BindView(R.id.loading_spinner)
    ProgressBar loading_spinner;
    @BindView(R.id.empty_view)
    TextView emptyView_tv;
    @BindView(R.id.no_internet_iv)
    ImageView no_internet_iv;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // set layout manager
        movie_recycler.setLayoutManager(new GridLayoutManager(this, getSpanCount()));

        // Get reference to Connectivity manager to check for network stat
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // get the current network
        assert cm != null;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        //
        mSharedPref = getSharedPreferences("sortSettings",MODE_PRIVATE);

        String sorting = mSharedPref.getString("sort","popular");

        // if there a network, load movies
        if (networkInfo != null && networkInfo.isConnected()) {
            loadPopularMovies();
        } else {
            loading_spinner.setVisibility(View.INVISIBLE);
            no_internet_iv.setVisibility(View.VISIBLE);
            emptyView_tv.setText(R.string.no_internet_connection);
        }

        // refresh content onSwipeToRefresh
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // call to refreshContent
                refreshContent();
            }
        });
    }

    // check if the device is in landscape mode
    private int getSpanCount() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return 4;
        }
        return 2;
    }

    // Method to load movies
    private void loadPopularMovies() {
        MovieService apiService = RetrofitClient.getClient().create(MovieService.class);

        Call<MovieAPIresponse> call = apiService.getPopularMovies(API_KEY);

        call.enqueue(new Callback<MovieAPIresponse>() {
            @Override
            public void onResponse(Call<MovieAPIresponse> call, Response<MovieAPIresponse> response) {
                if (response.isSuccessful()) {
                    List<Movie> movies = fetchResults(response);
                    movie_recycler.setAdapter(new MovieAdapter(getApplicationContext(), movies));
                    // hide the loading indicator
                    loading_spinner.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error while receiving data from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieAPIresponse> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }

    // Method to load top rated movies
    private void loadTopRatedMovies() {
        MovieService apiService = RetrofitClient.getClient().create(MovieService.class);

        Call<MovieAPIresponse> call = apiService.getTopRatedMovies(API_KEY);

        call.enqueue(new Callback<MovieAPIresponse>() {
            @Override
            public void onResponse(Call<MovieAPIresponse> call, Response<MovieAPIresponse> response) {
                if (response.isSuccessful()) {
                    List<Movie> movies = fetchResults(response);
                    movie_recycler.setAdapter(new MovieAdapter(getApplicationContext(), movies));
                    // hide the loading indicator
                    loading_spinner.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error while receiving data from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieAPIresponse> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }

    private List<Movie> fetchResults(Response<MovieAPIresponse> response) {
        MovieAPIresponse movieAPIresponse = response.body();
        return movieAPIresponse.getResults();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.about) {
            // TODO Show AboutActivity
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;

        } else if (itemId == R.id.sort) {
            showSortDialog();
        }
        return super.onOptionsItemSelected(item);

    }

    private void showSortDialog() {
        // options to display in dialog
        String[] sortOptions = {"Popular","Top rated"};

        //  Create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort by")
                .setIcon(R.drawable.ic_sort_15dp)
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // load movies accordingly to the selected option
                        if (which == 0) {
                            loadPopularMovies();
                            recreate();
                        } else if (which == 1) {
                            loadTopRatedMovies();
                        }loadTopRatedMovies();
                    }
                }).show(); // show the alert dialog
    }


    // Method to call on refresh
    private void refreshContent() {
        // TODO: 8/4/18 To be implemented
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                loadPopularMovies();
                swipeRefresh.setRefreshing(false);
            }
        },3000);
        // stop the refreshing
    }
}