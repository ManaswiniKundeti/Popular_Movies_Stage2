package com.example.popular_movies_stage2.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.popular_movies_stage2.R;
import com.example.popular_movies_stage2.adapter.GridAdapter;
import com.example.popular_movies_stage2.database.AppDatabase;
import com.example.popular_movies_stage2.database.MovieDao;
import com.example.popular_movies_stage2.model.Movie;
import com.example.popular_movies_stage2.model.MovieResult;
import com.example.popular_movies_stage2.retrofit.MovieService;
import com.example.popular_movies_stage2.retrofit.RetrofitInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    final static String POPULAR = "popular";

    final static String TOP_RATED = "top_rated";
    //store movies retrieved from api call
    public List<Movie> movieList = new ArrayList<Movie>();

    GridAdapter movieAdapter;

    private ProgressBar movieProgressBar;

    private MovieService mMovieService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getString(R.string.activity_main_title));

        GridView mGridView = findViewById(R.id.movie_grid_data);
        movieAdapter = new GridAdapter(this, movieList);
        mGridView.setAdapter(movieAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = MainActivity.this;
                Class destinationActivity = MovieDetailActivity.class;
                Intent createMovieDetailActivityIntent = new Intent(context, destinationActivity);

                Movie selectedMovie = movieAdapter.getItem(position);
                String movieId = selectedMovie.getmMovieId();

                createMovieDetailActivityIntent.putExtra(Intent.EXTRA_TEXT,movieId);
                startActivity(createMovieDetailActivityIntent);

            }
        });

        movieProgressBar = findViewById(R.id.movie_progress_bar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    public void onItemClick(MenuItem item){

        if(item.getItemId() == R.id.popular) {
            getMovies(POPULAR);
        } else if (item.getItemId() == R.id.top_rated) {
//            Toast toast = Toast.makeText(getApplicationContext(), "Item 2 clicked", Toast.LENGTH_SHORT);
//            toast.show();
            getMovies(TOP_RATED);
        } else if (item.getItemId() == R.id.favourites) {
            fetchFavMoviesFromDb();
            Toast toast = Toast.makeText(getApplicationContext(), "Favourites clicked", Toast.LENGTH_SHORT);
            toast.show();

        }
    }

    private void fetchFavMoviesFromDb() {
        //fetch fav movies from Db
        MovieDao dao = AppDatabase.getInstance(getApplicationContext())
                .movieDao();

        //copy DbMovie data object to Model movie obj that is passed to adapter
        List<com.example.popular_movies_stage2.database.Movie> dbMovieList = dao.loadAllMovies();
        List<Movie> favMovies = new ArrayList<>();

        for (com.example.popular_movies_stage2.database.Movie dbMovie : dbMovieList) {
            favMovies.add(new Movie(dbMovie.getMovieName(),
                    dbMovie.getPosterPath(),
                    dbMovie.getMovieId(),
                    dbMovie.getDescription(),
                    dbMovie.getRating(),
                    dbMovie.getReleaseDate()));
        }

        if (favMovies.size() > 0) {
            movieList.clear();
            movieList.addAll(favMovies);

            movieAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (movieList.isEmpty()) {
            getMovies(POPULAR);
        }
    }

    private void getMovies(String filter) {
        if (mMovieService == null) {
            mMovieService = RetrofitInstance.getRetorfitInstance().create(MovieService.class);
        }

        Call<MovieResult> call = mMovieService.getMovies(filter, RetrofitInstance.API_KEY);
        toggleProgressBar(true);

        call.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                toggleProgressBar(false);
                movieList.clear();

                if (response.isSuccessful()&& response.body() != null) {
                    movieList.addAll(response.body().getMovieList());
                    movieAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                Log.e(TAG, "Error fetching movies", t);
                Toast.makeText(MainActivity.this,
                        "Error fetching movies",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void toggleProgressBar(boolean isLoading) {
        movieProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
}

