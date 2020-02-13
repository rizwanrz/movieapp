package com.example.popularmovies.rizwan;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.example.popularmovies.rizwan.Database.MovieRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class Utility {

    private static final String TAG = Utility.class.getName();

    public static List<MyMovie> formatMovieJson(String api_response){
        Log.d(TAG, "API Response: " +api_response);

        List<MyMovie> myMovies = new ArrayList<>();
        try {
            JSONObject api_result = new JSONObject(api_response);
            JSONArray results = api_result.getJSONArray("results");

            for(int i=0; i<results.length(); i++) {
                JSONObject each_movie = results.getJSONObject(i);
                String title = each_movie.getString("title");
                String poster_path = each_movie.getString("poster_path");
                String vote_average = each_movie.getString("vote_average");
                long id = each_movie.getLong("id");
                String overview = each_movie.getString("overview");
                String backdrop_path = each_movie.getString("backdrop_path");
                String release_date = each_movie.getString("release_date");
                String rating = each_movie.getString("popularity");

                Log.d(TAG, "Movie Details: " + title + "\t" + id + "\t" + vote_average + "\t" + poster_path + "\t" +
                        backdrop_path + "\t" + release_date + "\t" + overview + "\n");

                myMovies.add(new MyMovie(id, title, overview, poster_path, backdrop_path, rating, release_date, vote_average, false));
                    MovieRepository movieRepository = new MovieRepository(MainActivity.mContext);
                    movieRepository.insert(id, title, overview, poster_path, backdrop_path, rating, release_date, vote_average, false);
            }

        } catch (JSONException e){
            Log.d(Utility.class.getName(), e.toString());
        }
        return myMovies;
    }

    static List<EachVideo> formatTrailerJson(String trailer_api_response){
        List<EachVideo> movieTrailers = new ArrayList<>();

        try {
            JSONObject api_result = new JSONObject(trailer_api_response);
            JSONArray results = api_result.getJSONArray("results");
            for(int i=0; i<results.length(); i++) {
                JSONObject each_trailer = results.getJSONObject(i);
                String id = each_trailer.getString("id");
                String videoSite =  each_trailer.getString("site");
                String videoName = each_trailer.getString("name");
                String key = each_trailer.getString("key");
                int size = each_trailer.getInt("size");

                Log.d(TAG, "Trailer Detail: " + videoName + "\t" + id + "\t" + videoSite + "\t" + key + "\t" + size + "\t");
                movieTrailers.add(new EachVideo(id, videoName, key, videoSite, size));
            }

        } catch (JSONException e){
            Log.d(Utility.class.getName(), e.toString());
        }
        return movieTrailers;
    }

    static List<EachReview> formatReviewsJson(String review_api_response){
        List<EachReview> movieReviews = new ArrayList<>();

        try {
            JSONObject api_result = new JSONObject(review_api_response);
            int numberOfReviews = api_result.getInt("total_results");
            JSONArray results = api_result.getJSONArray("results");

            for(int i=0; i<results.length(); i++) {
                JSONObject each_review = results.getJSONObject(i);
                String id = each_review.getString("id");
                String author =  each_review.getString("author");
                String content = each_review.getString("content");
                String url = each_review.getString("url");

                Log.d(TAG, "Review Details: " + id + "\t" + content + "\t" + author + "\t" + url + "\t");
                movieReviews.add(new EachReview(id, author, content, url));
            }

        } catch (JSONException e){
            Log.d(Utility.class.getName(), e.toString());
        }

        return movieReviews;
    }

    public static String apiCall(String API_COMPLETE_URL){
        String response = "";
        String input;
        try {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(API_COMPLETE_URL);
                urlConnection = (HttpURLConnection) url.openConnection();

                if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);
                    BufferedReader reader = new BufferedReader(isw);
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((input = reader.readLine()) != null) {
                        stringBuilder.append(input);
                    }
                    reader.close();
                    in.close();
                    response = stringBuilder.toString();
                } else {
                    response = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception: " + e.getMessage();
        }

        if (response!=null && response.equals("")) {
            Log.d(TAG, "Response is Empty!!!");
        } else {
        Log.d(TAG, "No Response from API Call");
        }
        return response;
    }

    static boolean checkConnectivity(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        Log.d(TAG, "Network Connectivity: "+isConnected);
        return isConnected;
    }
}
