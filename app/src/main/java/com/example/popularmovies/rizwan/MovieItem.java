package com.example.popularmovies.rizwan;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.popularmovies.rizwan.Fragments.MovieDetailsFragment;
import com.example.popularmovies.rizwan.Fragments.ReviewFragment;
import com.example.popularmovies.rizwan.Fragments.VideoFragment;

import java.io.Serializable;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MovieItem extends AppCompatActivity implements Button.OnClickListener {
    private static String TAG = MovieItem.class.getName();
    private static String api;
    long movieID;
    private static final String REVIEWS = "reviews";
    private static final String TRAILERS = "videos";
    private static String API_TRAILERS_MOVIE;
    private static String API_REVIEWS_MOVIE;
    private static List<EachVideo> eachVideos;
    private  static List<EachReview> eachReviews;
    private static Button readReviews;
    private static Button showTrailers;
    MyMovie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_info);

        Intent intent = getIntent();
        movie = (MyMovie) intent.getSerializableExtra("MyMovie");
        assert movie != null;
        movieID = movie.getId();
        readReviews = findViewById(R.id.readReviews);
        showTrailers = findViewById(R.id.showTrailers);

        getIntent().putExtra("movie", movie);
        setTitle(movie.getTitle());

        readReviews.setOnClickListener(this);
        showTrailers.setOnClickListener(this);
        //Load Reviews and Videos
        initializeTrailerAndReviews(movieID);

        // load Movie Details Fragment
        loadFragment(new MovieDetailsFragment());
    }

    @Override
    public void onClick(View view) {
        backPressed(view);
    }


    private void initializeTrailerAndReviews(long movieID){
        API_TRAILERS_MOVIE = "https://api.themoviedb.org/3/movie/"+movieID+"/videos?api_key="+MainActivity.API_KEY+"&language=en-US";
        API_REVIEWS_MOVIE = "https://api.themoviedb.org/3/movie/"+movieID+"/reviews?api_key="+MainActivity.API_KEY;
    }

    private class LoadVideoTrailerAsyncTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        private String TAG = LoadVideoTrailerAsyncTask.class.getName();

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null)
               Log.d(TAG, s);
            if (api.contains(TRAILERS)) {
                eachVideos = Utility.formatTrailerJson(s);
                Fragment fragment = new VideoFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("videos", (Serializable) eachVideos);
                fragment.setArguments(bundle);
                loadFragment(fragment);
            } else if(api.contains(REVIEWS)) {
                eachReviews = Utility.formatReviewsJson(s);
                Fragment fragment = new ReviewFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("reviews", (Serializable) eachReviews);
                fragment.setArguments(bundle);
                loadFragment(fragment);
            } else {
                    Log.d(TAG, "Invalid JSON received!!!");
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            if(strings.length == 0 || strings.length > 1){
                Log.d(TAG, "Invalid Arguments Passed to execute REVIEW/TRAILER API");
                return null;
            }

            switch (strings[0]) {
                case TRAILERS:
                    api = API_TRAILERS_MOVIE;
                    break;
                case REVIEWS:
                    api = API_REVIEWS_MOVIE;
                    break;
                default:
                    api = null;
                    Log.d(TAG, "Invalid Async Call arguments received!!!");
            }
            if(api != null)
                return Utility.apiCall(api);
            return null;
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        String title = getTitle().toString();

        if(title.equalsIgnoreCase(movie.getTitle())){
            super.onBackPressed();
        } else if(title.contains("Reviews")){
            backPressedOnReview();
        } else if(title.contains("Trailers")){
            backPressedOnTrailers();
        } else {
            Log.d(TAG, "Invalid State of Back Pressed");
        }
    }

    private void backPressedOnReview(){
        if(readReviews.getText().equals(getString(R.string.read_review_button_text))) {
            setTitle(movie.getTitle().concat(": Reviews"));
            new LoadVideoTrailerAsyncTask().execute(REVIEWS);
            readReviews.setText(getString(R.string.hide_review_button));
            showTrailers.setVisibility(View.GONE);
        } else{
            readReviews.setText(getString(R.string.read_review_button_text));
            setTitle(movie.getTitle());
            showTrailers.setVisibility(View.VISIBLE);
            loadFragment(new MovieDetailsFragment());
        }
    }

    private void backPressedOnTrailers(){
        if(showTrailers.getText().equals(getString(R.string.check_the_trailers))) {
            Log.d(TAG, "Trailers Clicked");
            setTitle(movie.getTitle().concat(": Trailers"));
            new LoadVideoTrailerAsyncTask().execute(TRAILERS);
            showTrailers.setText(R.string.hide_video_button);
            readReviews.setVisibility(View.GONE);
        } else {
            setTitle(movie.getTitle());
            loadFragment(new MovieDetailsFragment());
            showTrailers.setText(R.string.check_the_trailers);
            readReviews.setVisibility(View.VISIBLE);
        }
    }

    private void backPressed(View view){
        switch (view.getId()){
            case R.id.readReviews:
                backPressedOnReview();
                break;
            case R.id.showTrailers:
                backPressedOnTrailers();
                break;
            default:
                Log.d(TAG,"Wrong item Clicked");
                throw new IllegalArgumentException();
        }
    }
}