package com.example.popularmovies.rizwan;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.popularmovies.rizwan.Database.MovieDatabase;
import com.example.popularmovies.rizwan.Database.MovieRepository;
import com.example.popularmovies.rizwan.Database.Movies;
import com.example.popularmovies.rizwan.ViewModel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyMovieAdapter.MyMovieAdapterOnClickHandler{

    private static final String TAG = MainActivity.class.getName();

    private String API_URL_DEFAULT;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView error_text;
    private Button refresh_button;
    protected static final String API_KEY = "3b334bb50e5ad342c7188bb61d6dcf48";
    public static String API_COMPLETE_URL = "";
    private static String SAVED_API_URL = "saved_url";
    private String API_URL_TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
    private String API_URL_POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key=";
    private RelativeLayout relativeLayout;
    private static MovieDatabase movieDatabase;
    private MovieRepository movieRepository;
    private MainActivityViewModel viewModel;
    static Context mContext;
    private boolean showFav = false;

    public MainActivity() {
        API_URL_DEFAULT = "http://api.themoviedb.org/3/movie/now_playing?api_key=";
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(SAVED_API_URL, API_COMPLETE_URL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycleView);
        relativeLayout = findViewById(R.id.relative);
        progressBar = findViewById(R.id.pb_loading_indicator);
        error_text = findViewById(R.id.tv_error_message_display);
        refresh_button = findViewById(R.id.refresh);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        movieDatabase = MovieDatabase.getInstance(this);
        movieRepository = new MovieRepository(getApplication());

        if(savedInstanceState != null){
            API_COMPLETE_URL = savedInstanceState.getString(SAVED_API_URL);
        } else {
            API_COMPLETE_URL = API_URL_DEFAULT.concat(API_KEY);
        }
        mContext = getApplicationContext();

        Log.d(TAG, API_COMPLETE_URL + " In Main Activity on Launch");
        loadData();

        viewModel.getMovieList().observe(this, new Observer<List<MyMovie>>() {
            @Override
            public void onChanged(List<MyMovie> movies) {
                if(movies.size() == 0){
                    loadInvisibleDataUI();
                    Log.d(TAG, "There is no movie.");
                } else {
                    loadVisibleDataUI();
                    updateData(movies);
                }
            }
        });
        // Click on the Refresh Button calls and API and loads the UI again.
        refresh_button.setOnClickListener(this);
    }

    // loadData will load the UI after checking the network. If network is not available, it will show from db.

    private void loadData(){
            if (Utility.checkConnectivity(this)) {
                Log.d(TAG, "Connectivity is available, LOADING FROM SERVER");
                viewModel.loadFromServer();
            } else {
                Log.d(TAG, "Connectivity is not available, loading from DB");
                viewModel.loadFromDB();
            }
        }
    // This method loads the enables the UI when the Data from the API is available
    private void loadVisibleDataUI(){
        recyclerView.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.GONE);
        refresh_button.setVisibility(View.GONE);
        error_text.setVisibility(View.GONE);
    }

    //  This method loads the enables the UI when the Data from the API is not available.
    private void loadInvisibleDataUI(){
        relativeLayout.setVisibility(View.VISIBLE);
        error_text.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        refresh_button.setVisibility(View.VISIBLE);
        refresh_button.setEnabled(true);
        error_text.setText(R.string.no_interent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.top_rated:
                setTitle(getString(R.string.app_name).concat(": Top Rated Movies"));
                API_URL_DEFAULT = API_URL_TOP_RATED;
                API_COMPLETE_URL = API_URL_DEFAULT.concat(API_KEY);
                loadData();
                break;
            case R.id.popular:
                setTitle(getString(R.string.app_name).concat(": Most Popular Movies"));
                API_URL_DEFAULT = API_URL_POPULAR;
                API_COMPLETE_URL = API_URL_DEFAULT.concat(API_KEY);
                loadData();
                break;
            case R.id.only_favorites:
                setTitle(getString(R.string.app_name).concat(": Your Favourites"));
                showFav = true;
                loadAllFavoriteMovies();
                break;
            default:
                throw new IllegalArgumentException();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadAllFavoriteMovies(){
            new loadAllFavoriteMovies().execute();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.refresh){
            loadData();
        }
    }

    private class loadAllFavoriteMovies extends AsyncTask<Void, Void, List<MyMovie>> {
        @Override
        protected List<MyMovie> doInBackground(Void... voids) {
            List<Movies> moviesDb = movieRepository.fetchFavoriteMovies(true);
            if (moviesDb != null) {
                return convertDbMovie(moviesDb);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MyMovie> movies) {
            super.onPostExecute(movies);
            if(movies !=  null && movies.size() == 0){
                noFavMoviesUI();
            } else {
                favMoviesUI(movies);
            }
        }
    }

    public static List<MyMovie> convertDbMovie(List<Movies> movies){
            List<MyMovie> myFavMovies = new ArrayList<>();
            if(movies!= null && movies.size() != 0) {
                for (Movies movie: movies) {
                    myFavMovies.add(new MyMovie(movie.getMovieId(),
                            movie.getTitle(),
                            movie.getOverview(),
                            movie.getPoster_path(),
                            movie.getBackdrop_path(),
                            movie.getRating(),
                            movie.getRelease_date(),
                            movie.getVoteCount(),
                            movie.isFavorite()));
                }
            }
        return myFavMovies;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(showFav) observeLiveFav();
        else setTitle(getString(R.string.app_name).concat(": Recent Movies"));
    }

    private void observeLiveFav(){
            this.setTitle(getString(R.string.app_name).concat(": Your Favourites"));
            movieDatabase.daoAccess().fetchAllFavoriteMovies(true).observe(this, new Observer<List<Movies>>() {
                @Override
                public void onChanged(List<Movies> movies) {
                    Log.d(TAG, "Inside Observer");
                    List<MyMovie> myMovies = new ArrayList<>();
                    if (movies != null) {
                        myMovies = convertDbMovie(movies);
                    }
                    if (movies != null && movies.size() == 0) {
                        noFavMoviesUI();
                        showFav = false;
                    } else {
                        favMoviesUI(myMovies);
                    }
                }
            });
        }

    private void updateData(List<MyMovie> myMovies) {
        if(myMovies != null && myMovies.size() != 0) {
           favMoviesUI(myMovies);
        } else {
            error_text.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            loadInvisibleDataUI();
        }
    }

 //The Onclick will launch the MovieInformation activity. @param - MyMovie object for the movie which the user clicked on
    @Override
    public void onClick(MyMovie movie) {
        Intent intent = new Intent(getApplicationContext(), MovieItem.class);
        intent.putExtra("MyMovie", movie);
        startActivity(intent);
    }

    private void noFavMoviesUI(){
        recyclerView.setAdapter(null);
        relativeLayout.setVisibility(View.VISIBLE);
        error_text.setVisibility(View.VISIBLE);
        error_text.setText(getString(R.string.no_fav_text));
    }

    private void favMoviesUI(List<MyMovie> movies){
        progressBar.setVisibility(View.GONE);
        MyMovieAdapter adapter = new MyMovieAdapter(movies, this);
        adapter.setMyMovies(movies);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        String title = getTitle().toString();
        if (title.contains("Favourite")) {
            setTitle(getString(R.string.app_name).concat(": Recent Movies"));
            loadData();
        } else {
            super.onBackPressed();
        }
    }
}