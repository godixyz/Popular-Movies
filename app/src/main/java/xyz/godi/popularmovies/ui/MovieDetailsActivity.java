package xyz.godi.popularmovies.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.godi.popularmovies.R;
import xyz.godi.popularmovies.model.Movie;
import xyz.godi.popularmovies.utils.AppBarStateChangeListener;

public class MovieDetailsActivity extends AppCompatActivity {

    // Sharing TAG
    private static final String SHARE_TAG = "#PopularMovieByFarouk";

    // our Movie object
    private Movie movie;

    // Bind views using ButterKnife
    @BindView(R.id.details_colapsingToolbar) CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.details_appBar) AppBarLayout appBarLayout;
    @BindView(R.id.headerImage) ImageView movieHeaderImage;
    @BindView(R.id.tv_original_title) TextView movieTitle;
    @BindView(R.id.date_tv) TextView release_date;
    @BindView(R.id.vote_tv) TextView vote_count;
    @BindView(R.id.description_content) TextView movie_description;
    @BindView(R.id.stars_rb) RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        // take data from child activity
        getIntentData();
        // inflate data into views
        inflateData();
    }

    // inflate data in views
    private void inflateData() {
        toolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorWhite));
        toolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorWhite));
        // appbar propriety
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onIdle(AppBarLayout appBarLayout) {
            }

            @Override
            public void onCollapsed(AppBarLayout appBarLayout) {
                toolbar.setTitle(movie.getOriginal_title());
            }

            @Override
            public void onExpanded(AppBarLayout appBarLayout) {
                toolbar.setTitle("");
            }
        });

        // set the header image
        // build the image url string first
        String header_image_url = "http://image.tmdb.org/t/p/original/" + movie.getBackdrop_path();
        Picasso.get()
                .load(header_image_url)
                .placeholder(new ColorDrawable(Color.GRAY))
                .error(new ColorDrawable(Color.BLACK))
                .into(movieHeaderImage);

        // set the movie title
        movieTitle.setText(movie.getOriginal_title());

        // set the release date
        release_date.setText(movie.getRelease_date());

        // compute rating
        float rating = (float) Math.round(movie.getVote_average() * 10) / 10;

        // set stars
        ratingBar.setRating(rating);

        // set the vote count
        vote_count.setText(String.valueOf(movie.getVote_count()));

        // set movie overview
        movie_description.setText(movie.getOverview());
    }

    // Method to get the data received from the previous activity
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            movie = Objects.requireNonNull(intent.getExtras()).getParcelable(Movie.TAG);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share_menu) {
            shareMovie(movie.getOriginal_title() + " is trending now ! " + "\n" + "Overview : " +
                    movie.getOverview() + "\n" + SHARE_TAG);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareMovie(String text) {
        // set the sharing mime type
        String mimeType = "text/plain";

        // the sharing intent title
        String title = "Share movie with";

        // build the share intent with ShareCompat class
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setText(text)
                .setChooserTitle(title)
                .startChooser();
    }
}