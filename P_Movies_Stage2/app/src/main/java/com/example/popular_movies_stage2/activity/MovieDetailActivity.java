package com.example.popular_movies_stage2.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popular_movies_stage2.R;
import com.example.popular_movies_stage2.adapter.ListAdapter;
import com.example.popular_movies_stage2.database.AppDatabase;
import com.example.popular_movies_stage2.database.MovieDao;
import com.example.popular_movies_stage2.model.Movie;
import com.example.popular_movies_stage2.model.MovieReview;
import com.example.popular_movies_stage2.model.MovieReviewResults;
import com.example.popular_movies_stage2.model.MovieVideo;
import com.example.popular_movies_stage2.model.MovieVideoResult;
import com.example.popular_movies_stage2.retrofit.MovieService;
import com.example.popular_movies_stage2.retrofit.RetrofitInstance;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView mDetailImage;
    private TextView mNameTextView;
    private TextView mSummaryTextView;
    private TextView mRatingTextView;
    private TextView mReleaseDateTextView;
    private Button mTrailer1Button;
    private Button mTrailer2Button;

    private Button mFavouritesButton;

    private MovieService mMovieService;

    ListAdapter mListAdapter;

    List<MovieReviewResults> reviewResultList  = new ArrayList<>();

    private Movie mMovie = null;

    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setTitle(getString(R.string.activity_details_title));

        mDetailImage = findViewById(R.id.movie_detail_image);
        mNameTextView = findViewById(R.id.movie_detail_name);
        mSummaryTextView = findViewById(R.id.movie_detail_summary);
        mRatingTextView = findViewById(R.id.movie_detail_rating);
        mReleaseDateTextView = findViewById(R.id.movie_detail_release_date);
        mTrailer1Button = findViewById(R.id.trailer1_button);
        mTrailer2Button = findViewById(R.id.trailer2_button);
        mFavouritesButton = findViewById(R.id.favourites_button);

        ListView movieListView = findViewById(R.id.movie_review_data_list_view);
        movieListView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        mListAdapter = new ListAdapter(this, reviewResultList);
        movieListView.setAdapter(mListAdapter);

        Intent intentThatStartedThisActivity = getIntent();

        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)){
            String movieId = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            getMovie(movieId);
            getTrailer(movieId);
            getReviews(movieId);
        }

        mFavouritesButton.setEnabled(false);
        mFavouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfMovieIsFavorite()) {
                    unfavoriteMovie();
                } else {
                    favoriteMovie();
                }
            }
        });

    }

    private void unfavoriteMovie() {
        MovieDao dao = AppDatabase.getInstance(getApplicationContext())
                .movieDao();
        dao.deleteMovie(dao.getMovie(mMovie.getmMovieId()).get(0));

        mFavouritesButton.setText(getString(R.string.activity_details_favourites));
    }

    private boolean checkIfMovieIsFavorite() {
        if (mMovie == null) {
            return false;
        }

        MovieDao dao = AppDatabase.getInstance(getApplicationContext())
                .movieDao();
        List<com.example.popular_movies_stage2.database.Movie> movieList = dao.getMovie(mMovie.getmMovieId());
        if (movieList == null || movieList.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void favoriteMovie() {
        if (mMovie == null) {
            return;
        }

        com.example.popular_movies_stage2.database.Movie dbMovie = new com.example.popular_movies_stage2.database.Movie(
                mMovie.getmMovieId(),
                mMovie.getmMovieName(),
                mMovie.getmMovieImageUri(),
                mMovie.getmRating(),
                mMovie.getmReleaseDate(),
                mMovie.getmSummary()
        );

        MovieDao dao = AppDatabase.getInstance(getApplicationContext())
                .movieDao();
        dao.insertMovieDetails(dbMovie);

        mFavouritesButton.setText("Remove from favorites");

        Toast.makeText(this, "Movie added to favorites", Toast.LENGTH_SHORT).show();
    }

    private void getMovie(final String movieId) {
        if (mMovieService == null) {
            mMovieService = RetrofitInstance.getRetorfitInstance().create(MovieService.class);
        }

        Call<Movie> call = mMovieService.getMovie(movieId, RetrofitInstance.API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    final Movie movie = response.body();
                    mMovie = movie;

                    Picasso.get()
                            .load(Uri.parse("https://image.tmdb.org/t/p/w500" + movie.getmMovieImageUri()))
                            .into(mDetailImage);
                    mNameTextView.setText(movie.getmMovieName());
                    mSummaryTextView.setText(movie.getmSummary());
                    mRatingTextView.setText(movie.getmRating());
                    mReleaseDateTextView.setText(movie.getmReleaseDate());
                    mFavouritesButton.setEnabled(true);
                    if (checkIfMovieIsFavorite()) {
                        mFavouritesButton.setText("Remove from favorites");
                    }
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e(TAG, "Error fetching movie", t);
                Toast.makeText(MovieDetailActivity.this,
                        "Error fetching movie",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void playTrailerOnClick(final String trailerKey){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerKey));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailerKey));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex){
            startActivity(webIntent);
        }
    }

    private void getTrailer(final String movieId) {
        if (mMovieService == null) {
            mMovieService = RetrofitInstance.getRetorfitInstance().create(MovieService.class);
        }

        Call<MovieVideo> call = mMovieService.getTrailer(movieId, RetrofitInstance.API_KEY);
        call.enqueue(new Callback<MovieVideo>() {
            @Override
            public void onResponse(Call<MovieVideo> call, Response<MovieVideo> response) {

                if(response.isSuccessful() && response.body() != null){
                    MovieVideo videoDetail = response.body();
                    List<MovieVideoResult> resultList = videoDetail.getmVideoResults();

                    for(int i=0; i< 2; i++) {
                        try {
                            final String trailerKey = resultList.get(i).getmVideoKey();
                            if(i == 0){
                                mTrailer1Button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        playTrailerOnClick(trailerKey);
                                    }
                                });

                            } else if( i == 1){
                                mTrailer2Button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        playTrailerOnClick(trailerKey);
                                    }
                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<MovieVideo> call, Throwable t) {
                Log.e(TAG, "Error fetching movie trailer details", t);
                Toast.makeText(MovieDetailActivity.this,
                        "Error fetching movie trailer details",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getReviews(final String movieId) {
        if (mMovieService == null) {
            mMovieService = RetrofitInstance.getRetorfitInstance().create(MovieService.class);
        }

        Call<MovieReview> call = mMovieService.getReviews(movieId, RetrofitInstance.API_KEY);
        call.enqueue(new Callback<MovieReview>() {
            @Override
            public void onResponse(Call<MovieReview> call, Response<MovieReview> response) {

                if(response.isSuccessful() && response.body() != null){
                    reviewResultList.clear();

                    MovieReview reviewdetails = response.body();
                    reviewResultList.addAll(reviewdetails.getmReviewResults());

                    mListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MovieReview> call, Throwable t) {
                Log.e(TAG, "Error fetching movie review details", t);
                Toast.makeText(MovieDetailActivity.this,
                        "Error fetching movie review details",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
